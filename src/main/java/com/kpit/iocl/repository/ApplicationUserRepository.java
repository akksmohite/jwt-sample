package com.kpit.iocl.repository;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kpit.iocl.domain.ApplicationUser;

@Transactional
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}