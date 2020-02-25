package com.phoebelord.service;

import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.dao.GuestRepository;
import com.phoebelord.dao.RelationshipRepository;
import com.phoebelord.dao.TableRepository;
import com.phoebelord.exception.AppException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.Guest;
import com.phoebelord.model.Relationship;
import com.phoebelord.model.Table;
import com.phoebelord.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

  private Guest createGuest(GuestRequest guestRequest, Config config) {
    Guest guest = new Guest(guestRequest);
    guest.setConfig(config);
    guestRepository.save(guest);
    return guest;
  }

  private Relationship createRelationship(RelationshipRequest relationshipRequest, Config config) {
    Relationship relationship = new Relationship();
    String guestName = relationshipRequest.getGuestName();
    Guest otherGuest = guestRepository.findByNameAndConfig(guestName, config).orElseThrow(() -> new AppException("Guest doesn't exist: " + guestName));
    relationship.setGuestId(otherGuest.getId());
    relationship.setLikability(relationshipRequest.getLikability());
    relationshipRepository.save(relationship);

    return relationship;
  }

  private Table createTable(TableRequest tableRequest, Config config, int index, int offset) {
    Table table = new Table(tableRequest);
    table.setConfig(config);
    table.setTableNum(index);
    table.setOffset(offset);
    tableRepository.save(table);
    return table;
  }

  private void addRelationships(List<GuestRequest> guestRequests, Config config) {
    for (GuestRequest guestRequest : guestRequests) {
      Guest guest = guestRepository.findByNameAndConfig(guestRequest.getName(), config)
        .orElseThrow(() -> new AppException("Guest doesn't exist: " + guestRequest.getName()));
      guestRequest.getRelationships().forEach(relationshipRequest -> {
        guest.addRelationship(createRelationship(relationshipRequest, config));
      });
      guestRepository.save(guest);
    }
  }

  public ConfigResponse createConfigResponse(Config config) {
    ConfigResponse configResponse = new ConfigResponse(config);

    List<GuestRequest> guestRequests = new ArrayList<>();
    for(Guest guest: config.getGuests()) {
      GuestRequest guestRequest = new GuestRequest();
      guestRequest.setName(guest.getName());
      guestRequest.setRelationships(createRelationshipRequests(guest));
      guestRequests.add(guestRequest);
    }
    configResponse.setGuests(guestRequests);

    List<TableRequest> tableRequests = new ArrayList<>();
    for(Table table: config.getTables()) {
      TableRequest tableRequest = new TableRequest();
      tableRequest.setCapacity(table.getCapacity());
      tableRequest.setShape(table.getShape());
      tableRequests.add(tableRequest);
    }
    configResponse.setTables(tableRequests);

    return configResponse;
  }

  private List<RelationshipRequest> createRelationshipRequests(Guest guest) {
    List<RelationshipRequest> relationshipRequests = new ArrayList<>();
    for(Relationship relationship: guest.getRelationships()) {
      RelationshipRequest relationshipRequest = new RelationshipRequest();
      Guest otherGuest = guestRepository.findById(relationship.getGuestId()).orElseThrow(() -> new NotFoundException("User", "Id", relationship.getGuestId()));
      relationshipRequest.setGuestName(otherGuest.getName());
      relationshipRequest.setLikability(relationship.getLikability());
      relationshipRequests.add(relationshipRequest);
    }
    return relationshipRequests;
  }


}

