package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

public abstract class Algorithm {

  public abstract Solution calculateSolution(Person[] people, Seat[] seats);

  int calculateHappiness(Person[] people, Seat[] seats) {
    //2 x for next to
    int total = 0;
    for(int i = 0 ; i < 10; i++) {
      int personHappiness = 0;
      Person currentPerson = people[i];
      int[] neighbouringSeats = seats[i].getNeighbours();
      for(int neighbouringSeat: neighbouringSeats) {
        Person neighbour = people[neighbouringSeat];
        personHappiness += currentPerson.getRelationshipWith(neighbour);
      }
      total += personHappiness;
    }
    return total;
  }
}
