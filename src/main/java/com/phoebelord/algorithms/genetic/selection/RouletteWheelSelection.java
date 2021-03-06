package com.phoebelord.algorithms.genetic.selection;

import com.phoebelord.algorithms.genetic.ArrangementChromosome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouletteWheelSelection implements Selection {

  private Random random;

  @Override
  public List<ArrangementChromosome> getSelection(List<ArrangementChromosome> population, int selectionSize) {
    int minFitness = Collections.min(population).getFitness();
    int offset = (minFitness < 0) ? Math.abs(minFitness) : 0;

    double[] cumulativeHappiness = getCumulativeHappiness(population, offset);
    List<ArrangementChromosome> selected = new ArrayList<>();
    selected.add(Collections.max(population)); //elitism
    for(int i = 1; i < selectionSize; i++) {
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

  private double[] getCumulativeHappiness(List<ArrangementChromosome> population, int offset) {
    int totalFitness = population.stream().mapToInt(chromosome -> chromosome.getFitness() + offset).sum();
    double[] cumulativeHappiness = new double[population.size()];
    double sumOfProbabilities = 0.0;

    for(int i = 0; i < population.size(); i++) {
      ArrangementChromosome chromosome = population.get(i);
      double probability = ((chromosome.getFitness() + offset) / (double)totalFitness);
      sumOfProbabilities += probability;
      cumulativeHappiness[i] = sumOfProbabilities;
    }

    return cumulativeHappiness;
  }

  @Autowired
  public void setRandom(Random random) {
    this.random = random;
  }
}
