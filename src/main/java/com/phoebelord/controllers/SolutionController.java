package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.Solution;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SolutionController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public Solution[] getSolution() {
     return new Solution[]{ChristmasDinner.getSolution("data_a", AlgorithmType.Genetic)};
  }
}
