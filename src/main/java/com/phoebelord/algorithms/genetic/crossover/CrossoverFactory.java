package com.phoebelord.algorithms.genetic.crossover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrossoverFactory {

  private static TwoPointCrossover twoPointCrossover;

  private static  OnePointCrossover onePointCrossover;

  public static Crossover createCrossover(CrossoverType crossoverType) {
    switch (crossoverType) {
      case TwoPoint: return twoPointCrossover;
      case OnePoint:
      default: return onePointCrossover;
    }
  }

  @Autowired
  public void setTwoPointCrossover(TwoPointCrossover twoPointCrossover) {
    CrossoverFactory.twoPointCrossover = twoPointCrossover;
  }

  @Autowired
  public void setOnePointCrossover(OnePointCrossover onePointCrossover) {
    CrossoverFactory.onePointCrossover = onePointCrossover;
  }
}
