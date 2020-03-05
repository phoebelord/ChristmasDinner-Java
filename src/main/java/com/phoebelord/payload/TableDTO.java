package com.phoebelord.payload;

public class TableDTO extends TableRequest{

  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setShape(String shape) {
    this.shape = shape;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
