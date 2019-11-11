package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangementChromosome implements Comparable {

  final List<Integer> chromosome;
  final int fitness;

  public ArrangementChromosome(List<Integer> chromosome, List<Person> people, List<Seat> seats, List<Table> tables) {
    this.chromosome = chromosome;
    this.fitness = this.calculateFitness(people, seats, tables);
  }

  public ArrangementChromosome(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.chromosome = randomChromosome(people);
    this.fitness = this.calculateFitness(people, seats, tables);

  }

  private List<Integer> randomChromosome(List<Person> people) {
    List<Integer> chromosome = new ArrayList<Integer>();
    for (int i = 0; i < people.size(); i++) {
      chromosome.add(i);
    }
    Collections.shuffle(chromosome);
    return chromosome;
  }

  public List<Integer> getChromosome() {
    return chromosome;
  }

  private int calculateFitness(List<Person> people, List<Seat> seats, List<Table> tables) {
    return Algorithm.calculateHappiness(getPersonList(people), seats, tables);
  }

  public int getFitness() {
    return fitness;
  }

  public List<Person> getPersonList(List<Person> people) {
    List<Person> personList = new ArrayList<Person>();
    for (int i = 0; i < chromosome.size(); i++) {
      personList.add(i, people.get(chromosome.get(i)));
    }
    return personList;
  }


  @Override
  public int compareTo(Object o) {
    ArrangementChromosome chromosome = (ArrangementChromosome) o;
    if (this.fitness > chromosome.getFitness()) {
      return 1;
    } else if (this.fitness < chromosome.getFitness()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "[" + chromosome + ", " + fitness + "]";
  }
}
