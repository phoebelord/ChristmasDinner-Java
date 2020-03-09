package com.phoebelord.payload;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GuestRequest {

  @NotEmpty
  String name;

  List<? extends RelationshipRequest> relationships;

  public GuestRequest(@NotEmpty String name, List<? extends RelationshipRequest> relationships) {
    this.name = name;
    this.relationships = relationships;
  }

  public GuestRequest() {
  }

  public String getName() {
    return name;
  }

  public List<? extends RelationshipRequest> getRelationships() {
    return relationships;
  }
}
