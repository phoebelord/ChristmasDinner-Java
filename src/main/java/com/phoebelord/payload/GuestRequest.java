package com.phoebelord.payload;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GuestRequest {

  @NotEmpty
  private String name;

  @OneToMany
  private List<RelationshipRequest> relationships;

  public String getName() {
    return name;
  }

  public List<RelationshipRequest> getRelationships() {
    return relationships;
  }
}
