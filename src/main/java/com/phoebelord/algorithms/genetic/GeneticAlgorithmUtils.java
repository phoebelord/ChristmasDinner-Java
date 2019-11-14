package com.phoebelord.algorithms.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmUtils {

  public static List<ArrangementChromosome> getRandomElements(List<ArrangementChromosome> list, int n) {
    Random r = new Random();
    int length = list.size();
    if (length < n) {
      return null;
    }
    List<ArrangementChromosome> chromosomes = new ArrayList<>(list);
    Collections.shuffle(chromosomes);
    return chromosomes.subList(0, n);
  }
}
