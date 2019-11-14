package com.phoebelord.algorithms.genetic;

import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangementChromosome implements Comparable {

  private final List<Integer> chromosome;
  private final int fitness;

  public ArrangementChromosome(List<Integer> chromosome, List<Person> people, List<Seat> seats, List<Table> tables) {
    this.chromosome = chromosome;
    this.fitness = this.calculateFitness(people, seats, tables);
  }

  public ArrangementChromosome(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.chromosome = randomChromosome(people);
    this.fitness = this.calculateFitness(people, seats, tables);

  }

  private List<Integer> randomChromosome(List<Person> people) {
    List<Integer> chromosome = new ArrayList<>();
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
    List<Person> personList = new ArrayList<>();
    for (int i = 0; i < chromosome.size(); i++) {
      personList.add(i, people.get(chromosome.get(i)));
    }
    return personList;
  }

  @Override
  public int compareTo(Object o) {
    ArrangementChromosome chromosome = (ArrangementChromosome) o;
    return Integer.compare(this.fitness, chromosome.getFitness());
  }

  @Override
  public String toString() {
    return "[" + chromosome + ", " + fitness + "]";
  }
}
