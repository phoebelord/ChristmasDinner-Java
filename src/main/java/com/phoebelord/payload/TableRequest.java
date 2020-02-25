package com.phoebelord.payload;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TableRequest {

  @NotEmpty
  private String shape;

  @NotNull
  @Range(min = 2, max = 20)
  private int capacity;

  public String getShape() {
    return shape;
  }

  public void setShape(String shape) {
    this.shape = shape;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
