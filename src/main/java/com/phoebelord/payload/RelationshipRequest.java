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

  public void setGuestName(String guestName) {
    this.guestName = guestName;
  }

  public int getLikability() {
    return likability;
  }

  public void setLikability(int likability) {
    this.likability = likability;
  }

  public int getBribe() {
    return bribe;
  }

  public void setBribe(int bribe) {
    this.bribe = bribe;
  }
}
