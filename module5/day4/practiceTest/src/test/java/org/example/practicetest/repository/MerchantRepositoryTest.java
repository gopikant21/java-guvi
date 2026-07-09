package org.example.practicetest.repository;

import jakarta.validation.ConstraintViolationException;
import org.example.practicetest.entity.Merchant;
import org.example.practicetest.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MerchantRepositoryTest {

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
    void shouldPersistMerchantAndFindByEmail() {
        Merchant merchant = merchant("merchant@xyz.com", "AB12CD34EF");
        merchantRepository.saveAndFlush(merchant);

        assertTrue(merchantRepository.findByEmail("merchant@xyz.com").isPresent());
    }

    @Test
    void shouldEnforceUniqueEmailConstraint() {
        merchantRepository.saveAndFlush(merchant("merchant@xyz.com", "AB12CD34EF"));

        assertThrows(Exception.class, () -> merchantRepository.saveAndFlush(merchant("merchant@xyz.com", "ZX98YU76TR")));
    }

    @Test
    void shouldRejectInvalidTaxId() {
        Merchant invalid = merchant("other@xyz.com", "SHORT");

        assertThrows(ConstraintViolationException.class, () -> merchantRepository.saveAndFlush(invalid));
    }

    private Merchant merchant(String email, String taxId) {
        Merchant merchant = new Merchant();
        merchant.setName("XYZ");
        merchant.setEmail(email);
        merchant.setTaxId(taxId);
        merchant.setPassword("encoded");
        return merchant;
    }
}


