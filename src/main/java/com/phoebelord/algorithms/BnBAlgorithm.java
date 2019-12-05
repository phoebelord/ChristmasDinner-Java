package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BnBAlgorithm extends Algorithm {

  private final List<Seat> SEATS;
  private final List<Person> PEOPLE;
  private final List<Table> TABLES;

  private final int PARTNER = 1;
  private final int LIKES = 10;
  private final int NEUTRAL = 11;
  private final int DISLIKES = 12;

  private int bestSoFar;
  private List<Integer> bestSolution;

  private BigDecimal counter = BigDecimal.ZERO;
  private BigDecimal pruned = BigDecimal.ZERO;
  private BigDecimal lastCount = BigDecimal.ZERO;
  private BigDecimal noSolutions;

  BnBAlgorithm(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.PEOPLE = people;
    this.SEATS = seats;
    this.TABLES = tables;
    noSolutions = factorial(seats.size());
  }


  @Override
  public Solution calculateSolution() {
    bestSoFar = Integer.MAX_VALUE;
    for (int i = 0; i < PEOPLE.size(); i++) {
      List<Integer> partialSolution = new ArrayList<>();
      partialSolution.add(i);
      bnb(1, partialSolution, 0);
    }
    List<Person> personList = new ArrayList<>();
    for (int personIndex : bestSolution) {
      personList.add(PEOPLE.get(personIndex));
    }
    System.out.println("Pruned: " + pruned);
    System.out.println("Counter: " + counter);
    return new Solution(personList, Algorithm.calculateHappiness(personList, SEATS, TABLES));
  }

  private void bnb(int level, List<Integer> partialSolution, int currentHappiness) {
    if (level == PEOPLE.size()) {
      counter = counter.add(BigDecimal.ONE);
      if (currentHappiness < bestSoFar) {
        bestSoFar = currentHappiness;
        bestSolution = new ArrayList<>(partialSolution);
      }
    } else {
      for (int i = 0; i < PEOPLE.size(); i++) {
        if (!partialSolution.contains(i)) {
          List<Integer> newList = new ArrayList<>(partialSolution);
          newList.add(i);
          int newHappiness = recalculateHappiness(newList, currentHappiness);
          int lowerBound = estimateHappiness(newList, newHappiness);
          if (lowerBound < bestSoFar) {
            bnb(level + 1, newList, newHappiness);
          } else {
            counter = counter.add(factorial(PEOPLE.size() - (level + 1)));
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
    int currentPersonIndex = partialSolution.get(partialSolution.size() - 1);
    Person currentPerson = PEOPLE.get(currentPersonIndex);
    Seat seat = SEATS.get(partialSolution.size() - 1);
    List<Integer> neighbouringSeats = seat.getNeighbours();
    Table table = TABLES.get(seat.getTableNum());
    for(int neighbouringSeat : neighbouringSeats) {
      if(neighbouringSeat < partialSolution.size()) {
        Person neighbour = PEOPLE.get(partialSolution.get(neighbouringSeat));
        boolean isNextTo = isNextTo(seat.getSeatNum(), neighbouringSeat, table.getOffset(), table.getSize());
        int multiplier = (isNextTo) ? 2 : 1;
        currentHappiness += multiplier * getMinimisingRelationship(currentPerson.getRelationshipWith(neighbour));
        currentHappiness += multiplier * getMinimisingRelationship(neighbour.getRelationshipWith(currentPerson));
      }
    }
    return currentHappiness;
  }

  // TODO this is a mess
  // does it even work lolllllllll
  private int estimateHappiness(List<Integer> partialSolution, int currentHappiness) {
    int estimate = 0;
    for (int i = 0; i < PEOPLE.size(); i++) {
      List<Integer> neighbouringSeats = SEATS.get(i).getNeighbours();
      Table table = TABLES.get(SEATS.get(i).getTableNum());
      boolean hasPartner = false;
      if (i < partialSolution.size()) {
        if(neighbouringSeats.stream().allMatch(s -> s < partialSolution.size())) {
          continue;
        }
        int personIndex = partialSolution.get(i);
        Person currentPerson = PEOPLE.get(personIndex);
        for (int neighbouringSeat : neighbouringSeats) {
          if (neighbouringSeat < partialSolution.size() && !hasPartner) {
            Person neighbour = PEOPLE.get(partialSolution.get(neighbouringSeat));
            if (getMinimisingRelationship(currentPerson.getRelationshipWith(neighbour)) == PARTNER) {
              hasPartner = true;
            }
          } else if(neighbouringSeat >= partialSolution.size()) {
            // get best possible approximation - can have at most one partner
            int guess;
            boolean isNextTo = isNextTo(i, neighbouringSeat, table.getOffset(), table.getSize());
            if (!hasPartner && isNextTo) {
              guess = PARTNER;
              hasPartner = true;
            } else if(!hasPartner && i == PEOPLE.size() - 1) {
              guess = PARTNER;
              hasPartner = true;
            } else {
              guess = LIKES;
            }
            estimate += isNextTo ? 2 * guess : guess;
          }
        }
      } else {
        // at best a person will be sat next to their +1 and people they like ....
        int finalI = i;
        int countOfNextTo = (int) neighbouringSeats.stream().filter(neighbouringSeat -> isNextTo(finalI, neighbouringSeat, table.getOffset(), table.getSize())).count();
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
}
