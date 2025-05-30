package com.firstproject.connection.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ExpenseResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private UserDTO paidBy;
    private GroupDTO group;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ExpenseSplitDTO> splits;

    @Data
    public static class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String mobile;
    }

    @Data
    public static class GroupDTO {
        private Long id;
        private String name;
        private UserDTO owner;
        private UserDTO createdBy;
    }

    @Data
    public static class ExpenseSplitDTO {
        private Long id;
        private UserDTO user;
        private BigDecimal amount;
        private boolean isPaid;
    }
} 