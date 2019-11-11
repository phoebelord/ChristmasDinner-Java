package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaiveAlgorithm extends Algorithm {

  private int bestSolution = 0;
  private List<Person> solution;
  private List<Person> people;
  private List<Seat> seats;
  private List<Table> tables;
  private float counter = 0;
  private float lastCount = 0;
  private long noSolutions;

  public NaiveAlgorithm(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.people = people;
    this.seats = seats;
    this.tables = tables;
    noSolutions = factorial(seats.size());
  }

  @Override
  public Solution calculateSolution() {
    calculateAllSolutions(people.size(), people);
    return new Solution(solution, bestSolution);
  }



  private void calculateAllSolutions(int n, List<Person> elements) {
    if(n == 1) {
      counter++;
      if(((counter / noSolutions)*100) - ((lastCount / noSolutions)*100) >= 10)
      {
        System.out.println("" + (counter / noSolutions)*100);
        lastCount = counter;
      }
      int solutionHappiness = calculateHappiness(elements, seats, tables);
      if(solutionHappiness > bestSolution) {
        bestSolution = solutionHappiness;
        solution = new ArrayList<Person>(elements);
      }
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

  private long factorial(int n) {
    if(n == 0) {
      return 1;
    } else {
      return n * factorial(n - 1);
    }
  }
}
