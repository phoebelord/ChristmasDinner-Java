package com.phoebelord.payload;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewConfigRequest {

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
