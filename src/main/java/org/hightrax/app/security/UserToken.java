package org.hightrax.app.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "persistent_logins")
public class UserToken {

  @Column(length = 64)
  private String username;

  @Column(length = 64)
  @Id
  private String series;

  @Column(length = 64)
  private String token;

  @Column(name = "last_used")
  private Date lastUsed;

  public UserToken(){}

  public UserToken(String username, String series, String token, Date lastUsed) {
    this.username = username;
    this.series = series;
    this.token = token;
    this.lastUsed = lastUsed;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getLastUsed() {
    return lastUsed;
  }

  public void setLastUsed(Date lastUsed) {
    this.lastUsed = lastUsed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    UserToken userToken = (UserToken) o;

    if (!username.equals(userToken.username))
      return false;
    if (!series.equals(userToken.series))
      return false;
    if (!token.equals(userToken.token))
      return false;
    return lastUsed.equals(userToken.lastUsed);

  }

  @Override
  public int hashCode() {
    int result = username.hashCode();
    result = 31 * result + series.hashCode();
    result = 31 * result + token.hashCode();
    result = 31 * result + lastUsed.hashCode();
    return result;
  }
}
