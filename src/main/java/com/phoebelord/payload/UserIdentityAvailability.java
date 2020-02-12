package com.phoebelord.payload;

public class UserIdentityAvailability {
  private boolean available;

  public UserIdentityAvailability(boolean available) {
    this.available = available;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
