package com.phoebelord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.algorithms.Algorithm;
import com.phoebelord.algorithms.AlgorithmFactory;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static Logger log = LoggerFactory.getLogger(ChristmasDinner.class);

  public static void main(String[] args) {
    SpringApplication.run(ChristmasDinner.class, args);
  }

  public static Solution getSolution(String filename, AlgorithmType algorithmType) {
    try {
      List<Guest> guests = initialiseFromFile(filename + "/guests.json", Guest.class);
      List<Table> tables = initialiseFromFile(filename + "/tables.json", Table.class);
      log.info("Calculating solution for " + filename);

      Algorithm algorithm = AlgorithmFactory.createAlgorithm(algorithmType, guests, tables, MaximisationType.HAPPINESS);
      long startTime = System.currentTimeMillis();
      Solution solution = algorithm.calculateSolution();
      long endTime = System.currentTimeMillis();

      log.info("Time taken (ms): " + (endTime - startTime));
      log.info("Score: " + solution.getHappinessScore());

      return solution;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Solution getSolution(Config config, MaximisationType maximisationType) {
      List<Guest> guests = config.getGuests();
      List<Table> tables = config.getTables();

      log.info("Calculating solution for Config[ID: " + config.getId() + ", Name: " + config.getName() + ", No. Guests: " + config.getGuests().size() + "]");

      Solution solution;
      if(guests.size() > 0 && tables.size() > 0) {
        Algorithm algorithm = AlgorithmFactory.createAlgorithm(AlgorithmType.Genetic, guests, tables, maximisationType);
        solution = algorithm.calculateSolution();
      } else {
        solution = new Solution();
      }

      log.info("Score: " + solution.getHappinessScore());
      return solution;
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
