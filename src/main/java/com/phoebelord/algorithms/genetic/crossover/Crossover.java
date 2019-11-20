package com.phoebelord.algorithms.genetic.crossover;

import java.util.List;

public interface Crossover {

  abstract List<List<Integer>> performCrossover(List<Integer> parent1, List<Integer> parent2);
}
