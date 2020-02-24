package com.phoebelord.payload;

import com.phoebelord.model.Config;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConfigResponse {

  private int id;

  private String name;

  private int numGuests;

  private int numTables;

  private String lastModified;

  public ConfigResponse(Config config) {
    this.id = config.getId();
    this.name = config.getName();
    this.numGuests = config.getGuests().size();
    this.numTables = config.getTables().size();
    this.lastModified = LocalDateTime.ofInstant(config.getUpdatedAt(), ZoneId.systemDefault()).toString();
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

  public int getNumGuests() {
    return numGuests;
  }

  public void setNumGuests(int numGuests) {
    this.numGuests = numGuests;
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
