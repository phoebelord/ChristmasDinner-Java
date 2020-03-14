package com.phoebelord.algorithms;

import com.phoebelord.model.Guest;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// https://www.baeldung.com/java-array-permutations

@Component
@Scope("prototype")
public class NaiveAlgorithm extends Algorithm {

  private int bestSolution;
  private List<Guest> solution;
  private float counter;
  private float lastCount;
  private float noSolutions;

  @Override
  public Solution[] calculateSolution() {
    initialiseCounters();
    calculateAllSolutions(guests);
    return new Solution[]{new Solution(tables, solution, bestSolution)};
  }

  private void calculateAllSolutions(int n, List<Guest> elements) {
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

  private void calculateAllSolutions(List<Guest> elements) {
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

  private void calculateHappiness(List<Guest> elements) {
    counter++;
    if(((counter / noSolutions)*100) - ((lastCount / noSolutions)*100) >= 10)
    {
      System.out.printf("%2.2f\n",(counter / noSolutions)*100);
      lastCount = counter;
    }
    int solutionHappiness = calculateHappiness(elements, seats, tables, maximisationType);
    if(solutionHappiness > bestSolution) {
      bestSolution = solutionHappiness;
      solution = new ArrayList<>(elements);
    }
  }

  @Override
  public void setTablesAndSeats(List<Table> tables) {
    super.setTablesAndSeats(tables);
    this.noSolutions = factoriall(seats.size());
  }

  private void initialiseCounters() {
    counter = 0;
    lastCount = 0;
    bestSolution = 0;
  }
}
