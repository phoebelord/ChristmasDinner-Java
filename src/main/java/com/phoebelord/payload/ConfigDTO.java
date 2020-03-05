package com.phoebelord.payload;

import com.phoebelord.model.Config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfigDTO extends NewConfigRequest{

  private int id;

  List<GuestDTO> guests;

  private int numGuests;

  List<TableDTO> tables;

  private int numTables;

  private String lastModified;

  public ConfigDTO(Config config) {
    this.id = config.getId();
    this.name = config.getName();
    this.guests = new ArrayList<>();
    this.numGuests = config.getGuests().size();
    this.tables = new ArrayList<>();
    this.numTables = config.getTables().size();
    this.lastModified = DateTimeFormatter
      .ofLocalizedDateTime(FormatStyle.SHORT)
      .withLocale(Locale.UK)
      .withZone(ZoneId.systemDefault())
      .format(config.getUpdatedAt());
  }

  public ConfigDTO() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<GuestDTO> getGuests() {
    return guests;
  }

  public void setGuests(List<GuestDTO> guests) {
    this.guests = guests;
  }

  public int getNumGuests() {
    return numGuests;
  }

  public void setNumGuests(int numGuests) {
    this.numGuests = numGuests;
  }

  public List<TableDTO> getTables() {
    return tables;
  }

  public void setTables(List<TableDTO> tables) {
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
