package com.phoebelord.payload;

import com.phoebelord.model.Config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConfigResponse {

  private int id;

  private String name;

  private List<GuestRequest> guests;

  private int numGuests;

  private List<TableRequest> tables;

  private int numTables;

  private String lastModified;

  public ConfigResponse(Config config) {
    this.id = config.getId();
    this.name = config.getName();
    this.guests = new ArrayList<>();
    this.numGuests = config.getGuests().size();
    this.tables = new ArrayList<>();
    this.numTables = config.getTables().size();
    this.lastModified = LocalDateTime.ofInstant(config.getUpdatedAt(), ZoneId.systemDefault()).toString();
  }

  public ConfigResponse() {
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

  public List<GuestRequest> getGuests() {
    return guests;
  }

  public void setGuests(List<GuestRequest> guests) {
    this.guests = guests;
  }

  public int getNumGuests() {
    return numGuests;
  }

  public void setNumGuests(int numGuests) {
    this.numGuests = numGuests;
  }

  public List<TableRequest> getTables() {
    return tables;
  }

  public void setTables(List<TableRequest> tables) {
    this.tables = tables;
  }

  public int getNumTables() {
    return numTables;
  }

  public void setNumTables(int numTables) {
    this.numTables = numTables;
  }

  public String getLastModified() {
    return lastModified;
  }

  public void setLastModified(String lastModified) {
    this.lastModified = lastModified;
  }
}
