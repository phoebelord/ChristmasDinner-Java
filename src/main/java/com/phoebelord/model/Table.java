package com.phoebelord.model;

import com.phoebelord.payload.TableRequest;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@javax.persistence.Table(name = "guestTable")
public class Table {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @NotEmpty
  private String shape;
  private int tableNum;
  private int offset;

  @NotNull
  @Range(min = 2, max = 20)
  private int capacity;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name="config_id", nullable = false)
  private Config config;

  public Table(String shape, int tableNum, int offset, int capacity) {
    this.shape = shape;
    this.tableNum = tableNum;
    this.offset = offset;
    this.capacity = capacity;
  }

  public Table() {
  }

  public Table(TableRequest tableRequest) {
    this.shape = tableRequest.getShape();
    this.capacity = tableRequest.getCapacity();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getShape() {
    return shape;
  }

  public void setShape(String shape) {
    this.shape = shape;
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

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return "[table: " + tableNum + ", offset: " + offset + ", size: " + capacity + "]";
  }
}
