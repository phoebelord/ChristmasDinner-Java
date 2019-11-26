package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.List;

public class AlgorithmFactory {
  public static Algorithm createAlgorithm(AlgorithmType type, List<Person> people, List<Seat> seats, List<Table> tables) {
    switch (type) {
      case Naive: return new NaiveAlgorithm(people, seats, tables);
      case BnB: return new BnBAlgorithm(people, seats, tables);
      case Genetic:
      default: return new GeneticAlgorithm(people, seats, tables); //throw exception?
    }
  }
}
