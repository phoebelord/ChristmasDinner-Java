package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.dao.*;
import com.phoebelord.model.Role;
import com.phoebelord.model.RoleName;
import com.phoebelord.model.User;
import com.phoebelord.security.JwtTokenProvider;
import com.phoebelord.security.UserPrincipal;
import com.phoebelord.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChristmasDinner.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  AuthenticationManager authenticationManager;

  @MockBean
  UserRepository userRepository;

  @MockBean
  RoleRepository roleRepository;

  @MockBean
  PasswordEncoder passwordEncoder;

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  UserServiceImpl userService;

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
    given(userService.loadUserByUsername("test@email.com")).willReturn(UserPrincipal.create(user));
    given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.of(userRole));
    given(userRepository.save(any(User.class))).willReturn(user);
  }

  @Test
  public void Given_validLogin_When_authenticateUser_Then_isOkay() throws Exception {
    Authentication authentication = new UsernamePasswordAuthenticationToken("test@email.com", "password");
    authentication.setAuthenticated(false);
    given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authentication);
    String login = "{\n" +
      "    \"email\": \"test@email.com\",\n" +
      "    \"password\": \"password\"\n" +
      "}";
    mockMvc.perform(post("/api/auth/signin")
      .content(login)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  public void Given_invalidLogin_When_authenticateUser_Then_isUnauthorised() throws Exception {
    Authentication authentication = new UsernamePasswordAuthenticationToken("test@email.com", "password");
    authentication.setAuthenticated(false);
    given(authenticationManager.authenticate(any(Authentication.class))).willThrow(new BadCredentialsException("Bad Credentials"));
    String login = "{\n" +
      "    \"email\": \"test@email.com\",\n" +
      "    \"password\": \"passworddddd\"\n" +
      "}";
    mockMvc.perform(post("/api/auth/signin")
      .content(login)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  @Test
  public void Given_validSignUp_When_registerUser_Then_isCreated() throws Exception {
    given(userRepository.existsByEmail("test@email.com")).willReturn(false);

    String signUp = "{\n" +
      "    \"email\": \"test@email.com\",\n" +
      "    \"password\": \"password\"\n" +
      "}";
    mockMvc.perform(post("/api/auth/signup")
      .content(signUp)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    verify(userRepository).save(any(User.class));
  }

  @Test
  public void Given_SignUpEmailTaken_When_registerUser_Then_isBadRequest() throws Exception {
    given(userRepository.existsByEmail("test@email.com")).willReturn(true);

    String signUp = "{\n" +
      "    \"email\": \"test@email.com\",\n" +
      "    \"password\": \"password\"\n" +
      "}";
    mockMvc.perform(post("/api/auth/signup")
      .content(signUp)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void Given_invalidSignUpn_When_registerUser_Then_isBadRequest() throws Exception {
    String signUp = "{\n" +
      "    \"email\": \"\",\n" +
      "    \"password\": \"\"\n" +
      "}";
    mockMvc.perform(post("/api/auth/signup")
      .content(signUp)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());

    verify(userRepository, never()).save(any(User.class));
  }
}
