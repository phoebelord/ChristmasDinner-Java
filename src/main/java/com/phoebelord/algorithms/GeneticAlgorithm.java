package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm extends Algorithm {

  private final int GENERATION_SIZE = 100;
  private final int SELECTION_SIZE = 50;
  private final int MAX_ITERATIONS = 1000;
  private final float RATE_OF_MUTATION = 0.1f;
  private final int TOURNAMENT_SIZE = 40;
  private final int CHROMOSOME_SIZE;
  private final List<Seat> SEATS;
  private final List<Person> PEOPLE;
  private final List<Table> TABLES;

  public GeneticAlgorithm(List<Person> people, List<Seat> seats, List<Table> tables) {
    this.PEOPLE = people;
    this.SEATS = seats;
    this.TABLES = tables;
    this.CHROMOSOME_SIZE = seats.size();
  }

  private List<ArrangementChromosome> initialisePopulation() {
    List<ArrangementChromosome> population = new ArrayList<ArrangementChromosome>();
    for (int i = 0; i < GENERATION_SIZE; i++) {
      population.add(new ArrangementChromosome(PEOPLE, SEATS, TABLES));
    }
    return population;
  }

  private List<ArrangementChromosome> selectParents(List<ArrangementChromosome> population) {
    List<ArrangementChromosome> selected = new ArrayList<ArrangementChromosome>();
    selected.add(Collections.max(population)); //elitism
    for (int i = 1; i < SELECTION_SIZE; i++) {
      selected.add(tournamentSelection(population));
    }
    return selected;
  }

  // TODO multiple selection procedures + elitism
  // pick the best from a number of random arrangements
  private ArrangementChromosome tournamentSelection(List<ArrangementChromosome> population) {
    List<ArrangementChromosome> selected = getRandomElements(population, TOURNAMENT_SIZE);
    return Collections.max(selected);
  }

  private List<ArrangementChromosome> getRandomElements(List<ArrangementChromosome> list, int n) {
    Random r = new Random();
    int length = list.size();
    if (length < n) {
      return null;
    }
    List<ArrangementChromosome> chromosomes = new ArrayList<ArrangementChromosome>(list);
    Collections.shuffle(chromosomes);
    return chromosomes.subList(0, n);
  }

  private ArrangementChromosome mutate(ArrangementChromosome arrangement) {
    Random random = new Random();
    float mutate = random.nextFloat();
    if (mutate < RATE_OF_MUTATION) {
      List<Integer> chromosome = arrangement.getChromosome();
      Collections.swap(chromosome, random.nextInt(CHROMOSOME_SIZE), random.nextInt(CHROMOSOME_SIZE));
      return new ArrangementChromosome(chromosome, PEOPLE, SEATS, TABLES);
    }
    return arrangement;
  }

  private List<ArrangementChromosome> mutate(List<ArrangementChromosome> chromosomes) {
    for (int i = 0; i < chromosomes.size(); i++) {
      chromosomes.set(i, mutate(chromosomes.get(i)));
    }
    return chromosomes;
  }

  // TODO 2 point crossover + others
  private List<ArrangementChromosome> performOnePointOX(List<ArrangementChromosome> parents) {
    Random random = new Random();
    int crossoverPoint = random.nextInt(CHROMOSOME_SIZE);

    List<Integer> parent1 = new ArrayList<Integer>(parents.get(0).getChromosome());
    List<Integer> parent2 = new ArrayList<Integer>(parents.get(1).getChromosome());

    List<Integer> child1 = new ArrayList<Integer>();
    List<Integer> child2 = new ArrayList<Integer>();

    for (int i = 0; i < crossoverPoint; i++) {
      child1.add(i, parent1.get(i));
      child2.add(i, parent2.get(i));
    }

    for (int i = 0; i < CHROMOSOME_SIZE; i++) {
      if (!child1.contains(parent2.get(i))) {
        child1.add(parent2.get(i));
      }
      if (!child2.contains((parent1.get(i)))) {
        child2.add(parent1.get(i));
      }
    }

    List<ArrangementChromosome> children = new ArrayList<ArrangementChromosome>();
    children.add(new ArrangementChromosome(child1, PEOPLE, SEATS, TABLES));
    children.add(new ArrangementChromosome(child2, PEOPLE, SEATS, TABLES));

    return children;
  }

  private ArrangementChromosome getBestChromosome() {
    List<ArrangementChromosome> population = initialisePopulation();
    ArrangementChromosome bestChromosome = population.get(0);
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      List<ArrangementChromosome> selected = selectParents(population);
      List<ArrangementChromosome> generation = new ArrayList<ArrangementChromosome>();
      generation.add(Collections.max(selected)); //elitism
      int currentGenerationSize = 1;
      while (currentGenerationSize < GENERATION_SIZE) {
        List<ArrangementChromosome> parents = getRandomElements(selected, 2);
        List<ArrangementChromosome> children = performOnePointOX(parents);
        generation.addAll(mutate(children));
        currentGenerationSize += 2;
      }
      bestChromosome = Collections.max(generation); // TODO stop if no improvement in 10 gens
      System.out.println(bestChromosome);
      population = new ArrayList<ArrangementChromosome>(generation);
    }
    return bestChromosome;
  }

  @Override
  public Solution calculateSolution() {
    ArrangementChromosome answer = getBestChromosome();
    return new Solution(answer.getPersonList(PEOPLE), answer.getFitness());
  }
}
