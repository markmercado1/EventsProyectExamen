package com.emm.authservice.repository;

import com.emm.authservice.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {

    Optional<AuthUser> findByUserName(String userName);

}
