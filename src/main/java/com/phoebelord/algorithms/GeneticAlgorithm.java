package com.phoebelord.algorithms;

import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm extends Algorithm {

  private final int GENERATION_SIZE = 5000;
  private final int REPRODUCTION_SIZE = 200;
  private final int MAX_ITERATIONS = 1000;
  private final float RATE_OF_MUTATION = 0.1f;
  private final int TOURNAMENT_SIZE = 40;
  private final int GENOME_SIZE;
  private final int HAPPINESS_TARGET;
  private final List<Seat> SEATS;
  private final List<Person> PEOPLE;

  public GeneticAlgorithm(int genomeSize, List<Person> people, List<Seat> seats, int targetHappiness) {
    this.GENOME_SIZE = genomeSize;
    this.PEOPLE = people;
    this.SEATS = seats;
    this.HAPPINESS_TARGET = targetHappiness;
  }

  private List<ArrangementGenome> initialisePopulation() {
    List<ArrangementGenome> population = new ArrayList<ArrangementGenome>();
    for (int i = 0; i < GENERATION_SIZE; i++) {
      population.add(new ArrangementGenome(PEOPLE, SEATS));
    }
    return population;
  }

  private List<ArrangementGenome> selectParents(List<ArrangementGenome> population) {
    List<ArrangementGenome> selected = new ArrayList<ArrangementGenome>();
    for (int i = 0; i < REPRODUCTION_SIZE; i++) {
      selected.add(tournamentSelection(population));
    }
    return selected;
  }

  // TODO multiple selection procedures
  // pick the best from a number of random arrangements
  private ArrangementGenome tournamentSelection(List<ArrangementGenome> population) {
    List<ArrangementGenome> selected = getRandomElements(population, TOURNAMENT_SIZE);
    return Collections.max(selected);
  }

  private List<ArrangementGenome> getRandomElements(List<ArrangementGenome> list, int n) {
    Random r = new Random();
    int length = list.size();
    if (length < n) {
      return null;
    }
    List<ArrangementGenome> genomes = new ArrayList<ArrangementGenome>(list);
    Collections.shuffle(genomes);
    return genomes.subList(0, n);
  }

  private ArrangementGenome mutate(ArrangementGenome arrangement) {
    Random random = new Random();
    float mutate = random.nextFloat();
    if (mutate < RATE_OF_MUTATION) {
      List<Integer> genome = arrangement.getGenome();
      Collections.swap(genome, random.nextInt(GENOME_SIZE), random.nextInt(GENOME_SIZE));
      return new ArrangementGenome(genome, PEOPLE, SEATS);
    }
    return arrangement;
  }

  private List<ArrangementGenome> mutate(List<ArrangementGenome> genomes) {
    for (int i = 0; i < genomes.size(); i++) {
      genomes.set(i, mutate(genomes.get(i)));
    }
    return genomes;
  }

  // TODO 2 point crossover + others
  private List<ArrangementGenome> performOnePointOX(List<ArrangementGenome> parents) {
    Random random = new Random();
    int crossoverPoint = random.nextInt(GENOME_SIZE);

    List<Integer> parent1 = new ArrayList<Integer>(parents.get(0).getGenome());
    List<Integer> parent2 = new ArrayList<Integer>(parents.get(1).getGenome());

    List<Integer> child1 = new ArrayList<Integer>();
    List<Integer> child2 = new ArrayList<Integer>();

    for (int i = 0; i < crossoverPoint; i++) {
      child1.add(i, parent1.get(i));
      child2.add(i, parent2.get(i));
    }

    for (int i = 0; i < GENOME_SIZE; i++) {
      if (!child1.contains(parent2.get(i))) {
        child1.add(parent2.get(i));
      }
      if (!child2.contains((parent1.get(i)))) {
        child2.add(parent1.get(i));
      }
    }

    List<ArrangementGenome> children = new ArrayList<ArrangementGenome>();
    children.add(new ArrangementGenome(child1, PEOPLE, SEATS));
    children.add(new ArrangementGenome(child2, PEOPLE, SEATS));

    return children;
  }

  private ArrangementGenome getBestGenome() {
    List<ArrangementGenome> population = initialisePopulation();
    ArrangementGenome bestGenome = population.get(0);
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      List<ArrangementGenome> selected = selectParents(population);
      List<ArrangementGenome> generation = new ArrayList<ArrangementGenome>();
      int currentGenerationSize = 0;
      while (currentGenerationSize < GENERATION_SIZE) {
        List<ArrangementGenome> parents = getRandomElements(selected, 2);
        List<ArrangementGenome> children = performOnePointOX(parents);
        generation.addAll(mutate(children));
        currentGenerationSize += 2;
      }
      bestGenome = Collections.max(generation);
      if (bestGenome.getFitness() > HAPPINESS_TARGET) {
        break;
      }
    }
    return bestGenome;
  }

  @Override
  public Solution calculateSolution(List<Person> people, List<Seat> seats) {
    ArrangementGenome answer = getBestGenome();
    return new Solution(answer.getPersonList(), answer.getFitness());
  }
}
