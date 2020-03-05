package com.phoebelord.model;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"guestId", "owner_id"})})
public class Relationship implements Serializable {

  @ManyToOne(optional = false)
  private Guest owner;

  @Id
  @GeneratedValue
  private int id;

  @NotNull
  private int guestId;

  private int likability;

  private int bribe;

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

  public int getBribe() {
    return bribe;
  }

  public void setBribe(int bribe) {
    this.bribe = bribe;
  }

  public Guest getRelationshipOwner() {
    return owner;
  }

  public void setOwner(Guest owner) {
    this.owner = owner;
  }
}
