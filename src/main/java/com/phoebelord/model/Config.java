package com.phoebelord.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Config {

  @Id
  @GeneratedValue
  private int id;

  @ManyToOne
  private User user;

  @OneToMany
  private List<Guest> guests;

  @OneToMany
  private List<Table> tables;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Guest> getGuests() {
    return guests;
  }

  public void setGuests(List<Guest> guests) {
    this.guests = guests;
  }

  public List<Table> getTables() {
    return tables;
  }

  public void setTables(List<Table> tables) {
    this.tables = tables;
  }
}
