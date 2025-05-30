package com.firstproject.connection.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.Expense;
import com.firstproject.connection.Entity.ExpenseSplit;
import com.firstproject.connection.Entity.User;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findByExpense(Expense expense);
    List<ExpenseSplit> findByUser(User user);
    List<ExpenseSplit> findByUserAndIsPaid(User user, boolean isPaid);
}
