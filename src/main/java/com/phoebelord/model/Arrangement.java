package com.phoebelord.model;

import java.util.List;
import java.util.stream.Collectors;

public class Arrangement {
  private String shape;
  private int happiness;
  private List<ArrangementGuest> guests;

  public Arrangement(String shape, List<Guest> guests, MaximisationType maximisationType) {
    this.shape = shape;
    this.guests = guests.stream().map(guest -> new ArrangementGuest(guest, guests, shape, maximisationType)).collect(Collectors.toList());
    this.happiness = this.guests.stream().mapToInt(ArrangementGuest::getHappiness).sum();
  }

  public String getShape() {
    return shape;
  }

  public List<ArrangementGuest> getGuests() {
    return guests;
  }

  public int getHappiness() {
    return happiness;
  }

  @Override
  public String toString() {
    return "[Shape: " + shape + ", guests: " + guests.stream().map(ArrangementGuest::getName).toString() + "]";
  }
}
