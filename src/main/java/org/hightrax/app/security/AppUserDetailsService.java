package org.hightrax.app.security;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AppUserDetailsService implements UserDetailsService {

  /*
    select username,password,enabled from users where username = ?
    select username,authority from authorities where username = ?
  */

  @Autowired
  @Qualifier("securitySessionFactory")
  private SessionFactory sessionFactory;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails result = null;
    Session session = sessionFactory.openSession();
    try {
      Query query = session.
        createSQLQuery("SELECT username, password, enabled FROM users WHERE username = :username");
      query.setString("username", username);
      query.setMaxResults(1).setResultTransformer(new UserResultTransformer());

      result = (UserDetails) query.uniqueResult();
    } finally {
      session.close();
    }

    if (result == null){
      throw new UsernameNotFoundException("User with name " + username + " not found");
    }

    return result;
  }

  private static class UserResultTransformer implements ResultTransformer {

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
      User result = null;
      if (tuple.length == 3) {
        result =
          new User((String) tuple[0], (String) tuple[1], (Boolean) tuple[2], true, true, true,
            AuthorityUtils.NO_AUTHORITIES);
      }

      return result;
    }

    @Override
    public List transformList(List collection) {
      return null;
    }
  }
}
