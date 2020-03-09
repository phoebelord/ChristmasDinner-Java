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

  public RelationshipRequest(@NotBlank String guestName, @NotNull int likability, @NotNull int bribe) {
    this.guestName = guestName;
    this.likability = likability;
    this.bribe = bribe;
  }

  public RelationshipRequest() {
  }

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
