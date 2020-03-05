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

  public Config createConfig(NewConfigRequest newConfigRequest) {
    Config config = new Config(newConfigRequest);
    configRepository.save(config);
    newConfigRequest.getGuests().forEach(guestRequest -> {
      config.addGuest(createGuest(guestRequest, config));
    });

    addRelationships((List<GuestRequest>) newConfigRequest.getGuests(), config);

    List<TableRequest> tableRequests = (List<TableRequest>) newConfigRequest.getTables();
    int offset = 0;
    for (int i = 0; i < tableRequests.size(); i++) {
      TableRequest currentTableRequest = tableRequests.get(i);
      Table table = createTable(currentTableRequest, config, i, offset);
      config.addTable(table);
      offset += table.getCapacity();
    }

    return config;
  }

  //todo too long
  public Config editConfig(ConfigDTO configDTO) {
    Config config = configRepository.findById(configDTO.getId()).orElseThrow(() -> new NotFoundException("Config", "id", configDTO.getId()));
    config.setName(configDTO.getName());

    List<Guest> currentGuests = config.getGuests();
    List<GuestDTO> newGuests = configDTO.getGuests();

    List<Guest> deletedGuests = new ArrayList<>();
    for(Guest guest: currentGuests) {
      boolean found = newGuests.stream().anyMatch(guestDTO -> guestDTO.getId() == guest.getId());
      // Guest has been deleted
      if(!found) {
        guest.getRelationships().forEach(relationship -> relationshipRepository.delete(relationship));
        deletedGuests.add(guest);
        guestRepository.delete(guest);
      }
    }
    currentGuests.removeAll(deletedGuests);

    for(GuestDTO guestDTO: newGuests){
      // new guest
      if(guestDTO.getId() == -1) {
        Guest newGuest = createGuest(guestDTO, config);
        currentGuests.add(newGuest);
        guestDTO.setId(newGuest.getId());
      } else {
        Guest currentGuest = currentGuests.stream().filter(guest -> guest.getId() == guestDTO.getId()).findAny().get();
        currentGuest.setName(guestDTO.getName());
        guestRepository.save(currentGuest);
      }
    }

    for(Guest guest: currentGuests) {
      List<Relationship> currentRelationships = guest.getRelationships();
      List<RelationshipDTO> relationshipDTOS = newGuests.stream().filter(guestDTO -> guestDTO.getId() == guest.getId()).findAny().get().getRelationships();

      List<Relationship> deletedRelationships = new ArrayList<>();
      for(Relationship relationship: currentRelationships) {
        boolean found = relationshipDTOS.stream().anyMatch(relationshipDTO -> relationshipDTO.getId() == relationship.getId());
        boolean shouldDelete = deletedGuests.stream().anyMatch(deletedGuest -> deletedGuest.getId() == relationship.getGuestId());
        if(!found || shouldDelete) {
          relationshipRepository.delete(relationship);
          deletedRelationships.add(relationship);
        }
      }
      currentRelationships.removeAll(deletedRelationships);

      for(RelationshipDTO relationshipDTO: relationshipDTOS) {
        if(relationshipDTO.getId() == -1) {
          Relationship newRelationship = createRelationship(relationshipDTO, config, guest.getName());
          currentRelationships.add(newRelationship);
          relationshipDTO.setId(newRelationship.getId());
        } else {
          Relationship currentRelationship = currentRelationships.stream().filter(relationship -> relationship.getId() == relationshipDTO.getId()).findAny().get();
          currentRelationship.setBribe(relationshipDTO.getBribe());
          currentRelationship.setLikability(relationshipDTO.getLikability());
          Guest otherGuest = guestRepository.findByNameAndConfig(relationshipDTO.getGuestName(), config).get();
          if(guest.getId() == otherGuest.getId()) {
            throw new AppException("Guest: " + guest.getName() + " cannot have relationship with self");
          }
          currentRelationship.setGuestId(otherGuest.getId());
          relationshipRepository.save(currentRelationship);
        }
      }

      guest.setRelationships(currentRelationships);
      guestRepository.save(guest);
    }

    List<Table> currentTables = config.getTables();
    List<TableDTO> tableDTOs = configDTO.getTables();

    List<Table> deletedTables = new ArrayList<>();
    for(Table table: currentTables) {
      boolean found = tableDTOs.stream().anyMatch(tableDTO -> tableDTO.getId() == table.getId());
      // Table has been deleted
      if(!found) {
        tableRepository.delete(table);
        deletedTables.add(table);
      }
    }
    currentTables.removeAll(deletedTables);

    int offset = 0;
    for(int i = 0; i < tableDTOs.size(); i++) {
      TableDTO tableDTO = tableDTOs.get(i);
      Table table;
      if(tableDTO.getId() == -1) {
        table = createTable(tableDTO, config, i, offset);
        config.addTable(table);
        tableDTO.setId(table.getId());
      } else {
        table = currentTables.stream().filter(currentTable -> currentTable.getId() == tableDTO.getId()).findAny().get();
        table.setTableNum(i);
        table.setOffset(offset);
        table.setCapacity(tableDTO.getCapacity());
        table.setShape(tableDTO.getShape());
        tableRepository.save(table);
      }
      offset += table.getCapacity();
    }

    config.setGuests(currentGuests);
    config.setTables(currentTables);
    configRepository.save(config);
    return config;
  }

  Guest createGuest(GuestRequest guestRequest, Config config) {
    Guest guest = new Guest(guestRequest);
    guest.setConfig(config);
    guestRepository.save(guest);
    return guest;
  }


  Relationship createRelationship(RelationshipRequest relationshipRequest, Config config, String currentGuestName) {
    Relationship relationship = new Relationship();
    String guestName = relationshipRequest.getGuestName();
    if(guestName.equals(currentGuestName)) {
      throw new AppException("Guest: " + guestName + " cannot have a relationship with self");
    }
    Guest otherGuest = guestRepository.findByNameAndConfig(guestName, config).orElseThrow(() -> new AppException("Guest doesn't exist: " + guestName));
    relationship.setGuestId(otherGuest.getId());
    relationship.setLikability(relationshipRequest.getLikability());
    relationship.setBribe(relationshipRequest.getBribe());
    relationshipRepository.save(relationship);

    return relationship;
  }

  Table createTable(TableRequest tableRequest, Config config, int index, int offset) {
    Table table = new Table(tableRequest);
    table.setConfig(config);
    table.setTableNum(index);
    table.setOffset(offset);
    tableRepository.save(table);
    return table;
  }

  void addRelationships(List<GuestRequest> guestRequests, Config config) {
    for (GuestRequest guestRequest : guestRequests) {
      Guest guest = guestRepository.findByNameAndConfig(guestRequest.getName(), config)
        .orElseThrow(() -> new AppException("Guest doesn't exist: " + guestRequest.getName()));
      guestRequest.getRelationships().forEach(relationshipRequest -> {
        guest.addRelationship(createRelationship(relationshipRequest, config, guest.getName()));
      });
      guestRepository.save(guest);
    }
  }

  public ConfigDTO createConfigDTO(Config config) {
    ConfigDTO configDTO = new ConfigDTO(config);

    List<GuestDTO> guestDTOS = new ArrayList<>();
    for(Guest guest: config.getGuests()) {
      GuestDTO guestDTO = new GuestDTO();
      guestDTO.setId(guest.getId());
      guestDTO.setName(guest.getName());
      guestDTO.setRelationships(createRelationshipDTOs(guest));
      guestDTOS.add(guestDTO);
    }
    configDTO.setGuests(guestDTOS);

    List<TableDTO> tableDTOS = new ArrayList<>();
    for(Table table: config.getTables()) {
      TableDTO tableDTO = new TableDTO();
      tableDTO.setId(table.getId());
      tableDTO.setCapacity(table.getCapacity());
      tableDTO.setShape(table.getShape());
      tableDTOS.add(tableDTO);
    }
    configDTO.setTables(tableDTOS);

    return configDTO;
  }

  private List<RelationshipDTO> createRelationshipDTOs(Guest guest) {
    List<RelationshipDTO> relationshipDTOS = new ArrayList<>();
    for(Relationship relationship: guest.getRelationships()) {
      RelationshipDTO relationshipDTO = new RelationshipDTO();
      relationshipDTO.setId(relationship.getId());
      Guest otherGuest = guestRepository.findById(relationship.getGuestId()).orElseThrow(() -> new NotFoundException("User", "Id", relationship.getGuestId()));
      relationshipDTO.setGuestName(otherGuest.getName());
      relationshipDTO.setLikability(relationship.getLikability());
      relationshipDTO.setBribe(relationship.getBribe());
      relationshipDTOS.add(relationshipDTO);
    }
    return relationshipDTOS;
  }


}

