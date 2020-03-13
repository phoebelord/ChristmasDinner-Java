package com.phoebelord.model;

import com.phoebelord.model.audit.UserDateAudit;
import com.phoebelord.payload.NewConfigRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Config extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @NotBlank(message = "Please provide a name for your config")
  private String name;

  @OneToMany
  @OrderBy("id asc")
  private List<Guest> guests;

  @OneToMany
  @OrderBy("id asc")
  private List<Table> tables;

  public Config() {
  }

  public Config(NewConfigRequest newConfigRequest) {
    this.name = newConfigRequest.getName();
    this.guests = new ArrayList<>();
    this.tables = new ArrayList<>();
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Guest> getGuests() {
    return guests;
  }

  public void setGuests(List<Guest> guests) {
    this.guests = guests;
  }

  public List<Table> getTables() {
    return tables;
  }

  public void setTables(List<Table> tables) {
    this.tables = tables;
  }

  public void addGuest(Guest guest) {
    guest.setConfig(this);
    guests.add(guest);
  }

  public void addTable(Table table) {
    table.setConfig(this);
    tables.add(table);
  }
}
