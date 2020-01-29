package com.phoebelord.model;

import java.util.List;

public class Arrangement {
  private String shape;
  private List<String> names;

  public Arrangement(String shape, List<String> names) {
    this.shape = shape;
    this.names = names;
  }

  public String getShape() {
    return shape;
  }

  public List<String> getNames() {
    return names;
  }

  @Override
  public String toString() {
    return "[Shape: " + shape + ", guests: " + names + "]";
  }
}
