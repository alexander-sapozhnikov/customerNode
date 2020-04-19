package ru.sapozhnikov.security;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sapozhnikov.dao.UserSecurityDAO;
import ru.sapozhnikov.entity.UserSecurity;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserSecurityDAO userSecurityDAO;

    public UserDetailsServiceImpl(UserSecurityDAO userSecurityDAO) {
        this.userSecurityDAO = userSecurityDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  throws  UsernameNotFoundException{
        Optional<UserSecurity> optional = userSecurityDAO.findByUsername(username);
        if(!optional.isPresent()){
            throw new UsernameNotFoundException("User don't exist.");
        }
        UserSecurity userSecurity = optional.get();
        return new User(userSecurity.getUsername(), userSecurity.getPassword(), Collections.emptyList());
    }
}
