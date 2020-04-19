package ru.sapozhnikov.dao;

import org.springframework.data.repository.CrudRepository;
import ru.sapozhnikov.entity.UserSecurity;

import java.util.Optional;

public interface UserSecurityDAO extends CrudRepository<UserSecurity, Long> {
    Optional<UserSecurity> findByUsername(String username);
}
