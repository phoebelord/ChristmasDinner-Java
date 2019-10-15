package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import org.apache.commons.lang3.SerializationUtils;

public class NaiveAlgorithm extends Algorithm {

  private int bestSolution = 0;
  private Person[] solution;
  private Seat[] seats;


  @Override
  public Solution calculateSolution(Person[] people, Seat[] seats) {
    this.seats = seats;
    calculateAllSolutions(people.length, people);
    return new Solution(solution, bestSolution);
  }



  private void calculateAllSolutions(int n, Person[] elements) {
    if(n == 1) {
      int solutionHappiness = calculateHappiness(elements, seats);
      if(solutionHappiness > bestSolution) {
        bestSolution = solutionHappiness;
        solution = SerializationUtils.clone(elements);
      }
    } else {
      for(int i = 0; i < n-1; i++) {
        calculateAllSolutions(n - 1, elements);
        if(n % 2 == 0) {
          swap(elements, i, n - 1);
        } else {
          swap(elements, 0, n - 1);
        }
      }
      calculateAllSolutions(n - 1, elements);
    }
  }

  private <T> void swap(T[] input, int a, int b) {
    T tmp = input[a];
    input[a] = input[b];
    input[b] = tmp;
  }
}
