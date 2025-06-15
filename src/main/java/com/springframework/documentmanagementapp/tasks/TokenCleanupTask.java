package com.springframework.documentmanagementapp.tasks;

import com.springframework.documentmanagementapp.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {
    private final TokenService tokenService;

    @Autowired
    public TokenCleanupTask(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void deleteExpiredTokens() {
        tokenService.deleteExpiredTokens();
    }
}
