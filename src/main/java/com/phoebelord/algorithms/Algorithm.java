package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.util.List;

public abstract class Algorithm {

  static int calculateHappiness(List<Person> people, List<Seat> seats) {
    //2 x for next to
    int total = 0;
    for (int i = 0; i < 10; i++) {
      int personHappiness = 0;
      Person currentPerson = people.get(i);
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      for (int neighbouringSeat : neighbouringSeats) {
        Person neighbour = people.get(neighbouringSeat);
        personHappiness += currentPerson.getRelationshipWith(neighbour);
      }
      total += personHappiness;
    }
    return total;
  }

  public abstract Solution calculateSolution();
}
