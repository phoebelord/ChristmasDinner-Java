package com.phoebelord.algorithms;

import com.phoebelord.algorithms.genetic.crossover.Crossover;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.Selection;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.model.Guest;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlgorithmFactory {

  private static GeneticAlgorithm geneticAlgorithm;

  private static NaiveAlgorithm naiveAlgorithm;

  private static BnBAlgorithm bnBAlgorithm;

  public static Algorithm createAlgorithm(AlgorithmType type, List<Guest> guests,
                                          List<Table> tables,
                                          MaximisationType maximisationType,
                                          SelectionType selection,
                                          CrossoverType crossover) {
    Algorithm algorithm;
    switch (type) {
      case Naive:
        algorithm = naiveAlgorithm;
        break;
      case BnB:
        algorithm = bnBAlgorithm;
        break;
      case Genetic:
      default:
        algorithm = geneticAlgorithm; //throw exception?
        break;
    }

    algorithm.setGuests(guests);
    algorithm.setTablesAndSeats(tables);
    algorithm.setMaximisationType((maximisationType == null) ? MaximisationType.HAPPINESS : maximisationType);
    algorithm.setSelection(selection);
    algorithm.setCrossover(crossover);
    return algorithm;
  }

  @Autowired
  public void setGeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
    AlgorithmFactory.geneticAlgorithm = geneticAlgorithm;
  }

  @Autowired
  public void setNaiveAlgorithm(NaiveAlgorithm naiveAlgorithm) {
    AlgorithmFactory.naiveAlgorithm = naiveAlgorithm;
  }

  @Autowired
  public void setBnBAlgorithm(BnBAlgorithm bnBAlgorithm) {
    AlgorithmFactory.bnBAlgorithm = bnBAlgorithm;
  }
}
