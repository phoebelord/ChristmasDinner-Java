package com.phoebelord.model;

public class Table {

  private int tableNum;
  private int offset;
  private int size;

  public Table(int tableNum, int offset, int size) {
    this.tableNum = tableNum;
    this.offset = offset;
    this.size = size;
  }

  public Table() {
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
