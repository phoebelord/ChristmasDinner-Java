package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangementChromosome implements Comparable {

  final List<Integer> chromosome;
  final List<Person> people;
  final List<Seat> seats;
  final int fitness;

  public ArrangementChromosome(List<Integer> chromosome, List<Person> people, List<Seat> seats) {
    this.chromosome = chromosome;
    this.people = people;
    this.seats = seats;
    this.fitness = this.calculateFitness();
  }

  public ArrangementChromosome(List<Person> people, List<Seat> seats) {
    this.people = people;
    this.seats = seats;
    this.chromosome = randomChromosome();
    this.fitness = this.calculateFitness();

  }

  private List<Integer> randomChromosome() {
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

  private int calculateFitness() {
    return Algorithm.calculateHappiness(getPersonList(), seats);
  }

  public int getFitness() {
    return fitness;
  }

  public List<Person> getPersonList() {
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
