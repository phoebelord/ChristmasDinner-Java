package com.phoebelord.algorithms;

import com.phoebelord.algorithms.genetic.crossover.Crossover;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.Selection;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class Algorithm {

  List<Seat> seats;
  List<Guest> guests;
  List<Table> tables;
  MaximisationType maximisationType;
  SelectionType selection;
  CrossoverType crossover;


  //doesn't need to be static???
  public static int calculateHappiness(List<Guest> guests, List<Seat> seats, List<Table> tables, MaximisationType maximisationType) {
    if(maximisationType == MaximisationType.HAPPINESS) {
      return calculateHappiness(guests, seats, tables);
    } else {
      return calculateProfit(guests, seats);
    }
  }

  private static int calculateHappiness(List<Guest> guests, List<Seat> seats, List<Table> tables) {
    int total = 0;
    for (int i = 0; i < guests.size(); i++) {
      int guestHappiness = 0;
      Guest currentGuest = guests.get(i);
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      Table table = tables.get(seats.get(i).getTableNum());
      for (int neighbouringSeat : neighbouringSeats) {
        Guest neighbour = guests.get(neighbouringSeat);
        int relationship =  currentGuest.getRelationshipWith(neighbour, MaximisationType.HAPPINESS);
        guestHappiness += (isNextTo(i, neighbouringSeat, table.getOffset(), table.getCapacity()) ? 2 * relationship : relationship);
      }
      total += guestHappiness;
    }
    return total;
  }

  private static  int calculateProfit(List<Guest> guests, List<Seat> seats) {
    int total = 0;
    for(int i = 0; i < guests.size(); i++) {
      Guest currentGuest = guests.get(i);
      int guestProfit = 0;
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      for(Relationship relationship: currentGuest.getRelationships()) {
        int otherGuestId = relationship.getGuestId();
        boolean isNextTo = neighbouringSeats.stream().anyMatch(neighbouringSeat -> guests.get(neighbouringSeat).getId() == otherGuestId);
        if(isNextTo && relationship.getLikability() != RelationshipName.DISLIKES.likability) {
          guestProfit += relationship.getBribe();
        } else if(!isNextTo && relationship.getLikability() == RelationshipName.DISLIKES.likability){
          guestProfit += relationship.getBribe();
        }
      }
      total += guestProfit;
    }

    return  total;
  }

  public static boolean isNextTo(int seatNum, int otherSeatNum, int offset, int tableSize) {
    boolean isPlusOne = mod((seatNum + 1) - offset, tableSize) + offset == otherSeatNum;
    boolean isMinusOne = mod((seatNum - 1) - offset, tableSize) + offset == otherSeatNum;
    return isPlusOne || isMinusOne;
  }

  BigDecimal factorial(int n) {
    if(n == 0) {
      return BigDecimal.valueOf(1);
    } else {
      return BigDecimal.valueOf(n) .multiply(factorial(n - 1));
    }
  }

  float factoriall(int n) {
    if(n == 0) {
      return 1;
    } else {
      return n * (factoriall(n - 1));
    }
  }

  // Modulo rather than remainder
  // e.g. -1 mod 10 = 9 (-1 % 10 = -1 in Java)
  private static int mod(int x, int y) {
    return (x % y + y) % y;
  }

  public abstract Solution[] calculateSolution();

  public void setGuests(List<Guest> guests) {
    this.guests = guests;
  }

  public void setTablesAndSeats(List<Table> tables) {
    this.tables = tables;
    this.seats = deriveSeats();
  }

  public void setMaximisationType(MaximisationType maximisationType) {
    this.maximisationType = maximisationType;
  }

  public List<Seat> deriveSeats() {
    seats = new ArrayList<>();
    for(Table table : tables) {
      for(int i = 0; i < table.getCapacity(); i++) {
        Seat currentSeat = new Seat();
        currentSeat.setTableNum(table.getTableNum());
        currentSeat.setSeatNum(i + table.getOffset());
        currentSeat.setNeighbours(getNeighbouringSeats(table, i));
        seats.add(currentSeat);
      }
    }
    return seats;
  }

  private List<Integer> getNeighbouringSeats(Table table, int i) {
    List<Integer> neighbours = new ArrayList<>();
    // each seat is next to the 2 either side
    neighbours.add(mod((i + 1), table.getCapacity()) + table.getOffset());
    neighbours.add(mod((i - 1), table.getCapacity()) + table.getOffset());
    if(table.getShape().equals("Rectangle")) {
      //also across from someone
      int peoplePerSide = (table.getCapacity() - 2) / 2;
      if((i != table.getCapacity() - 1) && (i != peoplePerSide)) {
        int neighbour = (-i + 2 * (peoplePerSide));
        neighbours.add(neighbour + table.getOffset());
      }
    }
    return neighbours;
  }

  public void setSelection(SelectionType selection) {
    this.selection = selection;
  }

  public void setCrossover(CrossoverType crossover) {
    this.crossover = crossover;
  }
}
