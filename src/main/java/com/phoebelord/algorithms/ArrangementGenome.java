package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangementGenome implements Comparable {

  final List<Integer> genome;
  final List<Person> people;
  final List<Seat> seats;
  final int fitness;

  public ArrangementGenome(List<Integer> genome, List<Person> people, List<Seat> seats) {
    this.genome = genome;
    this.people = people;
    this.seats = seats;
    this.fitness = this.calculateFitness();
  }

  public ArrangementGenome(List<Person> people, List<Seat> seats) {
    this.people = people;
    this.seats = seats;
    this.genome = randomGenome();
    this.fitness = this.calculateFitness();

  }

  private List<Integer> randomGenome() {
    List<Integer> genome = new ArrayList<Integer>();
    for (int i = 0; i < people.size(); i++) {
      genome.add(i);
    }
    Collections.shuffle(genome);
    return genome;
  }

  public List<Integer> getGenome() {
    return genome;
  }

  private int calculateFitness() {
    return Algorithm.calculateHappiness(getPersonList(), seats);
  }

  public int getFitness() {
    return fitness;
  }

  public List<Person> getPersonList() {
    List<Person> personList = new ArrayList<Person>();
    for (int i = 0; i < genome.size(); i++) {
      personList.add(i, people.get(genome.get(i)));
    }
    return personList;
  }


  @Override
  public int compareTo(Object o) {
    ArrangementGenome genome = (ArrangementGenome) o;
    if (this.fitness > genome.getFitness()) {
      return 1;
    } else if (this.fitness < genome.getFitness()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "[" + genome + ", " + fitness + "]";
  }
}
