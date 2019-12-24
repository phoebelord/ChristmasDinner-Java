package com.phoebelord.algorithms.genetic.crossover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class TwoPointCrossover implements Crossover {

  private Random random;

  @Override
  public List<List<Integer>> performCrossover(List<Integer> parent1, List<Integer> parent2) {
    int chromosomeSize = parent1.size();
    int crossoverPointOne = random.nextInt(chromosomeSize);
    int crossoverPointTwo = random.nextInt(chromosomeSize);

    if(crossoverPointTwo < crossoverPointOne) {
      int temp = crossoverPointOne;
      crossoverPointOne = crossoverPointTwo;
      crossoverPointTwo = temp;
    }

    int[] child1 = new int[chromosomeSize];
    Arrays.fill(child1, -1);
    int[] child2 = new int[chromosomeSize];
    Arrays.fill(child2, -1);

    for (int i = crossoverPointOne; i <= crossoverPointTwo; i++) {
      child1[i] = parent1.get(i);
      child2[i] = parent2.get(i);
    }

    for (int i = 0; i < chromosomeSize; i++) {
      if(!arrayContains(child1, parent2.get(i))) {
        insertAtNextAvailableIndex(child1, parent2.get(i));
      }

      if(!arrayContains(child2, parent1.get(i))) {
        insertAtNextAvailableIndex(child2, parent1.get(i));
      }
    }

    List<List<Integer>> children = new ArrayList<>();
    children.add(convertToIntegerList(child1));
    children.add(convertToIntegerList(child2));

    return children;
  }

  private boolean arrayContains(int[] array, int element){
    for(int item : array) {
      if(item == element) {
        return true;
      }
    }
    return false;
  }

  private void insertAtNextAvailableIndex(int[] array, int element) {
    for(int i = 0; i < array.length; i++){
      if(array[i] == -1) {
        array[i] = element;
        return;
      }
    }
  }

  private List<Integer> convertToIntegerList(int[] array) {
    List<Integer> list = new ArrayList<>(array.length);
    for(int item: array) {
      list.add(item);
    }
    return list;
  }

  @Autowired
  public void setRandom(Random random) {
    this.random = random;
  }
}
