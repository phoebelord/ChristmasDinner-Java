package com.phoebelord.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoebelord.ChristmasDinner;
import com.phoebelord.algorithms.AlgorithmType;
import com.phoebelord.model.Solution;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SolutionController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public String getSolution() {
    try {
      Solution solution = ChristmasDinner.getSolution("data_a", AlgorithmType.Genetic);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(solution);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "";
    }

  }
}
