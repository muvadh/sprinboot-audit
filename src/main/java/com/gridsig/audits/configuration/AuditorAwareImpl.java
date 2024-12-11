package com.gridsig.audits.configuration;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        return Optional.of("current user");
    }

    /*
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        return Optional.of(authentication.getName());
    }
        return Optional.of("anonymousUser");
    */
}
