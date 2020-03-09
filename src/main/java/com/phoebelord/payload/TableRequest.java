package com.phoebelord.payload;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TableRequest {

  @NotEmpty
  String shape;

  @NotNull
  @Range(min = 2, max = 20)
  int capacity;

  public TableRequest(@NotEmpty String shape, @NotNull @Range(min = 2, max = 20) int capacity) {
    this.shape = shape;
    this.capacity = capacity;
  }

  public TableRequest() {
  }

  public String getShape() {
    return shape;
  }

  public int getCapacity() {
    return capacity;
  }

}
