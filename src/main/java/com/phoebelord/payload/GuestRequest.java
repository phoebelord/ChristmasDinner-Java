package com.phoebelord.payload;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GuestRequest {

  @NotEmpty
  String name;

  List<? extends RelationshipRequest> relationships;

  public String getName() {
    return name;
  }

  public List<? extends RelationshipRequest> getRelationships() {
    return relationships;
  }
}
