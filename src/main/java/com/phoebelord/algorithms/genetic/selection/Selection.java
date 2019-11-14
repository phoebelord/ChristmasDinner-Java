package com.phoebelord.algorithms.genetic.selection;

import com.phoebelord.algorithms.genetic.ArrangementChromosome;

import java.util.List;

public interface Selection {

  List<ArrangementChromosome> getSelection(List<ArrangementChromosome> population, int selectionSize);
}
