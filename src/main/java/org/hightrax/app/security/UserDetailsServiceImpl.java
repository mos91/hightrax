package org.hightrax.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("appUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserDao userDao;

  @Autowired
  public UserDetailsServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  private Set<GrantedAuthority> getGrantedAuthorities(Set<UserRole> userRoles){
    Set<GrantedAuthority> result = new HashSet<>();
    for (UserRole ur : userRoles){
      result.add(new SimpleGrantedAuthority(ur.getRole()));
    }

    return result;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    AuthUser user = userDao.getUser(username);
    User result = null;
    if (user != null){
      result = new User(user.getUsername(), user.getPassword(),
        user.isEnabled(), true, true, true, getGrantedAuthorities(user.getUserRoles()));
    }
    return result;
  }

}
