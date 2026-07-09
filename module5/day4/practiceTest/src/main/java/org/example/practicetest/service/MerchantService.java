package org.example.practicetest.service;

import org.example.practicetest.dto.auth.RegisterRequest;
import org.example.practicetest.entity.Merchant;

import java.util.List;

public interface MerchantService {
    Merchant create(Merchant merchant);

    List<Merchant> getAll();

    Merchant getById(Long id);

    Merchant update(Long id, Merchant merchant);

    void delete(Long id);

    Merchant register(RegisterRequest request);
}

