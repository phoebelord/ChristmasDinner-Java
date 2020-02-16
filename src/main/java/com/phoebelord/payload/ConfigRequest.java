package com.phoebelord.payload;

import com.phoebelord.model.Table;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ConfigRequest {

  @NotBlank
  private String name;

  private List<GuestRequest> guests;

  private List<TableRequest> tables;

  public String getName() {
    return name;
  }

  public List<GuestRequest> getGuests() {
    return guests;
  }

  public List<TableRequest> getTables() {
    return tables;
  }
}