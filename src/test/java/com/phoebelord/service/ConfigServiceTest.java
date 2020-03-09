package com.phoebelord.service;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.dao.GuestRepository;
import com.phoebelord.dao.RelationshipRepository;
import com.phoebelord.dao.TableRepository;
import com.phoebelord.exception.AppException;
import com.phoebelord.model.Config;
import com.phoebelord.model.Guest;
import com.phoebelord.model.Relationship;
import com.phoebelord.model.Table;
import com.phoebelord.payload.GuestRequest;
import com.phoebelord.payload.NewConfigRequest;
import com.phoebelord.payload.RelationshipRequest;
import com.phoebelord.payload.TableRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChristmasDinner.class)
public class ConfigServiceTest {

  @Mock
  GuestRepository guestRepository;

  @Mock
  ConfigRepository configRepository;

  @Mock
  RelationshipRepository relationshipRepository;

  @Mock
  TableRepository tableRepository;

  @InjectMocks
  ConfigService configService;

  @Before
  public void setUp() {
    when(configRepository.save(any(Config.class))).thenReturn(new Config());
    when(guestRepository.save(any(Guest.class))).thenReturn(new Guest());
    when(relationshipRepository.save(any(Relationship.class))).thenReturn(new Relationship());
    when(tableRepository.save(any(Table.class))).thenReturn(new Table());
  }

  @Test(expected = AppException.class)
  public void Given_configWithSelfRelationship_When_createConfig_Then_throwException() {
    List<GuestRequest> guestRequestList = new ArrayList<>();

    List<RelationshipRequest> relationshipRequestList = new ArrayList<>();
    relationshipRequestList.add(new RelationshipRequest("Guest 1", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 1", relationshipRequestList));

    List<RelationshipRequest> relationshipRequestList2 = new ArrayList<>();
    relationshipRequestList2.add(new RelationshipRequest("Guest 1", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 2", relationshipRequestList2));

    List<TableRequest> tableRequests = new ArrayList<>();
    tableRequests.add(new TableRequest("Circle", 2));

    NewConfigRequest newConfigRequest = new NewConfigRequest("Test", guestRequestList, tableRequests);

    configService.createConfig(newConfigRequest);
    verify(configRepository, never()).save(any(Config.class));
    verify(guestRepository, never()).save(any(Guest.class));
    verify(relationshipRepository, never()).save(any(Relationship.class));
    verify(tableRepository, never()).save(any(Table.class));
  }

  @Test(expected = AppException.class)
  public void Given_configWithDuplicateRelationship_When_createConfig_Then_throwException() {
    List<GuestRequest> guestRequestList = new ArrayList<>();

    List<RelationshipRequest> relationshipRequestList = new ArrayList<>();
    relationshipRequestList.add(new RelationshipRequest("Guest 2", 10, 10));
    relationshipRequestList.add(new RelationshipRequest("Guest 2", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 1", relationshipRequestList));

    List<RelationshipRequest> relationshipRequestList2 = new ArrayList<>();
    relationshipRequestList2.add(new RelationshipRequest("Guest 1", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 2", relationshipRequestList2));

    List<TableRequest> tableRequests = new ArrayList<>();
    tableRequests.add(new TableRequest("Circle", 2));

    NewConfigRequest newConfigRequest = new NewConfigRequest("Test", guestRequestList, tableRequests);

    configService.createConfig(newConfigRequest);
    verify(configRepository, never()).save(any(Config.class));
    verify(guestRepository, never()).save(any(Guest.class));
    verify(relationshipRepository, never()).save(any(Relationship.class));
    verify(tableRepository, never()).save(any(Table.class));
  }

  @Test(expected = AppException.class)
  public void Given_configInvalidTableCapacity_When_createConfig_Then_throwException() {
    List<GuestRequest> guestRequestList = new ArrayList<>();

    List<RelationshipRequest> relationshipRequestList = new ArrayList<>();
    relationshipRequestList.add(new RelationshipRequest("Guest 2", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 1", relationshipRequestList));

    List<RelationshipRequest> relationshipRequestList2 = new ArrayList<>();
    relationshipRequestList2.add(new RelationshipRequest("Guest 1", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 2", relationshipRequestList2));

    List<TableRequest> tableRequests = new ArrayList<>();
    tableRequests.add(new TableRequest("Circle", 3));

    NewConfigRequest newConfigRequest = new NewConfigRequest("Test", guestRequestList, tableRequests);

    configService.createConfig(newConfigRequest);
    verify(configRepository, never()).save(any(Config.class));
    verify(guestRepository, never()).save(any(Guest.class));
    verify(relationshipRepository, never()).save(any(Relationship.class));
    verify(tableRepository, never()).save(any(Table.class));
  }

  @Test(expected = AppException.class)
  public void Given_configInvalidTable_When_createConfig_Then_throwException() {
    List<GuestRequest> guestRequestList = new ArrayList<>();

    List<RelationshipRequest> relationshipRequestList = new ArrayList<>();
    relationshipRequestList.add(new RelationshipRequest("Guest 2", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 1", relationshipRequestList));

    List<RelationshipRequest> relationshipRequestList2 = new ArrayList<>();
    relationshipRequestList2.add(new RelationshipRequest("Guest 1", 10, 10));
    guestRequestList.add(new GuestRequest("Guest 2", relationshipRequestList2));

    List<RelationshipRequest> relationshipRequestList3 = new ArrayList<>();
    relationshipRequestList3.add(new RelationshipRequest("Guest 1", -1, 10));
    guestRequestList.add(new GuestRequest("Guest 3", relationshipRequestList3));

    List<TableRequest> tableRequests = new ArrayList<>();
    tableRequests.add(new TableRequest("Rectangle", 3));

    NewConfigRequest newConfigRequest = new NewConfigRequest("Test", guestRequestList, tableRequests);

    configService.createConfig(newConfigRequest);
    verify(configRepository, never()).save(any(Config.class));
    verify(guestRepository, never()).save(any(Guest.class));
    verify(relationshipRepository, never()).save(any(Relationship.class));
    verify(tableRepository, never()).save(any(Table.class));
  }
}
