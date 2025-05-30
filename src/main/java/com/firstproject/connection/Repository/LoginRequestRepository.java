package com.firstproject.connection.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.LoginRequest;

@Repository
public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long> {

}
