package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// https://www.baeldung.com/java-array-permutations

public class NaiveAlgorithm extends Algorithm {

  private int bestSolution = 0;
  private List<Person> solution;
  private List<Person> people;
  private List<Seat> seats;
  private List<Table> tables;
  private float counter = 0;
  private float lastCount = 0;
  private float noSolutions;

  NaiveAlgorithm(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.people = people;
    this.seats = seats;
    this.tables = tables;
    noSolutions = factoriall(seats.size());
  }

  @Override
  public Solution calculateSolution() {
    //calculateAllSolutions(people.size(), people);
    calculateAllSolutions(people);
    return new Solution(solution, bestSolution);
  }

  private void calculateAllSolutions(int n, List<Person> elements) {
    if(n == 1) {
      calculateHappiness(elements);
    } else {
      for(int i = 0; i < n-1; i++) {
        calculateAllSolutions(n - 1, elements);
        if(n % 2 == 0) {
          Collections.swap(elements, i, n - 1);
        } else {
          Collections.swap(elements, 0, n - 1);
        }
      }
      calculateAllSolutions(n - 1, elements);
    }
  }

  private void calculateAllSolutions(List<Person> elements) {
    int[] indexes = new int[elements.size()];
    for (int i = 0; i < elements.size(); i++) {
      indexes[i] = 0;
    }

    int i = 0;
    while (i < elements.size()) {
      if (indexes[i] < i) {
        Collections.swap(elements, i % 2 == 0 ?  0: indexes[i], i);
        indexes[i]++;
        i = 0;
        calculateHappiness(elements);
      }
      else {
        indexes[i] = 0;
        i++;
      }
    }
  }

  private void calculateHappiness(List<Person> elements) {
    counter++;
    if(((counter / noSolutions)*100) - ((lastCount / noSolutions)*100) >= 10)
    {
      System.out.printf("%2.2f\n",(counter / noSolutions)*100);
      lastCount = counter;
    }
    int solutionHappiness = calculateHappiness(elements, seats, tables);
    if(solutionHappiness > bestSolution) {
      bestSolution = solutionHappiness;
      solution = new ArrayList<>(elements);
    }
  }
}
