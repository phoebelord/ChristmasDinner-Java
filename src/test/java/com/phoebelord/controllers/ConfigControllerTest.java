package com.phoebelord.controllers;

import com.phoebelord.ChristmasDinner;
import com.phoebelord.dao.*;
import com.phoebelord.model.Config;
import com.phoebelord.model.Role;
import com.phoebelord.model.RoleName;
import com.phoebelord.model.User;
import com.phoebelord.payload.ConfigDTO;
import com.phoebelord.payload.NewConfigRequest;
import com.phoebelord.security.JwtTokenProvider;
import com.phoebelord.security.UserPrincipal;
import com.phoebelord.service.ConfigService;
import com.phoebelord.service.UserServiceImpl;
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

import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChristmasDinner.class)
@AutoConfigureMockMvc
public class ConfigControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ConfigService configService;

  @MockBean
  ConfigRepository configRepository;

  @MockBean
  UserRepository userRepository;

  @MockBean
  GuestRepository guestRepository;

  @MockBean
  RelationshipRepository relationshipRepository;

  @MockBean
  TableRepository tableRepository;

  @MockBean
  UserServiceImpl userDetailsService;

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @Before
  public void setup() {
    given(jwtTokenProvider.validateToken(anyString())).willReturn(true);

    User user = createUser(1);
    given(userDetailsService.loadUserById(1)).willReturn(UserPrincipal.create(user));
    given(userRepository.findById(1)).willReturn(Optional.of(user));

    User user2 = createUser(2);
    given(userDetailsService.loadUserById(2)).willReturn(UserPrincipal.create(user2));

    Config config = new Config();
    config.setId(1);
    config.setName("Test Config");
    config.setCreatedBy(1);
    config.setGuests(new ArrayList<>());
    config.setTables(new ArrayList<>());
    config.setUpdatedAt(Instant.now());
    given(configRepository.findById(1)).willReturn(Optional.of(config));
    given(configService.createConfig(any(NewConfigRequest.class))).willReturn(config);
    given(configService.editConfig(any(ConfigDTO.class))).willReturn(config);

    List<Config> configs = new ArrayList<>();
    configs.add(config);
    given(configRepository.findAllByCreatedByOrderByUpdatedAt(1)).willReturn(configs);


    ConfigDTO configDTO = new ConfigDTO();
    configDTO.setId(1);
    given(configService.createConfigDTO(config)).willReturn(configDTO);
  }

  @Test
  public void Given_userIsAuthenticated_When_getConfig_Then_isOkay() throws Exception {
    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);
    mockMvc.perform(get("/api/config/1")
      .header("Authorization", "Bearer " + jwt)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(1)));
  }

  @Test
  public void Given_userIsNotAuthenticated_When_getConfig_Then_isUnauthorised() throws Exception {
    mockMvc.perform(get("/api/config/1")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  @Test
  public void Given_userIsAuthenticated_When_getConfigNotOwned_Then_isForbidden() throws Exception {
    String jwt = getAuthenticationToken(2);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(2);
    mockMvc.perform(get("/api/config/1")
      .header("Authorization", "Bearer " + jwt)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  public void Given_userIsNotAuthenticated_When_createConfig_Then_isUnauthorised() throws Exception {
    String config = "{\n" +
      "    \"name\": \"Test Config\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    mockMvc.perform(post("/api/config/create")
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());

    verify(configRepository, never()).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_createConfig_Then_isCreated() throws Exception {
    String config = "{\n" +
      "    \"name\": \"Test Config\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);

    mockMvc.perform(post("/api/config/create")
      .header("Authorization", "Bearer " + jwt)
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    verify(configRepository).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_createInvalidConfig_Then_isBadRequest() throws Exception {
    String config = "{\n" +
      "    \"name\": \"\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);

    mockMvc.perform(post("/api/config/create")
      .header("Authorization", "Bearer " + jwt)
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());

    verify(configRepository, never()).save(any(Config.class));
  }

  @Test
  public void Given_userIsNotAuthenticated_When_editConfig_Then_isUnauthorised() throws Exception {
    String config = "{\n" +
      "    \"id\": \"1\",\n" +
      "    \"name\": \"Test Config edited\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    mockMvc.perform(post("/api/config/edit")
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());

    verify(configRepository, never()).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_editConfig_Then_isCreated() throws Exception {
    String config = "{\n" +
      "    \"id\": \"1\",\n" +
      "    \"name\": \"Test Config edited\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);

    mockMvc.perform(post("/api/config/edit")
      .header("Authorization", "Bearer " + jwt)
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    verify(configRepository).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_editConfigNotOwner_Then_isForbidden() throws Exception {
    String config = "{\n" +
      "    \"id\": \"1\",\n" +
      "    \"name\": \"Test Config edited\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    String jwt = getAuthenticationToken(2);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(2);

    mockMvc.perform(post("/api/config/edit")
      .header("Authorization", "Bearer " + jwt)
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());

    verify(configRepository, never()).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_editInvalidConfig_Then_isBadRequest() throws Exception {
    String config = "{\n" +
      "    \"id\": \"1\",\n" +
      "    \"name\": \"\",\n" +
      "    \"guests\": [],\n" +
      "    \"tables\": []\n" +
      "}";

    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);

    mockMvc.perform(post("/api/config/edit")
      .header("Authorization", "Bearer " + jwt)
      .content(config)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());

    verify(configRepository, never()).save(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_deleteConfig_Then_isNoContent() throws Exception {
    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);

    mockMvc.perform(delete("/api/config/delete/1")
      .header("Authorization", "Bearer " + jwt)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    verify(configRepository).delete(any(Config.class));
  }

  @Test
  public void Given_userIsNotAuthenticated_When_deleteConfig_Then_isUnauthorised() throws Exception {
    mockMvc.perform(delete("/api/config/delete/1")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());

    verify(configRepository, never()).delete(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_deleteConfigNotOwner_Then_isForbidden() throws Exception {
    String jwt = getAuthenticationToken(2);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(2);

    mockMvc.perform(delete("/api/config/delete/1")
      .header("Authorization", "Bearer " + jwt)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());

    verify(configRepository, never()).delete(any(Config.class));
  }

  @Test
  public void Given_userIsAuthenticated_When_getAllConfigs_Then_isOkay() throws Exception {
    String jwt = getAuthenticationToken(1);
    given(jwtTokenProvider.getUserIdFromJWT(jwt)).willReturn(1);
    mockMvc.perform(get("/api/config/all")
      .header("Authorization", "Bearer " + jwt)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(1)));
  }

  @Test
  public void Given_userIsNotAuthenticated_When_getAllConfig_Then_isUnauthorised() throws Exception {
    mockMvc.perform(get("/api/config/all")
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
