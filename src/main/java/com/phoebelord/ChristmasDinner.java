package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import org.apache.commons.lang3.SerializationUtils;

import java.net.URL;
import java.util.Arrays;

public class ChristmasDinner {
  private static Person[] people;
  private static Seat[] seats;
  private static int bestSolution;
  private static Person[] solution;
  private static int calls = 0;

  public static void main(String[] args) {
    initialiseFromFile("data_b");
    System.out.println("Seats: " + Arrays.toString(seats));
    System.out.println("\nPeople: " + Arrays.toString(people));
    getPermutations(people.length, people);
    System.out.println("Solution: " + Arrays.toString(solution));
    System.out.println(bestSolution);
    System.out.println(calls);

  }

  private static void initialiseFromFile(String dataSet) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      people = mapper.readValue(getResource(dataSet + "/people.json"), Person[].class);
      seats = mapper.readValue(getResource(dataSet + "/seats.json"), Seat[].class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int calculateHappiness(Person[] people) {
    calls++;
    int total = 0;
    for(int i = 0 ; i < 10; i++) {
      int personHappiness = 0;
      Person currentPerson = people[i];
      int[] neighbouringSeats = seats[i].getNeighbours();
      for(int neighbouringSeat: neighbouringSeats) {
        Person neighbour = people[neighbouringSeat];
        personHappiness += currentPerson.getRelationshipWith(neighbour);
      }
      total += personHappiness;
    }
    return total;
  }

  private static void getPermutations(int n, Person[] elements) {
    if(n == 1) {
      int solutionHappiness = calculateHappiness(elements);
      if(solutionHappiness > bestSolution) {
        bestSolution = solutionHappiness;
        solution = SerializationUtils.clone(elements);
      }
    } else {
      for(int i = 0; i < n-1; i++) {
        getPermutations(n - 1, elements);
        if(n % 2 == 0) {
          swap(elements, i, n - 1);
        } else {
          swap(elements, 0, n - 1);
        }
      }
      getPermutations(n - 1, elements);
    }
  }

  private static <T> void swap(T[] input, int a, int b) {
    T tmp = input[a];
    input[a] = input[b];
    input[b] = tmp;
  }

  private static URL getResource(String filename) {
    return ClassLoader.getSystemClassLoader().getResource(filename);
  }


}
