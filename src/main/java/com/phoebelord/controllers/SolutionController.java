package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.algorithms.genetic.crossover.Crossover;
import com.phoebelord.algorithms.genetic.crossover.CrossoverType;
import com.phoebelord.algorithms.genetic.selection.Selection;
import com.phoebelord.algorithms.genetic.selection.SelectionType;
import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.exception.ForbiddenException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Solution;
import com.phoebelord.security.CurrentUser;
import com.phoebelord.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolutionController {

  @Autowired
  ConfigRepository configRepository;
  private Logger log = LoggerFactory.getLogger(SolutionController.class);


  // This is just used for testing
  @GetMapping(value = "/api/solution")
  @ResponseBody
  public Solution[] getSolution(@RequestParam String dataSet,
                                @RequestParam String algorithm,
                                @RequestParam String selection,
                                @RequestParam String crossover) {
    return ChristmasDinner.getSolution(dataSet, AlgorithmType.valueOf(algorithm), SelectionType.valueOf(selection), CrossoverType.valueOf(crossover));
  }



  @GetMapping(value = "/api/solution/{configId}")
  @PreAuthorize("hasRole('USER')")
  @ResponseBody
  public Solution[] getASolution(@CurrentUser UserPrincipal currentUser,
                               @PathVariable int configId,
                               @RequestParam String maximisation,
                               @RequestParam String selection,
                               @RequestParam String crossover) {
    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if (currentUser.getId() != config.getCreatedBy()) {
      log.info(currentUser.getUsername() + " attempted to get the solution to someone else's config");
      throw new ForbiddenException("You do not have access to this config");
    }
    log.info(currentUser.getUsername() + " getting solution for config " + config.getId() + "[Maximisation: " + maximisation + ", Selection: " + selection + ", Crossover: " + crossover +"]");
    return ChristmasDinner.getSolution(config, MaximisationType.valueOf(maximisation), SelectionType.valueOf(selection), CrossoverType.valueOf(crossover));
  }
}
