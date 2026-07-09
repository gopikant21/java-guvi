package org.example.week5.service;

import org.example.week5.dto.TransactionDTO;
import org.example.week5.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    public List<TransactionDTO> getAllTransactions() {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public TransactionDTO getTransactionById(Long id) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public TransactionDTO recordTransaction(Long accountId, Transaction.TransactionType type,
                                           BigDecimal amount, String description) {
        throw new UnsupportedOperationException("Implementation pending");
    }
}

