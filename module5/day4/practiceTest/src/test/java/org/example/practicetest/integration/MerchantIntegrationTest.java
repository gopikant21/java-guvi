package org.example.practicetest.integration;

import org.example.practicetest.entity.Merchant;
import org.example.practicetest.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MerchantIntegrationTest {

    @Autowired
    private MerchantRepository merchantRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void registrationFlowShouldStoreEncryptedPassword() {
        Merchant merchant = new Merchant();
        merchant.setName("XYZ");
        merchant.setEmail("integration@xyz.com");
        merchant.setTaxId("AB12CD34EF");
        merchant.setPassword(passwordEncoder.encode("plain-password"));

        Merchant saved = merchantRepository.saveAndFlush(merchant);

        assertTrue(passwordEncoder.matches("plain-password", saved.getPassword()));
    }

    @Test
    void duplicateEmailShouldFailWithConflictPrecondition() {
        merchantRepository.saveAndFlush(merchant("integration@xyz.com"));

        assertThrows(Exception.class, () -> merchantRepository.saveAndFlush(merchant("integration@xyz.com")));
    }

    private Merchant merchant(String email) {
        Merchant merchant = new Merchant();
        merchant.setName("XYZ");
        merchant.setEmail(email);
        merchant.setTaxId("ZX98YU76TR");
        merchant.setPassword("encoded");
        return merchant;
    }
}


