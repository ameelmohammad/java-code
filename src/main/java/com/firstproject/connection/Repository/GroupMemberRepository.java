package com.firstproject.connection.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.Group;
import com.firstproject.connection.Entity.GroupMember;
import com.firstproject.connection.Entity.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

	boolean existsByUserAndGroup(User user, Group group);

	List<GroupMember> findByGroup(Group group);

}
