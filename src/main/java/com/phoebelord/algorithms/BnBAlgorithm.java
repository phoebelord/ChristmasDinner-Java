package com.phoebelord.algorithms;

import com.phoebelord.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class BnBAlgorithm extends Algorithm {

  private final int PARTNER = 1;
  private final int LIKES = 10;
  private final int NEUTRAL = 11;
  private final int DISLIKES = 12;

  private int bestSoFar;
  private List<Integer> bestSolution;

  private BigDecimal counter;
  private BigDecimal pruned;
  private BigDecimal lastCount;
  private BigDecimal noSolutions;

  @Override
  public Solution[] calculateSolution() {
    bestSoFar = Integer.MAX_VALUE;
    initialiseCounters();
    for (int i = 0; i < guests.size(); i++) {
      List<Integer> partialSolution = new ArrayList<>();
      partialSolution.add(i);
      bnb(1, partialSolution, 0);
    }
    List<Guest> guestList = new ArrayList<>();
    for (int guestIndex : bestSolution) {
      guestList.add(guests.get(guestIndex));
    }
    System.out.println("Pruned: " + pruned);
    System.out.println("Counter: " + counter);
    return new Solution[]{new Solution(tables, guestList, calculateHappiness(guestList, seats, tables, MaximisationType.HAPPINESS))};
  }

  private void bnb(int level, List<Integer> partialSolution, int currentHappiness) {
    if (level == guests.size()) {
      counter = counter.add(BigDecimal.ONE);
      if (currentHappiness < bestSoFar) {
        bestSoFar = currentHappiness;
        bestSolution = new ArrayList<>(partialSolution);
      }
    } else {
      for (int i = 0; i < guests.size(); i++) {
        if (!partialSolution.contains(i)) {
          List<Integer> newList = new ArrayList<>(partialSolution);
          newList.add(i);
          int newHappiness = recalculateHappiness(newList, currentHappiness);
          int lowerBound = estimateHappiness(newList, newHappiness);
          if (lowerBound < bestSoFar) {
            bnb(level + 1, newList, newHappiness);
          } else {
            counter = counter.add(factorial(guests.size() - (level + 1)));
            pruned = pruned.add(BigDecimal.ONE);
          }
        }
      }
    }

    if(calculatePercentageOfSolutions(counter).subtract(calculatePercentageOfSolutions(lastCount)).compareTo(BigDecimal.TEN) != -1) {
      System.out.printf("%s\n", calculatePercentageOfSolutions(counter).toString());
      lastCount = counter;
    }
  }

  private int recalculateHappiness(List<Integer> partialSolution, int currentHappiness) {
    int currentGuestIndex = partialSolution.get(partialSolution.size() - 1);
    Guest currentGuest = guests.get(currentGuestIndex);
    Seat seat = seats.get(partialSolution.size() - 1);
    List<Integer> neighbouringSeats = seat.getNeighbours();
    Table table = tables.get(seat.getTableNum());
    for(int neighbouringSeat : neighbouringSeats) {
      if(neighbouringSeat < partialSolution.size()) {
        Guest neighbour = guests.get(partialSolution.get(neighbouringSeat));
        boolean isNextTo = isNextTo(seat.getSeatNum(), neighbouringSeat, table.getOffset(), table.getCapacity());
        int multiplier = (isNextTo) ? 2 : 1;
        currentHappiness += multiplier * getMinimisingRelationship(currentGuest.getRelationshipWith(neighbour));
        currentHappiness += multiplier * getMinimisingRelationship(neighbour.getRelationshipWith(currentGuest));
      }
    }
    return currentHappiness;
  }

  // TODO this is a mess
  // does it even work lolllllllll
  private int estimateHappiness(List<Integer> partialSolution, int currentHappiness) {
    int estimate = 0;
    for (int i = 0; i < guests.size(); i++) {
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      Table table = tables.get(seats.get(i).getTableNum());
      boolean hasPartner = false;
      if (i < partialSolution.size()) {
        if(neighbouringSeats.stream().allMatch(s -> s < partialSolution.size())) {
          continue;
        }
        int guestIndex = partialSolution.get(i);
        Guest currentGuest = guests.get(guestIndex);
        for (int neighbouringSeat : neighbouringSeats) {
          if (neighbouringSeat < partialSolution.size() && !hasPartner) {
            Guest neighbour = guests.get(partialSolution.get(neighbouringSeat));
            if (getMinimisingRelationship(currentGuest.getRelationshipWith(neighbour)) == PARTNER) {
              hasPartner = true;
            }
          } else if(neighbouringSeat >= partialSolution.size()) {
            // get best possible approximation - can have at most one partner
            int guess;
            boolean isNextTo = isNextTo(i, neighbouringSeat, table.getOffset(), table.getCapacity());
            if (!hasPartner && isNextTo) {
              guess = PARTNER;
              hasPartner = true;
            } else if(!hasPartner && i == guests.size() - 1) {
              guess = PARTNER;
              hasPartner = true;
            } else {
              guess = LIKES;
            }
            estimate += isNextTo ? 2 * guess : guess;
          }
        }
      } else {
        // at best a guest will be sat next to their +1 and guests they like ....
        int finalI = i;
        int countOfNextTo = (int) neighbouringSeats.stream().filter(neighbouringSeat -> isNextTo(finalI, neighbouringSeat, table.getOffset(), table.getCapacity())).count();
        if (countOfNextTo >= 1) {
          estimate += (2 * PARTNER) + (2 * LIKES * (countOfNextTo - 1)) + (LIKES * (neighbouringSeats.size() - countOfNextTo));
        } else {
          System.out.println("error");
        }
      }
    }
    return currentHappiness + estimate;
  }

  private int getMinimisingRelationship(int relationship) {
    switch (relationship) {
      case -1:
        return DISLIKES;
      case 1:
        return LIKES;
      case 10:
        return PARTNER;
      case 0:
      default:
        return NEUTRAL;
    }
  }

  private BigDecimal calculatePercentageOfSolutions(BigDecimal x) {
    return (x.divide(noSolutions, 3, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
  }

  @Override
  public void setTablesAndSeats(List<Table> tables) {
    super.setTablesAndSeats(tables);
    this.noSolutions = factorial(seats.size());
  }

  private void initialiseCounters() {
    counter = BigDecimal.ZERO;
    pruned = BigDecimal.ZERO;
    lastCount = BigDecimal.ZERO;
  }
}
