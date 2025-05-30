package com.firstproject.connection.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstproject.connection.Entity.Expense;
import com.firstproject.connection.Entity.ExpenseRequestDTO;
import com.firstproject.connection.Entity.ExpenseResponseDTO;
import com.firstproject.connection.Entity.ExpenseSplit;
import com.firstproject.connection.Service.ExpenseService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseRequestDTO request) {
        try {
            ExpenseResponseDTO response = expenseService.createExpense(
                request.getGroupId(),
                request.getPaidById(),
                request.getDescription(),
                request.getAmount()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroupExpenses(@PathVariable Long groupId) {
        try {
            List<Expense> expenses = expenseService.getGroupExpenses(groupId);
            return ResponseEntity.ok(expenses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paid-by/{userId}")
    public ResponseEntity<?> getExpensesPaidByUser(@PathVariable Long userId) {
        try {
            List<Expense> expenses = expenseService.getExpensesPaidByUser(userId);
            return ResponseEntity.ok(expenses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/splits")
    public ResponseEntity<?> getUserExpenseSplits(@PathVariable Long userId) {
        try {
            List<ExpenseSplit> splits = expenseService.getUserExpenseSplits(userId);
            return ResponseEntity.ok(splits);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/split/{splitId}/mark-paid")
    public ResponseEntity<?> markExpenseSplitAsPaid(@PathVariable Long splitId) {
        try {
            expenseService.markExpenseSplitAsPaid(splitId);
            return ResponseEntity.ok("Expense split marked as paid");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/unpaid-splits")
    public ResponseEntity<?> getUnpaidExpenseSplits(@PathVariable Long userId) {
        try {
            List<ExpenseSplit> splits = expenseService.getUnpaidExpenseSplits(userId);
            return ResponseEntity.ok(splits);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 