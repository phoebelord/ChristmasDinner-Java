package com.phoebelord.model;

import java.util.Arrays;

public class Seat {

  private int tableNum;
  private int seatNum;
  private int[] neighbours;

  public Seat(int tableNum, int seatNum) {
    this.tableNum = tableNum;
    this.seatNum = seatNum;
  }

  public Seat() {

  }

  public int getTableNum() {
    return tableNum;
  }

  public void setTableNum(int tableNum) {
    this.tableNum = tableNum;
  }

  public int getSeatNum() {
    return seatNum;
  }

  public void setSeatNum(int seatNum) {
    this.seatNum = seatNum;
  }

  public int[] getNeighbours() {
    return neighbours;
  }

  public void setNeighbours(int[] neighbours) {
    this.neighbours = neighbours;
  }

  @Override
  public String toString() {
    return "[table: " + tableNum + ", seat: " + seatNum + ", neighbours: " + Arrays.toString(neighbours) + "]";
  }
}
