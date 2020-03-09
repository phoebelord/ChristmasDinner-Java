package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.dao.ConfigRepository;
import com.phoebelord.exception.ForbiddenException;
import com.phoebelord.exception.NotFoundException;
import com.phoebelord.model.Config;
import com.phoebelord.model.MaximisationType;
import com.phoebelord.model.Solution;
import com.phoebelord.security.CurrentUser;
import com.phoebelord.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolutionController {

  @Autowired
  ConfigRepository configRepository;

  @GetMapping(value = "/api/solution")
  @ResponseBody
  public Solution[] getSolution(@RequestParam String dataSet, @RequestParam String algorithmType) {
     return new Solution[]{ChristmasDinner.getSolution(dataSet, AlgorithmType.valueOf(algorithmType))};
  }

  @GetMapping(value = "/api/solution/{configId}")
  @PreAuthorize("hasRole('USER')")
  @ResponseBody
  public Solution getASolution(@CurrentUser UserPrincipal currentUser, @PathVariable int configId, @RequestParam String type) {
    Config config = configRepository.findById(configId).orElseThrow(() -> new NotFoundException("Config", "id", configId));
    if(currentUser.getId() != config.getCreatedBy()) {
      throw new ForbiddenException("You do not have access to this config");
    }
    return ChristmasDinner.getSolution(config, MaximisationType.valueOf(type));
  }
}
