package org.example.emidefaulter.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmiDefaulterScheduler {

    private final LoanRecoveryService loanRecoveryService;

    @Scheduled(cron = "0 0 0 * * *")
    public void processOverduePayments() {
        loanRecoveryService.runMissedEmiScheduler();
    }
}

