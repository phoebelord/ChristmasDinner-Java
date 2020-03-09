package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.dao.*;
import com.phoebelord.model.Config;
import com.phoebelord.model.Role;
import com.phoebelord.model.RoleName;
import com.phoebelord.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChristmasDinner.class)
@AutoConfigureMockMvc
public class SolutionControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserRepository userRepository;

  @MockBean
  ConfigRepository configRepository;

  @Before
  public void setup() {
    User user = createUser(1);
    given(userRepository.findById(1)).willReturn(Optional.of(user));

    User user2 = createUser(2);
    given(userRepository.findById(2)).willReturn(Optional.of(user2));

    Config config = new Config();
    config.setId(1);
    config.setCreatedBy(1);
    config.setGuests(new ArrayList<>());
    config.setTables(new ArrayList<>());
    given(configRepository.findById(1)).willReturn(Optional.of(config));
  }

  @Test
  public void Given_authenticated_When_getSolution_Then_isOkay() throws Exception {
    mockMvc.perform(get("/api/solution/1?type=HAPPINESS")
      .header("Authorization", "Bearer " + getAuthenticationToken(1))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.happinessScore", is(0)));
  }

  @Test
  public void Given_authenticated_When_getSolutionNotOwner_Then_isForbidden() throws Exception {
    mockMvc.perform(get("/api/solution/1?type=HAPPINESS")
      .header("Authorization", "Bearer " + getAuthenticationToken(2))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  public void Given_unauthenticated_When_getSolutionNotOwner_Then_isUnauthorised() throws Exception {
    mockMvc.perform(get("/api/solution/1?type=HAPPINESS")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  private String getAuthenticationToken(int id) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 604800000);

    return Jwts.builder()
      .setSubject(Integer.toString(id))
      .setIssuedAt(new Date())
      .setExpiration(expiryDate)
      .signWith(SignatureAlgorithm.HS512, "JWTSuperSecretKey")
      .compact();
  }

  private User createUser(int id) {
    User user = new User();
    user.setId(id);
    user.setEmail("test" + id + "@email.com");
    user.setPassword("$2y$12$BA6RC1N5aFmVzSEcP0xrMOVnOGlmV7NCWfEJxOJSUc6vA9I88066.");
    Role userRole = new Role();
    userRole.setId(1);
    userRole.setName(RoleName.ROLE_USER);
    user.setRoles(Set.of(userRole));

    return user;
  }
}
