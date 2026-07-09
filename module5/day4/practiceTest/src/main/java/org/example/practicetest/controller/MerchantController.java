package org.example.practicetest.controller;

import jakarta.validation.Valid;
import org.example.practicetest.entity.Merchant;
import org.example.practicetest.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<Merchant> create(@Valid @RequestBody Merchant merchant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(merchantService.create(merchant));
    }

    @GetMapping
    public List<Merchant> getAll() {
        return merchantService.getAll();
    }

    @GetMapping("/{id}")
    public Merchant getById(@PathVariable Long id) {
        return merchantService.getById(id);
    }

    @PutMapping("/{id}")
    public Merchant update(@PathVariable Long id, @Valid @RequestBody Merchant merchant) {
        return merchantService.update(id, merchant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

