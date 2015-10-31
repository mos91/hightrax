package org.hightrax.app.security;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_roles",
  uniqueConstraints = @UniqueConstraint(
    columnNames = { "role", "user_id" }))
public class UserRole extends AbstractPersistable<Long> {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AuthUser user;

  @Column(name = "role", nullable = false, length = 32)
  private String role;

  public UserRole() {
  }

  public UserRole(AuthUser user, String role) {
    this.user = user;
    this.role = role;
  }

  public AuthUser getUser() {
    return this.user;
  }

  public void setUser(AuthUser user) {
    this.user = user;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    if (!super.equals(o))
      return false;

    UserRole userRole = (UserRole) o;

    return role.equals(userRole.role);

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + role.hashCode();
    return result;
  }
}
