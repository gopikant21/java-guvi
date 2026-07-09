package org.example.practicetest.service;

import org.example.practicetest.dto.auth.RegisterRequest;
import org.example.practicetest.entity.Merchant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoopMerchantService implements MerchantService {
    @Override
    public Merchant create(Merchant merchant) {
        return merchant;
    }

    @Override
    public List<Merchant> getAll() {
        return List.of();
    }

    @Override
    public Merchant getById(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Merchant update(Long id, Merchant merchant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Merchant register(RegisterRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

