package com.phoebelord.algorithms;

import com.phoebelord.model.Solution;
import com.phoebelord.algorithms.genetic.ArrangementChromosome;
import com.phoebelord.algorithms.genetic.GeneticAlgorithmUtils;
import com.phoebelord.algorithms.genetic.crossover.Crossover;
import com.phoebelord.algorithms.genetic.crossover.CrossoverFactory;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.Selection;
import com.phoebelord.algorithms.genetic.selection.SelectionFactory;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneticAlgorithm extends Algorithm {

  private final int GENERATION_SIZE = 100;
  private final int SELECTION_SIZE = 50;
  private final int MAX_ITERATIONS = 1000;
  private final float RATE_OF_MUTATION = 0.1f;
  private int chromosomeSize;

  private Random random;

  @Override
  public Solution calculateSolution() {
    List<ArrangementChromosome> currentGeneration = initialisePopulation();
    ArrangementChromosome bestChromosome = currentGeneration.get(0);
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      List<ArrangementChromosome> selection = getSelection(currentGeneration, SelectionType.TOURNAMENT);
      List<ArrangementChromosome> nextGeneration = new ArrayList<>();
      nextGeneration.add(Collections.max(selection)); //elitism
      int currentGenerationSize = 1;
      while (currentGenerationSize < GENERATION_SIZE) {
        List<ArrangementChromosome> parents = GeneticAlgorithmUtils.getRandomElements(selection, 2);
        List<ArrangementChromosome> children = performCrossover(Objects.requireNonNull(parents), CrossoverType.TwoPoint);
        nextGeneration.addAll(children);
        currentGenerationSize += 2;
      }
      nextGeneration = performMutation(nextGeneration);
      bestChromosome = Collections.max(nextGeneration); // TODO stop if no improvement in 10 gens
      //System.out.println(bestChromosome);
      currentGeneration = new ArrayList<>(nextGeneration);
    }
    return new Solution(bestChromosome.getPersonList(people), bestChromosome.getFitness());
  }

  private List<ArrangementChromosome> initialisePopulation() {
    List<ArrangementChromosome> population = new ArrayList<>();
    for (int i = 0; i < GENERATION_SIZE; i++) {
      population.add(new ArrangementChromosome(people, seats, tables));
    }
    return population;
  }

  private List<ArrangementChromosome> getSelection(List<ArrangementChromosome> population, SelectionType selectionType) {
    Selection selectionMethod = SelectionFactory.createSelectionMethod(selectionType);
    return selectionMethod.getSelection(population, SELECTION_SIZE);
  }

  private List<ArrangementChromosome> performCrossover(List<ArrangementChromosome> parents, CrossoverType crossoverType) {
    Crossover crossoverMethod = CrossoverFactory.createCrossover(crossoverType);
    List<List<Integer>> children = crossoverMethod.performCrossover(parents.get(0).getChromosome(), parents.get(1).getChromosome());
    return children.stream().map(this::createChromosome).collect(Collectors.toList());
  }

  private List<ArrangementChromosome> performMutation(List<ArrangementChromosome> chromosomes) {
    return chromosomes.stream().map(this::mutate).collect(Collectors.toList());
  }
  private ArrangementChromosome mutate(ArrangementChromosome arrangement) {
    float mutate = random.nextFloat();
    if (mutate < RATE_OF_MUTATION) {
      List<Integer> chromosome = arrangement.getChromosome();
      Collections.swap(chromosome, random.nextInt(chromosomeSize), random.nextInt(chromosomeSize));
      return createChromosome(chromosome);
    }
    return arrangement;
  }

  private ArrangementChromosome createChromosome(List<Integer> arrangement) {
    return new ArrangementChromosome(arrangement, people, seats, tables);
  }

  @Override
  public void setSeats(List<Seat> seats) {
    this.seats = seats;
    this.chromosomeSize = seats.size();
  }

  @Autowired
  public void setRandom(Random random) {
    this.random = random;
  }
}
