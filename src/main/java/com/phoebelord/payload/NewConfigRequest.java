package com.phoebelord.payload;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewConfigRequest {

  private int id;

  @NotBlank
  private String name;

  private List<GuestRequest> guests;

  private List<TableRequest> tables;

  public int getId() {
    return id;
  }

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
