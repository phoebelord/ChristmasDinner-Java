package com.phoebelord.model;

public class Table {

  private String shape;
  private int tableNum;
  private int offset;
  private int size;

  public Table(String shape, int tableNum, int offset, int size) {
    this.shape = shape;
    this.tableNum = tableNum;
    this.offset = offset;
    this.size = size;
  }

  public Table() {
  }

  public String getShape() {
    return shape;
  }

  public int getTableNum() {
    return tableNum;
  }

  public void setTableNum(int tableNum) {
    this.tableNum = tableNum;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "[table: " + tableNum + ", offset: " + offset + ", size: " + size + "]";
  }
}
