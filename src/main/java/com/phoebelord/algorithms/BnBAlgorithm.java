package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
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
  public Solution calculateSolution() {
    bestSoFar = Integer.MAX_VALUE;
    initialiseCounters();
    for (int i = 0; i < people.size(); i++) {
      List<Integer> partialSolution = new ArrayList<>();
      partialSolution.add(i);
      bnb(1, partialSolution, 0);
    }
    List<Person> personList = new ArrayList<>();
    for (int personIndex : bestSolution) {
      personList.add(people.get(personIndex));
    }
    System.out.println("Pruned: " + pruned);
    System.out.println("Counter: " + counter);
    return new Solution(tables, personList, Algorithm.calculateHappiness(personList, seats, tables));
  }

  private void bnb(int level, List<Integer> partialSolution, int currentHappiness) {
    if (level == people.size()) {
      counter = counter.add(BigDecimal.ONE);
      if (currentHappiness < bestSoFar) {
        bestSoFar = currentHappiness;
        bestSolution = new ArrayList<>(partialSolution);
      }
    } else {
      for (int i = 0; i < people.size(); i++) {
        if (!partialSolution.contains(i)) {
          List<Integer> newList = new ArrayList<>(partialSolution);
          newList.add(i);
          int newHappiness = recalculateHappiness(newList, currentHappiness);
          int lowerBound = estimateHappiness(newList, newHappiness);
          if (lowerBound < bestSoFar) {
            bnb(level + 1, newList, newHappiness);
          } else {
            counter = counter.add(factorial(people.size() - (level + 1)));
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
    Person currentPerson = people.get(currentPersonIndex);
    Seat seat = seats.get(partialSolution.size() - 1);
    List<Integer> neighbouringSeats = seat.getNeighbours();
    Table table = tables.get(seat.getTableNum());
    for(int neighbouringSeat : neighbouringSeats) {
      if(neighbouringSeat < partialSolution.size()) {
        Person neighbour = people.get(partialSolution.get(neighbouringSeat));
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
    for (int i = 0; i < people.size(); i++) {
      List<Integer> neighbouringSeats = seats.get(i).getNeighbours();
      Table table = tables.get(seats.get(i).getTableNum());
      boolean hasPartner = false;
      if (i < partialSolution.size()) {
        if(neighbouringSeats.stream().allMatch(s -> s < partialSolution.size())) {
          continue;
        }
        int personIndex = partialSolution.get(i);
        Person currentPerson = people.get(personIndex);
        for (int neighbouringSeat : neighbouringSeats) {
          if (neighbouringSeat < partialSolution.size() && !hasPartner) {
            Person neighbour = people.get(partialSolution.get(neighbouringSeat));
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
            } else if(!hasPartner && i == people.size() - 1) {
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

  @Override
  public void setSeats(List<Seat> seats) {
    this.seats = seats;
    this.noSolutions = factorial(seats.size());
  }

  private void initialiseCounters() {
    counter = BigDecimal.ZERO;
    pruned = BigDecimal.ZERO;
    lastCount = BigDecimal.ZERO;
  }
}
