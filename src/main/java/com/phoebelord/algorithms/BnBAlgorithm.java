package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;

import java.math.BigDecimal;
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
      bnb(1, partialSolution);
    }
    List<Person> personList = new ArrayList<>();
    for (int personIndex : bestSolution) {
      personList.add(PEOPLE.get(personIndex));
    }
    System.out.println("Pruned: " + pruned);
    System.out.println("Counter: " + counter);
    return new Solution(personList, Algorithm.calculateHappiness(personList, SEATS, TABLES));
  }

  private void bnb(int level, List<Integer> partialSolution) {
    if (level == PEOPLE.size()) {
      counter = counter.add(BigDecimal.ONE);
      int happiness = calculateHappiness(partialSolution);
      if (happiness < bestSoFar) {
        bestSoFar = happiness;
        bestSolution = new ArrayList<>(partialSolution);
      }
    } else {
      for (int i = 0; i < PEOPLE.size(); i++) {
        if (!partialSolution.contains(i)) {
          int lowerBound = calculateHappiness(partialSolution, i);
          if (lowerBound < bestSoFar) {
            List<Integer> newList = new ArrayList<>(partialSolution);
            newList.add(i);
            bnb(level + 1, newList);
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

  private int calculateHappiness(List<Integer> partialSolution, int next) {
    List<Integer> newList = new ArrayList<>(partialSolution);
    newList.add(next);
    return calculateHappiness(newList);
  }

  // TODO this is a mess
  // does it even work lolllllllll
  private int calculateHappiness(List<Integer> partialSolution) {
    int total = 0;
    for (int i = 0; i < PEOPLE.size(); i++) {
      int personHappiness = 0;
      List<Integer> neighbouringSeats = SEATS.get(i).getNeighbours();
      Table table = TABLES.get(SEATS.get(i).getTableNum());
      boolean hasPartner = false;
      if (i < partialSolution.size()) {
        int personIndex = partialSolution.get(i);
        Person currentPerson = PEOPLE.get(personIndex);
        for (int neighbouringSeat : neighbouringSeats) {
          int relationshipWithNeighbour;
          boolean isNextTo = isNextTo(i, neighbouringSeat, table.getOffset(), table.getSize());
          if (neighbouringSeat < partialSolution.size()) {
            Person neighbour = PEOPLE.get(partialSolution.get(neighbouringSeat));
            relationshipWithNeighbour = getMinimisingRelationship(currentPerson.getRelationshipWith(neighbour));
            if (relationshipWithNeighbour == PARTNER) {
              hasPartner = true;
            }
          } else {
            // get best possible approximation - can have at most one partner
            if (!hasPartner && isNextTo) {
              relationshipWithNeighbour = PARTNER;
              hasPartner = true;
            } else if(!hasPartner && i == PEOPLE.size() - 1) {
              relationshipWithNeighbour = PARTNER;
              hasPartner = true;
            } else {
              relationshipWithNeighbour = LIKES;
            }
          }
          personHappiness += isNextTo ? 2 * relationshipWithNeighbour : relationshipWithNeighbour;
        }
      } else {
        // at best a person will be sat next to their +1 and people they like ....
        int finalI = i;
        int countOfNextTo = (int) neighbouringSeats.stream().filter(neighbouringSeat -> isNextTo(finalI, neighbouringSeat, table.getOffset(), table.getSize())).count();
        if (countOfNextTo >= 1) {
          personHappiness = (2 * PARTNER) + (2 * LIKES * (countOfNextTo - 1)) + (LIKES * (neighbouringSeats.size() - countOfNextTo));
        } else {
          System.out.println("error");
        }
      }
      total += personHappiness;
    }

    return total;
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
