package com.firstproject.connection.Entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ExpenseRequestDTO {
    private Long groupId;
    private Long paidById;
    private String description;
    private BigDecimal amount;
} 