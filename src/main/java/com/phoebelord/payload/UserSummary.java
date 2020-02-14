package com.phoebelord.payload;

public class UserSummary {
  private int id;
  private String email;

  public UserSummary(int id, String email) {
    this.id = id;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
}
