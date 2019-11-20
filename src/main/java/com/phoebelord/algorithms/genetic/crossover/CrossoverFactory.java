package com.phoebelord.algorithms.genetic.crossover;

public class CrossoverFactory {
  public static Crossover createCrossover(CrossoverType crossoverType) {
    switch (crossoverType) {
      case TwoPoint: return new TwoPointCrossover();
      case OnePoint:
      default: return new OnePointCrossover();
    }
  }
}
