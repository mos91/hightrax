package org.hightrax.app.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.Date;

@Repository("appTokenRepository")
public class TokenRepositoryImpl implements PersistentTokenRepository {

  private static final String ERROR_NOT_FOUND_MSG = "User token of series {0} not found!";

  private static final Logger logger = LoggerFactory.getLogger(TokenRepositoryImpl.class);

  private final TokenDao tokenDao;

  @Autowired
  public TokenRepositoryImpl(TokenDao tokenDao){
    this.tokenDao = tokenDao;
  }

  @Override
  public void createNewToken(final PersistentRememberMeToken token) {
    tokenDao.createNewToken(
      new UserToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate()));
  }

  @Override
  public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
    UserToken userToken = tokenDao.getTokenBySeries(series);
    if (userToken != null){
      userToken.setLastUsed(lastUsed);
      userToken.setToken(tokenValue);
      tokenDao.updateToken(userToken);
    } else {
      logger.error(MessageFormat.format(ERROR_NOT_FOUND_MSG, series));
    }
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(final String series) {
    UserToken userToken = tokenDao.getTokenBySeries(series);
    PersistentRememberMeToken result = null;
    if (userToken != null){
      result = new PersistentRememberMeToken(
        userToken.getUsername(), userToken.getSeries(),
        userToken.getToken(), userToken.getLastUsed());
    } else {
      logger.error(MessageFormat.format(ERROR_NOT_FOUND_MSG, series));
    }

    return result;
  }

  @Override
  public void removeUserTokens(final String username) {
    tokenDao.removeUserTokens(username);
  }

}
