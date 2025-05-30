package com.firstproject.connection.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstproject.connection.Entity.Expense;
import com.firstproject.connection.Entity.ExpenseResponseDTO;
import com.firstproject.connection.Entity.ExpenseResponseDTO.ExpenseSplitDTO;
import com.firstproject.connection.Entity.ExpenseResponseDTO.GroupDTO;
import com.firstproject.connection.Entity.ExpenseResponseDTO.UserDTO;
import com.firstproject.connection.Entity.ExpenseSplit;
import com.firstproject.connection.Entity.Group;
import com.firstproject.connection.Entity.GroupMember;
import com.firstproject.connection.Entity.User;
import com.firstproject.connection.Repository.ExpenseRepository;
import com.firstproject.connection.Repository.ExpenseSplitRepository;
import com.firstproject.connection.Repository.GroupMemberRepository;
import com.firstproject.connection.Repository.GroupRepository;
import com.firstproject.connection.Repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Transactional
    public ExpenseResponseDTO createExpense(Long groupId, Long paidById, String description, BigDecimal amount) {
        // Validate group and paid by user
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        User paidBy = userRepository.findById(paidById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate that paid by user is a member of the group
        if (!groupMemberRepository.existsByUserAndGroup(paidBy, group)) {
            throw new RuntimeException("User is not a member of the group");
        }

        // Get all active group members
        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);
        if (groupMembers.isEmpty()) {
            throw new RuntimeException("No members found in the group");
        }

        // Create expense
        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense = expenseRepository.save(expense);

        // Calculate split amount per user
        BigDecimal splitAmount = amount.divide(new BigDecimal(groupMembers.size()), 2, RoundingMode.HALF_UP);

        // Create expense splits for all group members
        List<ExpenseSplit> splits = new ArrayList<>();
        for (GroupMember member : groupMembers) {
            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(member.getUser());
            split.setAmount(splitAmount);
            split.setPaid(false);
            splits.add(expenseSplitRepository.save(split));
        }

        return convertToExpenseResponseDTO(expense, splits);
    }

    private ExpenseResponseDTO convertToExpenseResponseDTO(Expense expense, List<ExpenseSplit> splits) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUpdatedAt(expense.getUpdatedAt());

        // Convert paidBy user
        UserDTO paidByDTO = new UserDTO();
        paidByDTO.setId(expense.getPaidBy().getId());
        paidByDTO.setUsername(expense.getPaidBy().getUsername());
        paidByDTO.setEmail(expense.getPaidBy().getEmail());
        paidByDTO.setMobile(expense.getPaidBy().getMobile());
        dto.setPaidBy(paidByDTO);

        // Convert group
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(expense.getGroup().getId());
        groupDTO.setName(expense.getGroup().getName());
        
        UserDTO ownerDTO = new UserDTO();
        ownerDTO.setId(expense.getGroup().getOwner().getId());
        ownerDTO.setUsername(expense.getGroup().getOwner().getUsername());
        ownerDTO.setEmail(expense.getGroup().getOwner().getEmail());
        ownerDTO.setMobile(expense.getGroup().getOwner().getMobile());
        groupDTO.setOwner(ownerDTO);

        UserDTO createdByDTO = new UserDTO();
        createdByDTO.setId(expense.getGroup().getCreatedBy().getId());
        createdByDTO.setUsername(expense.getGroup().getCreatedBy().getUsername());
        createdByDTO.setEmail(expense.getGroup().getCreatedBy().getEmail());
        createdByDTO.setMobile(expense.getGroup().getCreatedBy().getMobile());
        groupDTO.setCreatedBy(createdByDTO);
        
        dto.setGroup(groupDTO);

        // Convert splits
        List<ExpenseSplitDTO> splitDTOs = splits.stream().map(split -> {
            ExpenseSplitDTO splitDTO = new ExpenseSplitDTO();
            splitDTO.setId(split.getId());
            splitDTO.setAmount(split.getAmount());
            splitDTO.setPaid(split.isPaid());

            UserDTO userDTO = new UserDTO();
            userDTO.setId(split.getUser().getId());
            userDTO.setUsername(split.getUser().getUsername());
            userDTO.setEmail(split.getUser().getEmail());
            userDTO.setMobile(split.getUser().getMobile());
            splitDTO.setUser(userDTO);

            return splitDTO;
        }).collect(Collectors.toList());
        dto.setSplits(splitDTOs);

        return dto;
    }

    public List<Expense> getGroupExpenses(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return expenseRepository.findByGroup(group);
    }

    public List<Expense> getExpensesPaidByUser(Long userId) {
        return expenseRepository.findByPaidById(userId);
    }

    public List<ExpenseSplit> getUserExpenseSplits(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseSplitRepository.findByUser(user);
    }

    @Transactional
    public void markExpenseSplitAsPaid(Long expenseSplitId) {
        ExpenseSplit split = expenseSplitRepository.findById(expenseSplitId)
                .orElseThrow(() -> new RuntimeException("Expense split not found"));
        split.setPaid(true);
        expenseSplitRepository.save(split);
    }

    public List<ExpenseSplit> getUnpaidExpenseSplits(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseSplitRepository.findByUserAndIsPaid(user, false);
    }
} 