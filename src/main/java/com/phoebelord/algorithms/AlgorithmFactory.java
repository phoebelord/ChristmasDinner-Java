package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlgorithmFactory {

  private static GeneticAlgorithm geneticAlgorithm;

  private static NaiveAlgorithm naiveAlgorithm;

  private static BnBAlgorithm bnBAlgorithm;

  public static Algorithm createAlgorithm(AlgorithmType type, List<Person> people, List<Seat> seats, List<Table> tables) {
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

    algorithm.setPeople(people);
    algorithm.setSeats(seats);
    algorithm.setTables(tables);
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
