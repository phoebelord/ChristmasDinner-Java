package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.dao.*;
import com.phoebelord.model.Role;
import com.phoebelord.model.RoleName;
import com.phoebelord.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserRepository userRepository;

  @Before
  public void setup() {
    User user = new User();
    user.setId(1);
    user.setEmail("test@email.com");
    user.setPassword("$2y$12$BA6RC1N5aFmVzSEcP0xrMOVnOGlmV7NCWfEJxOJSUc6vA9I88066.");
    Role userRole = new Role();
    userRole.setId(1);
    userRole.setName(RoleName.ROLE_USER);
    user.setRoles(Set.of(userRole));
    given(userRepository.findById(1)).willReturn(Optional.of(user));
  }

  @Test
  public void Given_email_When_emailInUse_Then_isOkay() throws Exception {
    given(userRepository.existsByEmail("test@email.com")).willReturn(true);
    mockMvc.perform(get("/api/user/checkEmailAvailability?email=test@email.com")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.available", is(false)));
  }

  @Test
  public void Given_email_When_emailNotInUse_Then_isOkay() throws Exception {
    given(userRepository.existsByEmail("test@email.com")).willReturn(false);
    mockMvc.perform(get("/api/user/checkEmailAvailability?email=test@email.com")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.available", is(true)));
  }

  @Test
  public void Given_userAuthorised_When_getCurrentUser_Then_isOkay() throws Exception {
    mockMvc.perform(get("/api/user/me")
      .header("Authorization", "Bearer " + getAuthenticationToken(1))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.email", is("test@email.com")));
  }

  @Test
  public void Given_userNotAuthorised_When_getCurrentUser_Then_isUnauthorised() throws Exception {
    mockMvc.perform(get("/api/user/me")
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
}
