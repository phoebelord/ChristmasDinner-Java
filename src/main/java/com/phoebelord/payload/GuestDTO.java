package com.phoebelord.payload;

import java.util.List;

public class GuestDTO extends GuestRequest{

  private int id;

  List<RelationshipDTO> relationships;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<RelationshipDTO> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<RelationshipDTO> relationships) {
    this.relationships = relationships;
  }
}
