package com.spd.trello.configuration;

import com.spd.trello.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditProvider(){
        return new SpringSecurityAuditorAware();
    }
}
