package org.hightrax.app.security;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.BasicTransformerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("appTokenRepository")
public class TokenRepositoryHbmImpl extends HibernateDaoSupport implements
  PersistentTokenRepository {

  private static final Logger logger = LoggerFactory.getLogger(TokenRepositoryHbmImpl.class);

  @Autowired
  public TokenRepositoryHbmImpl(@Qualifier("securitySessionFactory") SessionFactory sessionFactory){
    setSessionFactory(sessionFactory);
  }

  @Override
  public void createNewToken(final PersistentRememberMeToken token) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {
      @Override
      public Object doInHibernate(Session session) throws HibernateException {
        Query query = session.
          createSQLQuery("INSERT into persistent_logins (username, series, token, last_used) "
            + "VALUES(:username,:series,:token,:last_used)");
        query.setString("username", token.getUsername());
        query.setString("series", token.getSeries());
        query.setString("token", token.getTokenValue());
        query.setDate("last_used", token.getDate());
        return query.executeUpdate();
      }
    });
  }

  @Override
  public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {
      @Override
      public Object doInHibernate(Session session) throws HibernateException {
        Query query = session.createSQLQuery("UPDATE persistent_logins "
          + "SET token = :token, last_used = :last_used WHERE series = :series");
        query.setString("token", tokenValue);
        query.setString("series", series);
        query.setDate("last_used", lastUsed);
        return query.executeUpdate();
      }
    });
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(final String series) {
    PersistentRememberMeToken token = null;

    try {
      token = getHibernateTemplate().execute(new HibernateCallback<PersistentRememberMeToken>() {
        @Override
        public PersistentRememberMeToken doInHibernate(Session session) throws HibernateException {
          SQLQuery query = session.
            createSQLQuery("SELECT username,series,token,last_used "
              + "FROM persistent_logins where series = :series");
          query.setString("series", series);
          query.setResultTransformer(new RememberMeTokenTransformer());
          return (PersistentRememberMeToken) query.uniqueResult();
        }
      });
    } catch (DataAccessException e){
      logger.error("Some error occured while querying remember-me token of series : " + series, e);
    }

    return token;
  }

  @Override
  public void removeUserTokens(final String username) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {
      @Override
      public Object doInHibernate(Session session) throws HibernateException {
        Query query = session.createSQLQuery("DELETE from persistent_logins "
          + "where username = :username");
        query.setString("username", username);
        return query.executeUpdate();
      }
    });
  }

  public static class RememberMeTokenTransformer extends BasicTransformerAdapter {

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
      PersistentRememberMeToken result = null;
      if (tuple.length == 4){
        result = new PersistentRememberMeToken((String) tuple[0], (String) tuple[1],
          (String) tuple[2], (Date) tuple[3]);
      }

      return result;
    }
  }
}
