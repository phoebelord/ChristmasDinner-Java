package com.phoebelord.model;

import java.io.Serializable;

public class Person implements Serializable {

  private int num;
  private String name;

  public Person(String name, int num) {
    this.num = num;
    this.name = name;
  }

  public Person() {
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "[num:" + num + ", name: " + name + "]";
  }
}
