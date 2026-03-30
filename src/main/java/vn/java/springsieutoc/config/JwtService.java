package vn.java.springsieutoc.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.RefreshToken;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.ExchangeTokenResponse;
import vn.java.springsieutoc.model.dto.LoginResponseDTO;
import vn.java.springsieutoc.repository.RefreshTokenRepository;
import vn.java.springsieutoc.service.RefreshTokenService;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    private final JwtEncoder jwtEncoder;

    @Value("${spring.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    private final RefreshTokenService  refreshTokenService;


    public String getScope(Authentication authentication) {
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
        }
        return "UNKNOW";
    }

    public String createAccessToken(Authentication authentication, int userId) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("scope", this.getScope(authentication))
                .claim("userId", userId)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public String generateSecureToken() {
        byte[] randomBytes = new byte[64]; // 512 bits
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }


    public String createRefreshToken(User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        String token = this.generateSecureToken();
         RefreshToken refreshToken = new RefreshToken();
         refreshToken.setCreatedAt(now);
         refreshToken.setExpiredAt(validity);
         refreshToken.setToken(token);
         refreshToken.setUser(user);

         this.refreshTokenService.createRefreshToken(refreshToken);
        return token;
    }

    public ExchangeTokenResponse handleExchangeToken(String currentToken) {

        //find token
       RefreshToken currentRefreshTk = this.refreshTokenService.getRefreshToken(currentToken);

       //validate
        Instant now = Instant.now();
        if (now.isAfter(currentRefreshTk.getExpiredAt())) {
            throw new ResourceNotFoundException("Refresh token has expired");
        }

        //create new token
        User currentUser = currentRefreshTk.getUser();
        String newRefreshToken = this.createRefreshToken(currentUser);

        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        String scope = "ROLE_" +currentUser.getRole();

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(currentUser.getEmail())
                .claim("scope", scope)
                .claim("userId", currentUser.getId())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        //build response
        ExchangeTokenResponse exchangeTk = new ExchangeTokenResponse();
        exchangeTk.setAccessToken(accessToken);
        exchangeTk.setRefreshToken(newRefreshToken);
        exchangeTk.setUserLogin(new LoginResponseDTO.UserLogin(currentUser.getId(), currentUser.getName(), currentUser.getRole().getName()));

        //delete old token
        this.refreshTokenService.deleteRefreshToken(currentRefreshTk.getId());
        return exchangeTk;
    }
}
