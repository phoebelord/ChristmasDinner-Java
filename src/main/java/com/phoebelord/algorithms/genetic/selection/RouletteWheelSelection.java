package com.phoebelord.algorithms.genetic.selection;

import com.phoebelord.algorithms.genetic.ArrangementChromosome;

import java.util.*;

public class RouletteWheelSelection implements Selection {
  @Override
  public List<ArrangementChromosome> getSelection(List<ArrangementChromosome> population, int selectionSize) {
    int minFitness = Collections.min(population).getFitness();
    int offset = (minFitness < 0) ? Math.abs(minFitness) : 0;

    int totalFitness = population.stream().mapToInt(chromosome -> chromosome.getFitness() + offset).sum();

    double[] cumulativeHappiness = new double[population.size()];
    double sumOfProbabilities = 0.0;

    for(int i = 0; i < population.size(); i++) {
      ArrangementChromosome chromosome = population.get(i);
      double probability = ((chromosome.getFitness() + offset) / (double)totalFitness);
      sumOfProbabilities += probability;
      cumulativeHappiness[i] = sumOfProbabilities;
    }

    List<ArrangementChromosome> selected = new ArrayList<>();
    selected.add(Collections.max(population)); //elitism
    for(int i = 0; i < selectionSize; i++) {
      Random random = new Random();
      double probability = random.nextDouble();
      int index = Arrays.binarySearch(cumulativeHappiness, probability);
      if(index < 0) {
        index = -(index + 1);
      } else if(index >= population.size()) {
        index = population.size() - 1;
      }
      selected.add(population.get(index));
    }
    return selected;
  }
}
