package com.phoebelord.model;

import java.util.ArrayList;
import java.util.List;

public class ArrangementGuest {
  String name;
  int happiness;
  List<String> relationships;

  public ArrangementGuest(Guest guest, List<Guest> guests, String shape) {
    this.name = guest.getName();
    this.relationships = new ArrayList<>();
    this.happiness = getHappiness(guest, guests, shape);
  }

  private int getHappiness(Guest guest, List<Guest> guests, String shape) {
    int happiness = 0;
    int guestIndex = guests.indexOf(guest);

    Guest leftGuest = guests.get(mod((guestIndex - 1), guests.size()));
    int relationship = guest.getRelationshipWith(leftGuest);
    happiness += 2 * relationship;
    relationships.add(leftGuest.getName() + ": " + RelationshipName.valueOfLikability(relationship).label);

    if(shape.equals("Rectangle")) {
      int peoplePerSide = (guests.size() - 2) / 2;
      if((guestIndex != guests.size() - 1) && (guestIndex != peoplePerSide)) {
        Guest acrossGuest = guests.get(-guestIndex + 2 * (peoplePerSide));
        relationship = guest.getRelationshipWith(acrossGuest);
        happiness += relationship;
        relationships.add(acrossGuest.getName() + ": " + RelationshipName.valueOfLikability(relationship).label);
      }
    }

    Guest rightGuest = guests.get(mod((guestIndex + 1), guests.size()));
    relationship =  guest.getRelationshipWith(rightGuest);
    happiness += 2 * relationship;
    relationships.add(rightGuest.getName() + ": " + RelationshipName.valueOfLikability(relationship).label);

    return happiness;
  }

  private int mod(int x, int y) {
    return (x % y + y) % y;
  }

  public String getName() {
    return name;
  }

  public int getHappiness() {
    return happiness;
  }

  public List<String> getRelationships() {
    return relationships;
  }
}
