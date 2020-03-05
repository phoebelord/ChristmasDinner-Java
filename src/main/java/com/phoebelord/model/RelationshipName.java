package com.phoebelord.model;

public enum RelationshipName {
  LIKES("Likes", 1),
  NEUTRAL("Neutral", 0),
  DISLIKES("Dislikes", -1),
  PARTNER("Partner", 10);

  public final String label;
  public final int likability;

  RelationshipName(String label, int likability) {
    this.label = label;
    this.likability = likability;
  }

  public static RelationshipName valueOfLikability(int likability) {
    for(RelationshipName e: values()) {
      if(e.likability == likability) {
        return e;
      }
    }
    return null;
  }
}
