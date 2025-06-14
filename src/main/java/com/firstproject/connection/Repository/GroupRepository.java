package com.firstproject.connection.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
