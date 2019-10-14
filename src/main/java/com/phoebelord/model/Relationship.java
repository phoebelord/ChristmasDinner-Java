package com.phoebelord.model;

import java.io.Serializable;

public class Relationship implements Serializable {

  private int id;
  private int value;

  public Relationship(int target, int value) {
    this.id = target;
    this.value = value;
  }

  public Relationship() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
