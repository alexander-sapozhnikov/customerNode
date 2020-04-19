package ru.sapozhnikov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sapozhnikov.dao.UserSecurityDAO;
import ru.sapozhnikov.entity.UserSecurity;
import ru.sapozhnikov.security.JwtResponse;
import ru.sapozhnikov.security.JwtTokenUtil;
import ru.sapozhnikov.security.UserDetailsServiceImpl;

import java.util.Collections;

@Controller
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserSecurityDAO userSecurityDAO;

    @Autowired
    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                       UserDetailsServiceImpl userDetailsServiceImpl,
                                       BCryptPasswordEncoder bCryptPasswordEncoder, UserSecurityDAO userSecurityDAO) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userSecurityDAO = userSecurityDAO;
    }

    @PostMapping("/authenticate")
    public ResponseEntity createAuthenticationToken(@RequestParam String username,
                                                    @RequestParam String password)  {
        UserDetails userDetails;
        try {
            authenticate(username, password);
            userDetails = userDetailsServiceImpl
                    .loadUserByUsername(username);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestParam String username,
                                                    @RequestParam String password) {

        if(userSecurityDAO.findByUsername(username).isPresent()){
            return new ResponseEntity("User with this username exist.", HttpStatus.NOT_ACCEPTABLE);
        }

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUsername(username);
        userSecurity.setPassword(bCryptPasswordEncoder.encode(password));
        userSecurityDAO.save(userSecurity);


        UserDetails userDetails = new User(username, userSecurity.getPassword(), Collections.emptyList());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

}
