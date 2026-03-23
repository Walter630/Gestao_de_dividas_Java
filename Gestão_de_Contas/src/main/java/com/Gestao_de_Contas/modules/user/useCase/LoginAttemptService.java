package com.Gestao_de_Contas.modules.user.useCase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    //Maximo de tentativas
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_SECONDS = 60L;
    private static final String PREFIX_USER = "login:user:";
    private static final String PREFIX_IP =  "login:ip:";

    private final RedisTemplate<String, Integer> redisTemplate;

    //login bem sucessido acessa isso
    public void loginSucceeded(String username, String ipAddress) {
        redisTemplate.delete(PREFIX_USER + username);
        redisTemplate.delete(PREFIX_IP + ipAddress);
    }

    //acessa caso login falha
    public void loginFailed(String username, String ipAddress) {
        increment(PREFIX_USER + username);
        increment(PREFIX_IP + ipAddress);
    }

    //verifica se esta bloqueado
    public boolean isBlocked(String username, String ipAddress) {
        return isKeyBlocked(PREFIX_USER + username) || isKeyBlocked(PREFIX_IP + ipAddress);
    }

    // ✅ Consulta quantas tentativas restam
    public int remainingAttempts(String username) {
        Integer count = redisTemplate.opsForValue().get(PREFIX_USER + username);
        return Math.max(0, MAX_ATTEMPTS - (count == null ? 0 : count));
    }

    private void increment(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        // Define o TTL apenas na primeira tentativa
        if (count != null && count == 1) {
            redisTemplate.expire(key, BLOCK_TIME_SECONDS, TimeUnit.SECONDS);
        }
    }

    private boolean isKeyBlocked(String key) {
        Integer count = redisTemplate.opsForValue().get(key);
        return count != null && count >=  MAX_ATTEMPTS;
    }
}
