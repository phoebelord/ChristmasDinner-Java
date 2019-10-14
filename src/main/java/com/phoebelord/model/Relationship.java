package com.phoebelord.model;

public class Relationship {

  private Person target;
  private int weight;

  public Relationship(Person target, int weight) {
    this.target = target;
    this.weight = weight;
  }

  public Person getTarget() {
    return target;
  }

  public void setTarget(Person target) {
    this.target = target;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }
}
