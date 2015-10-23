package org.hightrax.app.security;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class AppUserDetailsService extends HibernateDaoSupport implements UserDetailsService, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

  private static final int USER_COLUMNS_COUNT = 4;

  private SessionFactory sessionFactory;

  @Autowired
  public AppUserDetailsService(@Qualifier("securitySessionFactory") SessionFactory sessionFactory){
    setSessionFactory(sessionFactory);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    UserDetails result = null;
    try {
      result = getHibernateTemplate().execute(new HibernateCallback<UserDetails>() {
        @Override
        public UserDetails doInHibernate(Session session) throws HibernateException {
          Query query = session.
            createSQLQuery(
              "SELECT username, password, enabled, authorities FROM users WHERE username = :username");
          query.setString("username", username);
          query.setMaxResults(1).setResultTransformer(new UserResultTransformer());
          return (UserDetails) query.uniqueResult();
        }
      });
    } catch (DataAccessException e) {
      logger.error("Some error occured while quering user with username" + username, e);
    }

    if (result == null) {
      throw new UsernameNotFoundException("User with name " + username + " not found");
    }

    return result;
  }

  private static class UserResultTransformer implements ResultTransformer {

    private Collection<GrantedAuthority> getGrantedAuthorities(Object value){
      String[] roles = ((String) value).split(",");
      Collection<GrantedAuthority> authorityCollection = new HashSet<GrantedAuthority>();
      for (String role : roles){
        authorityCollection.add(new SimpleGrantedAuthority(role));
      }

      return authorityCollection;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
      User result = null;
      if (tuple.length == USER_COLUMNS_COUNT) {
        result =
          new User((String) tuple[0], (String) tuple[1], (Boolean) tuple[2], true, true, true,
            getGrantedAuthorities(tuple[3]));
      }

      return result;
    }

    @Override
    public List transformList(List collection) {
      return collection;
    }
  }
}
