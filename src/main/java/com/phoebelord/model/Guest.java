package com.phoebelord.model;

import com.phoebelord.payload.GuestRequest;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "config_id"})})
public class Guest implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @NotEmpty(message = "Please provide a unique name for each guest")
  private String name;

  @OneToMany(mappedBy = "owner")
  @OrderBy("id asc")
  private List<Relationship> relationships;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name="config_id", nullable = false)
  private Config config;

  public Guest(String name, int id) {
    this.id = id;
    this.name = name;
  }

  public Guest() {
  }

  public Guest(GuestRequest guestRequest) {
    this.name = guestRequest.getName();
    this.relationships = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<Relationship> relationships) {
    this.relationships = relationships;
  }

  public void addRelationship(Relationship relationship) {
    relationships.add(relationship);
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  public int getRelationshipWith(Guest guest) {
    int value = 0;
    for (Relationship relationship : relationships) {
      if (relationship.getGuestId() == guest.getId()) {
        value = relationship.getLikability();
        break;
      }
    }
    return value;
  }

  public int getRelationshipWith(Guest guest, MaximisationType maximisationType) {
    int value = 0;
    for (Relationship relationship : relationships) {
      if (relationship.getGuestId() == guest.getId()) {
        value = (maximisationType == MaximisationType.HAPPINESS) ? relationship.getLikability() : relationship.getBribe();
        break;
      }
    }
    return value;
  }

  @Override
  public String toString() {
    return "[id:" + id + ", name: " + name + "]";
  }
}
