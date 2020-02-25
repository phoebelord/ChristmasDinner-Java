package com.phoebelord.payload;

import com.phoebelord.model.Guest;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
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

  public void setName(String name) {
    this.name = name;
  }

  public void setRelationships(List<RelationshipRequest> relationships) {
    this.relationships = relationships;
  }
}
