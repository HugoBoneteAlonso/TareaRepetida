package org.example.empresa.service;

import org.example.empresa.exception.TooManyRequestsException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginRateLimiter {
    private static final int MAX_ATTEMPTS = 5;
    private final Map<String, Integer> attempts = new HashMap<>();
    private final Map<String, LocalDateTime> blocked =  new HashMap<>();

    public void checkLimit(String ip) {
        LocalDateTime blockedTime = blocked.get(ip);

        if(blockedTime != null && LocalDateTime.now().isBefore(blockedTime)) {
            throw new TooManyRequestsException();
        }
    }

    public void failedAttempt(String ip) {
        int current = attempts.getOrDefault(ip, 0) + 1;
        if (current >= MAX_ATTEMPTS) {
            block(ip);
            reset(ip);
            return;
        }
        attempts.put(ip, current);
    }

    public void reset(String ip) {
        attempts.remove(ip);
    }

    public void block(String ip) {
        blocked.put(ip, LocalDateTime.now().plusMinutes(1));
    }
}
