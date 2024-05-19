package io.joaco.mangovaultserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz.requestMatchers("/api/v1/user")
                                                        .permitAll()
                                                        .anyRequest()
                                                        .authenticated())
                   .httpBasic(Customizer.withDefaults())
                   .csrf(AbstractHttpConfigurer::disable)
                   .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsManager service, PasswordEncoder encoder) throws Exception {
        DaoAuthenticationProvider manager = new DaoAuthenticationProvider();

        manager.setPasswordEncoder(encoder);
        manager.setUserDetailsService(service);

        return new ProviderManager(manager);
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, role FROM roles WHERE username = ?");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

}
