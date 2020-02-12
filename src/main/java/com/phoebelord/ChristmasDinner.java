package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.AlgorithmFactory;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.Guest;
import com.phoebelord.model.Seat;
import com.phoebelord.model.Solution;
import com.phoebelord.model.Table;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackageClasses = {
  ChristmasDinner.class,
  Jsr310JpaConverters.class
})
public class ChristmasDinner {

  public static void main(String[] args) {
    SpringApplication.run(ChristmasDinner.class, args);
  }

  public static Solution getSolution(String filename, AlgorithmType algorithmType) {
    try {
      List<Guest> guests = initialiseFromFile(filename + "/guests.json", Guest.class);
      List<Seat> seats = initialiseFromFile(filename + "/seats.json", Seat.class);
      List<Table> tables = initialiseFromFile(filename + "/tables.json", Table.class);
      System.out.println("\nTables: " + tables);
      System.out.println("Seats: " + seats);
      System.out.println("\nGuests: " + guests);

      Algorithm algorithm = AlgorithmFactory.createAlgorithm(algorithmType, guests, seats, tables);
      Solution solution = algorithm.calculateSolution();

      System.out.println("Arrangement: " + solution.getArrangements());
      System.out.println("Score: " + solution.getHappinessScore());

      return solution;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static <T> List<T> initialiseFromFile(String filename, Class<T> clazz) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(getResource("data/" + filename), mapper.getTypeFactory(). constructCollectionType(List.class, clazz));
  }

  private static URL getResource(String filename) throws FileNotFoundException {
    URL resource = ClassLoader.getSystemClassLoader().getResource(filename);
    if(resource != null) {
      return ClassLoader.getSystemClassLoader().getResource(filename);
    } else {
      throw new FileNotFoundException("Resource " + filename + " not found");
    }
  }
}
