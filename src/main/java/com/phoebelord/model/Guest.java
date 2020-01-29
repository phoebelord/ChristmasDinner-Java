package com.phoebelord.model;

import java.io.Serializable;
import java.util.List;

public class Guest implements Serializable {

  private int id;
  private String name;
  private List<Relationship> relationships;

  public Guest(String name, int id) {
    this.id = id;
    this.name = name;
  }

  public Guest() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<Relationship> relationships) {
    this.relationships = relationships;
  }

  public int getRelationshipWith(Guest guest) {
    int value = 0;
    for (Relationship relationship : relationships) {
      if (relationship.getGuestId() == guest.getId()) {
        value = relationship.getLikability();
        break;
      }
    }
    return value;
  }

  @Override
  public String toString() {
    return "[id:" + id + ", name: " + name + "]";
  }
}
