package com.phoebelord.algorithms.genetic.selection;

import com.phoebelord.algorithms.genetic.ArrangementChromosome;
import com.phoebelord.algorithms.genetic.GeneticAlgorithmUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TournamentSelection implements Selection {

  private final int TOURNAMENT_SIZE = 40;

  @Override
  public List<ArrangementChromosome> getSelection(List<ArrangementChromosome> population, int selectionSize) {
    List<ArrangementChromosome> selected = new ArrayList<>();
    selected.add(Collections.max(population)); //elitism
    for (int i = 1; i < selectionSize; i++) {
      selected.add(tournamentSelection(population));
    }
    return selected;
  }

  private ArrangementChromosome tournamentSelection(List<ArrangementChromosome> population) {
    List<ArrangementChromosome> selected = GeneticAlgorithmUtils.getRandomElements(population, TOURNAMENT_SIZE);
    return Collections.max(Objects.requireNonNull(selected));
  }
}
