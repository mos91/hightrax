package org.hightrax.app.servlet3;

public enum ApplicationMode {
  PRODUCTION("production"), DEVELOPMENT("development");

  private final String profileName;

  ApplicationMode(String profileName){
    this.profileName = profileName;
  }

  public String getProfileName(){
    return profileName;
  }
}
