package com.phoebelord.model;

import java.io.Serializable;

public class Person implements Serializable {

  private int id;
  private String name;
  private Relationship[] relationships;

  public Person(String name, int id) {
    this.id = id;
    this.name = name;
  }

  public Person() {
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

  public Relationship[] getRelationships() {
    return relationships;
  }

  public void setRelationships(Relationship[] relationships) {
    this.relationships = relationships;
  }

  public int getRelationshipWith(Person person) {
    int value = 0;
    for(Relationship relationship: relationships) {
      if(relationship.getId() == person.getId()) {
        value = relationship.getValue();
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
