package org.hightrax.app.security;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Repository
@Transactional
public class TokenDaoHbmImpl extends HibernateDaoSupport implements TokenDao {

  private static final Logger logger = LoggerFactory.getLogger(TokenDaoHbmImpl.class);

  @Autowired
  public TokenDaoHbmImpl(SessionFactory sessionFactory){
    setSessionFactory(sessionFactory);
  }

  @Override
  public void createNewToken(UserToken userToken) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {
      @Override
      public Object doInHibernate(Session session)
        throws HibernateException {
        return session.save(userToken);
      }
    });
  }

  @Override
  public void updateToken(UserToken userToken) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {

      @Override
      public Object doInHibernate(Session session)
        throws HibernateException {
        session.update(userToken);
        return userToken;
      }

    });
  }

  @Override
  public UserToken getTokenBySeries(String series) {
    return getHibernateTemplate().get(UserToken.class, series);
  }

  @Override
  public void removeUserTokens(final String username) {
    getHibernateTemplate().execute(new HibernateCallback<Object>() {
      @Override
      public Object doInHibernate(Session session)
        throws HibernateException {
        Criteria criteria = session.createCriteria(UserToken.class);
        criteria.add(Restrictions.eq("username", username));

        Set<UserToken> tokensForDelete = new HashSet<UserToken>(criteria.list());

        tokensForDelete.stream().forEach((UserToken token) -> session.delete(token));
        return null;
      }
    });
  }
}
