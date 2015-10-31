package org.hightrax.app.security;

public interface UserDao {

  AuthUser getUser(String username);

}
