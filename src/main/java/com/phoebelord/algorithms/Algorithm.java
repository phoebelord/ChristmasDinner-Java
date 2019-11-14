package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;

import java.util.List;

public abstract class Algorithm {

  public static int calculateHappiness(List<Person> people, List<Seat> seats, List<Table> tables) {
    int total = 0;
    for (int i = 0; i < people.size(); i++) {
      int personHappiness = 0;
      Person currentPerson = people.get(i);
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      Table table = tables.get(seats.get(i).getTableNum());
      for (int neighbouringSeat : neighbouringSeats) {
        Person neighbour = people.get(neighbouringSeat);
        int relationship =  currentPerson.getRelationshipWith(neighbour);
        personHappiness += (isNextTo(i, neighbouringSeat, table.getOffset(), table.getSize()) ? 2 * relationship : relationship);
      }
      total += personHappiness;
    }
    return total;
  }

  private static boolean isNextTo(int seatNum, int otherSeatNum, int offset, int tableSize) {
    boolean isPlusOne = mod((seatNum + 1) - offset, tableSize) + offset == otherSeatNum;
    boolean isMinusOne = mod((seatNum - 1) - offset, tableSize) + offset == otherSeatNum;
    return isPlusOne || isMinusOne;
  }

  // Modulo rather than remainder
  // e.g. -1 mod 10 = 9 (-1 % 10 = -1 in Java)
  private static int mod(int x, int y) {
    return (x % y + y) % y;
  }

  public abstract Solution calculateSolution();
}
