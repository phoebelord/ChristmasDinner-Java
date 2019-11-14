package com.phoebelord.algorithms.genetic.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnePointCrossover implements Crossover {

  @Override
  public List<List<Integer>> performCrossover(List<Integer> parent1, List<Integer> parent2) {
    Random random = new Random();
    int chromosomeSize = parent1.size();
    int crossoverPoint = random.nextInt(chromosomeSize);

    List<Integer> child1 = new ArrayList<>();
    List<Integer> child2 = new ArrayList<>();

    for (int i = 0; i < crossoverPoint; i++) {
      child1.add(i, parent1.get(i));
      child2.add(i, parent2.get(i));
    }

    for (int i = 0; i < chromosomeSize; i++) {
      if (!child1.contains(parent2.get(i))) {
        child1.add(parent2.get(i));
      }
      if (!child2.contains((parent1.get(i)))) {
        child2.add(parent1.get(i));
      }
    }

    List<List<Integer>> children = new ArrayList<>();
    children.add(child1);
    children.add(child2);

    return children;
  }
}
