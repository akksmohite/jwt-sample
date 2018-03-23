package com.kpit.iocl.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kpit.iocl.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

	Authority findByName(String name);
}
