package com.phoebelord.service;

import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.dao.GuestRepository;
import com.phoebelord.dao.RelationshipRepository;
import com.phoebelord.dao.TableRepository;
import com.phoebelord.exception.AppException;
import com.phoebelord.model.Config;
import com.phoebelord.model.Guest;
import com.phoebelord.model.Relationship;
import com.phoebelord.model.Table;
import com.phoebelord.payload.ConfigRequest;
import com.phoebelord.payload.GuestRequest;
import com.phoebelord.payload.RelationshipRequest;
import com.phoebelord.payload.TableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

  @Autowired
  GuestRepository guestRepository;

  @Autowired
  ConfigRepository configRepository;

  @Autowired
  RelationshipRepository relationshipRepository;

  @Autowired
  TableRepository tableRepository;

  public Config createConfig(ConfigRequest configRequest) {
    Config config = new Config(configRequest);
    configRepository.save(config);
    configRequest.getGuests().forEach(guestRequest -> {
      config.addGuest(createGuest(guestRequest, config));
    });

    addRelationships(configRequest.getGuests(), config);

    List<TableRequest> tableRequests = configRequest.getTables();
    int offset = 0;
    for (int i = 0; i < tableRequests.size(); i++) {
      TableRequest currentTableRequest = tableRequests.get(i);
      Table table = createTable(currentTableRequest, config, i, offset);
      config.addTable(table);
      offset += table.getCapacity();
    }

    return config;
  }

  public Guest createGuest(GuestRequest guestRequest, Config config) {
    Guest guest = new Guest(guestRequest);
    guest.setConfig(config);
    guestRepository.save(guest);
    return guest;
  }

  public Relationship createRelationship(RelationshipRequest relationshipRequest, Config config) {
    Relationship relationship = new Relationship();
    String guestName = relationshipRequest.getGuestName();
    Guest otherGuest = guestRepository.findByNameAndConfig(guestName, config).orElseThrow(() -> new AppException("Guest doesn't exist: " + guestName));
    relationship.setGuestId(otherGuest.getId());
    relationship.setLikability(relationshipRequest.getLikability());
    relationshipRepository.save(relationship);

    return relationship;
  }

  public Table createTable(TableRequest tableRequest, Config config, int index, int offset) {
    Table table = new Table(tableRequest);
    table.setConfig(config);
    table.setTableNum(index);
    table.setOffset(offset);
    tableRepository.save(table);
    return table;
  }

  public void addRelationships(List<GuestRequest> guestRequests, Config config) {
    for (GuestRequest guestRequest : guestRequests) {
      Guest guest = guestRepository.findByNameAndConfig(guestRequest.getName(), config)
        .orElseThrow(() -> new AppException("Guest doesn't exist: " + guestRequest.getName()));
      guestRequest.getRelationships().forEach(relationshipRequest -> {
        guest.addRelationship(createRelationship(relationshipRequest, config));
      });
      guestRepository.save(guest);
    }
  }
}

