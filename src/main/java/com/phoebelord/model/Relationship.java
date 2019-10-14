package com.phoebelord.model;

import java.io.Serializable;

public class Relationship implements Serializable {

  private int personId;
  private int likability;

  public Relationship(int target, int likability) {
    this.personId = target;
    this.likability = likability;
  }

  public Relationship() {
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public int getLikability() {
    return likability;
  }

  public void setLikability(int likability) {
    this.likability = likability;
  }
}
