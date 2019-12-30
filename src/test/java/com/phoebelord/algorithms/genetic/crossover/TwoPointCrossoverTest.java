package com.phoebelord.algorithms.genetic.crossover;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwoPointCrossoverTest {

  @Mock
  Random random;

  @InjectMocks
  TwoPointCrossover twoPointCrossover;

  List<Integer> parent1 = Arrays.asList(1, 2, 3, 4);
  List<Integer> parent2 = Arrays.asList(4, 3, 2, 1);

  @Test
  public void givenCrossoverPointOf0And3ThenParentsAreUnchanged() {
    when(random.nextInt(parent1.size())).thenReturn(0).thenReturn(3);
    List<List<Integer>> children = twoPointCrossover.performCrossover(parent1, parent2);

    assertEquals(children.get(0), parent1);
    assertEquals(children.get(1), parent2);
  }

  @Test
  public void givenCrossoverPointOf3And0ThenParentsAreUnchanged() {
    when(random.nextInt(parent1.size())).thenReturn(3).thenReturn(0);
    List<List<Integer>> children = twoPointCrossover.performCrossover(parent1, parent2);

    assertEquals(children.get(0), parent1);
    assertEquals(children.get(1), parent2);
  }

  @Test
  public void givenMiddleCrossoverPointsThenChildrenCross() {
    when(random.nextInt(parent1.size())).thenReturn(1).thenReturn(2);
    List<List<Integer>> children = twoPointCrossover.performCrossover(parent1, parent2);

    assertEquals(children.get(0), Arrays.asList(4, 2, 3, 1));
    assertEquals(children.get(1), Arrays.asList(1, 3, 2, 4));
  }
}
