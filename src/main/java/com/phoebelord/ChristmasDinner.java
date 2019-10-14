package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.model.Person;
import com.phoebelord.model.Seat;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ChristmasDinner {
  private static Person[] people;
  private static Seat[] seats;
  private static int[][] relationships;
  private static int bestSolution;
  private static Person[] solution;
  private static int calls = 0;

  public static void main(String[] args) {
    initialiseFromFile();
    System.out.println("Seats: " + Arrays.toString(seats));
    System.out.println("\nPeople: " + Arrays.toString(people));
    System.out.println("\nRelationships: " + Arrays.toString(relationships));
    getPermutations(10, people);
    System.out.println("End: " + Arrays.toString(people));
    System.out.println("Solution: " + Arrays.toString(solution));
    System.out.println(bestSolution);
    System.out.println(calls);

  }

//  private static void initialise() {
//    people = new Person[10];
//    seats = new Seat[10];
//
//    for(int i = 0; i < 10; i++) {
//      people[i] = new Person(String.valueOf(i), i);
//      seats[i] = new Seat(1, i);
//    }
//
//    for(int i = 0; i < 10; i++) {
//      Seat seat = seats[i];
//      seat.addNeighbour(seats[(((i - 1) % 10) + 10) % 10]);
//      seat.addNeighbour(seats[(i + 1) % 10]);
//    }
//  }

  private static void initialiseFromFile() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      people = mapper.readValue(getResource("people.json"), Person[].class);
      seats = mapper.readValue(getResource("seats.json"), Seat[].class);
      relationships = mapper.readValue(getResource("relationships.json"), int[][].class);

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
        personHappiness += relationships[currentPerson.getNum()][neighbour.getNum()];
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
