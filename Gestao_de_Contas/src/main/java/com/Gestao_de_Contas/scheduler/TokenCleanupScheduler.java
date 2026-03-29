package com.Gestao_de_Contas.scheduler;

import com.Gestao_de_Contas.modules.user.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenCleanupScheduler {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * *")//toda meia noite
    @Transactional
    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
