package com.phoebelord.payload;

public class RelationshipDTO extends RelationshipRequest {

  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setGuestName(String guestName){
    this.guestName = guestName;
  }

  public void setLikability(int likability){
    this.likability = likability;
  }

  public void setBribe(int bribe){
    this.bribe = bribe;
  }
}
