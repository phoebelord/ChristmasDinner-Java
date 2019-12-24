package com.phoebelord.algorithms.genetic.crossover;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OnePointCrossoverTest {

  @Mock
  Random random;

  @InjectMocks
  OnePointCrossover onePointCrossover;

  List<Integer> parent1 = Arrays.asList(1, 2, 3, 4);
  List<Integer> parent2 = Arrays.asList(4, 3, 2, 1);

  @Test
  public void givenCrossoverPointOf0ThenParentsAreUnchanged() {
    when(random.nextInt(parent1.size())).thenReturn(0);
    List<List<Integer>> children = onePointCrossover.performCrossover(parent1, parent2);

    assertEquals(children.get(1), parent1);
    assertEquals(children.get(0), parent2);
  }

  @Test
  public void givenCrossoverPointOf2ThenChildrenCross() {
    when(random.nextInt(parent1.size())).thenReturn(2);
    List<List<Integer>> children = onePointCrossover.performCrossover(parent1, parent2);

    assertEquals(children.get(1), Arrays.asList(4, 3, 1, 2));
    assertEquals(children.get(0), Arrays.asList(1, 2, 4, 3));
  }
}
