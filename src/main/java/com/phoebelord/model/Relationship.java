package com.phoebelord.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Relationship implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @NotNull
  private int guestId;

  private int likability;

  public Relationship(int target, int likability) {
    this.guestId = target;
    this.likability = likability;
  }

  public Relationship() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
