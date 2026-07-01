package org.northernarc.assessment4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@DisplayName("Assessment 4 Advanced Integration Tests")
class MyTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private Customer customer;
    private Account a1;
    private Account a2;
    private Account a3;

    @BeforeEach
    void init() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        customer = new Customer();
        customer.setCustomerName("Rahul Sharma");
        customer.setEmail("rahul@bank.com");
        customer.setPassword(passwordEncoder.encode("password123"));
        customer.setBranch("Chennai");
        customer = customerRepository.save(customer);

        a1 = new Account();
        a1.setAccountNumber("ACC9001");
        a1.setAccountType("SAVINGS");
        a1.setBalance(50000.0);
        a1.setCustomer(customer);

        a2 = new Account();
        a2.setAccountNumber("ACC9002");
        a2.setAccountType("CURRENT");
        a2.setBalance(150000.0);
        a2.setCustomer(customer);

        a3 = new Account();
        a3.setAccountNumber("ACC9003");
        a3.setAccountType("SAVINGS");
        a3.setBalance(150000.0); // tie balance for sorting determinism checks
        a3.setCustomer(customer);

        accountRepository.saveAll(List.of(a1, a2, a3));
    }

    @Nested
    @DisplayName("Authentication/Authorization Edge Cases")
    class AuthEdges {

        @Test
        @DisplayName("Login should fail for wrong password")
        void loginWrongPassword() throws Exception {
            Map<String, String> bad = Map.of(
                    "email", "rahul@bank.com",
                    "password", "wrong-pass"
            );

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bad)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error", containsStringIgnoringCase("authentication failed")));
        }

        @Test
        @DisplayName("Login should fail when payload misses email/password")
        void loginMissingFields() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Tampered JWT should be rejected")
        void tamperedTokenRejected() throws Exception {
            String fake = "eyJhbGciOiJIUzI1NiJ9.abc.def";
            mockMvc.perform(get("/api/accounts")
                            .header("Authorization", "Bearer " + fake))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Domain Integrity and Mapping Traps")
    class MappingAndIntegrity {

        @Test
        @DisplayName("Deleting customer should delete child accounts and transactions")
        void cascadeDeleteFullGraph() {
            Transaction tx1 = new Transaction();
            tx1.setAmount(500.0);
            tx1.setTransactionType("CREDIT");
            tx1.setTransactionDate(LocalDate.now());
            tx1.setAccount(a1);

            Transaction tx2 = new Transaction();
            tx2.setAmount(1000.0);
            tx2.setTransactionType("DEBIT");
            tx2.setTransactionDate(LocalDate.now().minusDays(1));
            tx2.setAccount(a2);

            transactionRepository.saveAll(List.of(tx1, tx2));

            customerRepository.delete(customer);
            customerRepository.flush();

            assertThat(accountRepository.findById("ACC9001")).isEmpty();
            assertThat(accountRepository.findById("ACC9002")).isEmpty();
            assertThat(transactionRepository.findById(tx1.getTransactionId())).isEmpty();
            assertThat(transactionRepository.findById(tx2.getTransactionId())).isEmpty();
        }

        @Test
        @DisplayName("Creating account without customer should fail validation")
        @WithMockUser(roles = "MANAGER")
        void createAccountWithoutCustomer() throws Exception {
            Account invalid = new Account();
            invalid.setAccountNumber("ACC9991");
            invalid.setAccountType("SAVINGS");
            invalid.setBalance(100.0);
            invalid.setCustomer(null);

            mockMvc.perform(post("/api/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Query/Pagination Determinism")
    class QueryDeterminism {

        @Test
        @DisplayName("Pagination should always return balance-desc sorted content")
        @WithMockUser(roles = "USER")
        void paginationSortedDesc() throws Exception {
            mockMvc.perform(get("/api/accounts")
                            .param("page", "0")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].balance", greaterThanOrEqualTo(
                            (Integer) 150000)))
                    .andExpect(jsonPath("$.content[1].balance", greaterThanOrEqualTo(
                            (Integer) 50000)));
        }

        @Test
        @DisplayName("Latest transaction query should return most recent transaction only")
        void latestTransactionRobust() {
            Transaction oldTx = new Transaction();
            oldTx.setAmount(10.0);
            oldTx.setTransactionType("DEBIT");
            oldTx.setTransactionDate(LocalDate.now().minusDays(10));
            oldTx.setAccount(a1);

            Transaction newTx = new Transaction();
            newTx.setAmount(9999.0);
            newTx.setTransactionType("CREDIT");
            newTx.setTransactionDate(LocalDate.now());
            newTx.setAccount(a1);

            transactionRepository.save(oldTx);
            transactionRepository.save(newTx);

            List<Transaction> latest = transactionRepository.findLatestTransaction(
                    org.springframework.data.domain.PageRequest.of(0, 1)
            );

            assertThat(latest).hasSize(1);
            assertThat(latest.get(0).getAmount()).isEqualTo(9999.0);
        }
    }

    @Nested
    @DisplayName("Concurrency/Atomicity")
    class ConcurrencyTests {

        @Test
        @DisplayName("Concurrent balance increments should result in exact final amount")
        void concurrentBalanceIncrease() throws Exception {
            int workers = 10;
            double increment = 100.0;
            ExecutorService pool = Executors.newFixedThreadPool(workers);
            CountDownLatch start = new CountDownLatch(1);
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < workers; i++) {
                futures.add(pool.submit(() -> {
                    start.await();
                    accountRepository.increaseBalance("ACC9001", increment);
                    return null;
                }));
            }

            start.countDown();

            for (Future<?> f : futures) {
                f.get(5, TimeUnit.SECONDS);
            }

            accountRepository.flush();
            Account updated = accountRepository.findById("ACC9001").orElseThrow();

            assertThat(updated.getBalance()).isEqualTo(50000.0 + (workers * increment));

            pool.shutdownNow();
        }
    }

    @Nested
    @DisplayName("Dashboard Contract Validations")
    class DashboardContract {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Dashboard should stay internally consistent with seeded data")
        void dashboardConsistency() throws Exception {
            mockMvc.perform(get("/api/dashboard")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalCustomers").value(1))
                    .andExpect(jsonPath("$.totalAccounts").value(3))
                    .andExpect(jsonPath("$.totalBalance").value(350000.0))
                    .andExpect(jsonPath("$.topBranch").value("Chennai"))
                    .andExpect(jsonPath("$.highestBalanceCustomer").value("Rahul Sharma"));
        }
    }
}
