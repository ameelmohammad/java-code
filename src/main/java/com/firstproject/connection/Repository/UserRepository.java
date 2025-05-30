package com.firstproject.connection.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	    Optional<User> findByEmail(String email);
	    Optional<User> findByMobile(String mobile);
	    Optional<User> findByEmailOrMobile(String email, String mobile);
}
