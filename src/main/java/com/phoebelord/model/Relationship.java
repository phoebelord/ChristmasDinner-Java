package com.phoebelord.model;

import java.io.Serializable;

public class Relationship implements Serializable {

  private int guestId;
  private int likability;

  public Relationship(int target, int likability) {
    this.guestId = target;
    this.likability = likability;
  }

  public Relationship() {
  }

  public int getGuestId() {
    return guestId;
  }

  public void setGuestId(int guestId) {
    this.guestId = guestId;
  }

  public int getLikability() {
    return likability;
  }

  public void setLikability(int likability) {
    this.likability = likability;
  }
}
