package com.phoebelord.algorithms.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithmUtils {

  public static List<ArrangementChromosome> getRandomElements(List<ArrangementChromosome> list, int n) {
    int length = list.size();
    if (length < n) {
      return null;
    }
    List<ArrangementChromosome> chromosomes = new ArrayList<>(list);
    Collections.shuffle(chromosomes);
    return chromosomes.subList(0, n);
  }
}
