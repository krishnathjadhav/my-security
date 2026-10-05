package com.security.demo.config;

import com.security.demo.exceptionhandling.CustomAccessDeniedHandler;
import com.security.demo.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@Profile("prode")
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       http.cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                   @Override
                   public @Nullable CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                       CorsConfiguration config = new CorsConfiguration();
                       config.setAllowedOrigins(java.util.Collections.singletonList("http://localhost:4200"));
                       config.setAllowedMethods(java.util.Collections.singletonList("*"));
                       config.setAllowCredentials(true);
                       config.setAllowedHeaders(java.util.Collections.singletonList("*"));
                       config.setMaxAge(3600L);
                       return config;
                   }
               }))
               .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
               .redirectToHttps(https->https.requestMatchers(AnyRequestMatcher.INSTANCE)) // Only HTTPS
               .csrf(csrfConfig -> csrfConfig.disable())
               .authorizeHttpRequests((requests) -> requests
                       .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards", "/user").authenticated()
                       .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll());
         http.formLogin(Customizer.withDefaults());

        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc->ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
