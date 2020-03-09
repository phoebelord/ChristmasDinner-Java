package com.phoebelord.payload;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewConfigRequest {

  @NotBlank
  String name;

  List<? extends GuestRequest> guests;

  List<? extends TableRequest> tables;

  public NewConfigRequest(@NotBlank String name, List<? extends GuestRequest> guests, List<? extends TableRequest> tables) {
    this.name = name;
    this.guests = guests;
    this.tables = tables;
  }

  public NewConfigRequest() {
  }

  public String getName() {
    return name;
  }

  public List<? extends GuestRequest> getGuests() {
    return guests;
  }

  public List<? extends TableRequest> getTables() {
    return tables;
  }
}
