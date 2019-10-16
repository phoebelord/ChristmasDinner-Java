package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaiveAlgorithm extends Algorithm {

  private int bestSolution = 0;
  private List<Person> solution;
  private List<Seat> seats;


  @Override
  public Solution calculateSolution(List<Person> people, List<Seat> seats) {
    this.seats = seats;
    calculateAllSolutions(people.size(), people);
    return new Solution(solution, bestSolution);
  }



  private void calculateAllSolutions(int n, List<Person> elements) {
    if(n == 1) {
      int solutionHappiness = calculateHappiness(elements, seats);
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
}
