package org.example.week5.service;

import org.example.week5.dto.AccountDTO;
import org.example.week5.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    public AccountDTO createAccount(AccountDTO accountDTO) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public List<AccountDTO> getAllAccounts() {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public AccountDTO getAccountById(Long id) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public void deleteAccount(Long id) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public void deposit(Long accountId, BigDecimal amount, String description) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public void withdraw(Long accountId, BigDecimal amount, String description) {
        throw new UnsupportedOperationException("Implementation pending");
    }

    public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, String description) {
        throw new UnsupportedOperationException("Implementation pending");
    }
}

