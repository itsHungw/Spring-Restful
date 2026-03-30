package vn.java.springsieutoc.config;

import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import vn.java.springsieutoc.helper.exception.CustomAccessDeniedHandler;
import vn.java.springsieutoc.helper.exception.CustomAuthenticationEntryPoint;
import vn.java.springsieutoc.service.UserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${spring.jwt.base64-secret}")
    private String jwtKey;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(dao);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                "http://127.0.0.1:3000", "http://localhost:4173", "http://localhost:5173",
                "https://yourdomain.com"));

        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-Type", "Cache-Control"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            CustomAccessDeniedHandler customAccessDeniedHandler,
                                            CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
            throws Exception {
        // @formatter:off
        String[] WHITELIST = {
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/auth/login", "/auth/refresh", "/auth/refresh-with-cookie",
                "/auth/register",

        };

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(
                (requests) ->
                        requests.requestMatchers(WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.GET, "/posts/**", "/comments").permitAll()
//						.requestMatchers("/users").hasRole("USER")
                                .anyRequest().authenticated());
        http.csrf(c -> c.disable());

        http.formLogin(form -> form.disable());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http.sessionManagement(
                sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on
        return http.build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JwtService.JWT_ALGORITHM).build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthoritiesClaimName("scope");
        scopeConverter.setAuthorityPrefix(""); // giữ nguyên, không thêm "SCOPE_"

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(scopeConverter);
        return converter;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();

        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JwtService.JWT_ALGORITHM.getName());
    }

}