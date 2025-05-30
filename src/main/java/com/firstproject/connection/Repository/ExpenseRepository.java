package com.firstproject.connection.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.Expense;
import com.firstproject.connection.Entity.Group;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
    
    @Query("SELECT e FROM Expense e WHERE e.paidBy.id = :userId")
    List<Expense> findByPaidById(@Param("userId") Long userId);
}
