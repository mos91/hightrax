package org.hightrax.app.security;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.Collections;

@Entity
@Table(name = "users")
public class AuthUser extends AbstractPersistable<Long> {

  @Column(nullable = false, length = 64)
  private String username;

  @Column(nullable = false, length = 128)
  private String password;

  @Column
  private boolean enabled;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
  private Set<UserRole> userRoles;

  public AuthUser(){}

  public AuthUser(String username, String password, Set<UserRole> userRoles) {
    this.username = username;
    this.password = password;
    this.userRoles = Collections.unmodifiableSet(userRoles);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> userRoles) {
    this.userRoles = Collections.unmodifiableSet(userRoles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    AuthUser authUser = (AuthUser) o;

    return username.equals(authUser.username);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + username.hashCode();
    return result;
  }
}
