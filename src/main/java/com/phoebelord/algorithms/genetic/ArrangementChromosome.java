package com.phoebelord.algorithms.genetic;

import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.model.Guest;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangementChromosome implements Comparable {

  private final List<Integer> chromosome;
  private final int fitness;
  private int generationNumber = 0;

  public ArrangementChromosome(List<Integer> chromosome, List<Guest> guests, List<Seat> seats, List<Table> tables, MaximisationType maximisationType) {
    this.chromosome = chromosome;
    this.fitness = this.calculateFitness(guests, seats, tables, maximisationType);
  }

  public ArrangementChromosome(List<Guest> guests, List<Seat> seats, List<Table> tables, MaximisationType maximisationType) {
    this.chromosome = randomChromosome(guests);
    this.fitness = this.calculateFitness(guests, seats, tables, maximisationType);

  }

  private List<Integer> randomChromosome(List<Guest> guests) {
    List<Integer> chromosome = new ArrayList<>();
    for (int i = 0; i < guests.size(); i++) {
      chromosome.add(i);
    }
    Collections.shuffle(chromosome);
    return chromosome;
  }

  public List<Integer> getChromosome() {
    return chromosome;
  }

  private int calculateFitness(List<Guest> guests, List<Seat> seats, List<Table> tables, MaximisationType maximisationType) {
    return Algorithm.calculateHappiness(getGuestList(guests), seats, tables, maximisationType);
  }

  public int getFitness() {
    return fitness;
  }

  public List<Guest> getGuestList(List<Guest> guests) {
    List<Guest> guestList = new ArrayList<>();
    for (int i = 0; i < chromosome.size(); i++) {
      guestList.add(i, guests.get(chromosome.get(i)));
    }
    return guestList;
  }

  public int getGenerationNumber() {
    return generationNumber;
  }

  public void setGenerationNumber(int generationNumber) {
    this.generationNumber = generationNumber;
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
