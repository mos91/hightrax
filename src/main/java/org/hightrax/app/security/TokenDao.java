package org.hightrax.app.security;

public interface TokenDao {
  void createNewToken(UserToken userToken);

  void updateToken(UserToken userToken);

  UserToken getTokenBySeries(String series);

  void removeUserTokens(String username);
}
