package com.phoebelord.payload;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewConfigRequest {

  @NotBlank
  String name;

  List<? extends GuestRequest> guests;

  List<? extends TableRequest> tables;

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
