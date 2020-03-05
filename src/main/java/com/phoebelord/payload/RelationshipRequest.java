package com.phoebelord.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RelationshipRequest {

  @NotBlank
  String guestName;

  @NotNull
  int likability;

  @NotNull
  int bribe;

  public String getGuestName() {
    return guestName;
  }

  public int getLikability() {
    return likability;
  }

  public int getBribe() {
    return bribe;
  }
}
