package com.phoebelord.model;

import java.util.ArrayList;
import java.util.List;

public class ArrangementGuest {
  String name;
  int happiness;
  List<String> relationships;

  public ArrangementGuest(Guest guest, List<Guest> guests, String shape, MaximisationType maximisationType) {
    this.name = guest.getName();
    this.relationships = new ArrayList<>();
    this.happiness = getHappiness(guest, guests, shape, maximisationType);
  }

  private int getHappiness(Guest guest, List<Guest> guests, String shape, MaximisationType maximisationType) {
    if(maximisationType == MaximisationType.HAPPINESS) {
      return getHappiness(guest, guests, shape);
    } else {
      return getProfit(guest, guests, shape);
    }
  }

  // TODO could do with a tidy
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

  // TODO could do with a tidy
  private int getProfit(Guest guest, List<Guest> guests, String shape) {
    int profit = 0;
    int guestIndex = guests.indexOf(guest);
    List<Guest> neighbours = new ArrayList<>();
    Guest leftGuest = guests.get(mod((guestIndex - 1), guests.size()));
    neighbours.add(leftGuest);
    int relationship = guest.getRelationshipWith(leftGuest);
    if(relationship != RelationshipName.DISLIKES.likability) {
      int bribe = guest.getRelationshipWith(leftGuest, MaximisationType.PROFIT);
      profit += bribe;
      if(bribe != 0) {
        relationships.add(leftGuest.getName() + ": £" + bribe);
      }
    }

    if(shape.endsWith("Rectangle")) {
      int peoplePerSide = (guests.size() - 2) / 2;
      if((guestIndex != guests.size() - 1) && (guestIndex != peoplePerSide)) {
        Guest acrossGuest = guests.get(-guestIndex + 2 * (peoplePerSide));
        neighbours.add(acrossGuest);
        relationship = guest.getRelationshipWith(acrossGuest);
        if(relationship != RelationshipName.DISLIKES.likability) {
          int bribe = guest.getRelationshipWith(acrossGuest, MaximisationType.PROFIT);
          profit += bribe;
          if(bribe != 0) {
            relationships.add(acrossGuest.getName() + ": £" + bribe);
          }
        }
      }
    }

    Guest rightGuest = guests.get(mod((guestIndex + 1), guests.size()));
    neighbours.add(rightGuest);
    relationship = guest.getRelationshipWith(rightGuest);
    if(relationship != RelationshipName.DISLIKES.likability) {
      int bribe = guest.getRelationshipWith(rightGuest, MaximisationType.PROFIT);
      profit += bribe;
      if(bribe != 0) {
        relationships.add(rightGuest.getName() + ": £" + bribe);
      }
    }

    for(Relationship rel : guest.getRelationships()) {
      if(rel.getLikability() == RelationshipName.DISLIKES.likability) {
        if(neighbours.stream().noneMatch(neighbour -> neighbour.getId() == rel.getGuestId())) {
          profit += rel.getBribe();
          if(rel.getBribe() != 0) {
            relationships.add(guests.stream().filter(guest1 -> guest1.getId() == rel.getGuestId()).findFirst().get().getName() + ": £" + rel.getBribe());
          }
        }
      }
    }

    return profit;
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
