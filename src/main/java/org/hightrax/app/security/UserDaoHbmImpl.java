package org.hightrax.app.security;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDaoHbmImpl extends HibernateDaoSupport implements UserDao {

  @Autowired
  public UserDaoHbmImpl(SessionFactory sessionFactory) {
    setSessionFactory(sessionFactory);
  }

  @Override
  public AuthUser getUser(final String username) {
    AuthUser result = null;
    try {
      result = getHibernateTemplate().execute(new HibernateCallback<AuthUser>() {
        @Override
        public AuthUser doInHibernate(Session session) throws HibernateException {
          Query query = session.createQuery("from AuthUser where username=:username");
          query.setString("username", username);
          return (AuthUser) query.uniqueResult();
        }
      });
    } catch (DataAccessException e) {
      logger.error("Some error occured while querying user with username" + username, e);
    }

    if (result == null) {
      throw new UsernameNotFoundException("User with name " + username + " not found");
    }

    return result;
  }
}
