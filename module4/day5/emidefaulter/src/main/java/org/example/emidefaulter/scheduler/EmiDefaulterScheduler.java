package org.example.emidefaulter.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmiDefaulterScheduler {

    private final LoanRecoveryService loanRecoveryService;

    @Scheduled(cron = "0 0 0 * * *")
    public void processOverduePayments() {
        log.info("Starting scheduled task: Processing overdue EMI payments");
        try {
            loanRecoveryService.runMissedEmiScheduler();
            log.info("Completed scheduled task: Processing overdue EMI payments");
        } catch (Exception e) {
            log.error("Error in scheduled task: Processing overdue EMI payments", e);
        }
    }
}

