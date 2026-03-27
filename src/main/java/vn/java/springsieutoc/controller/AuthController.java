

package vn.java.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import vn.java.springsieutoc.config.JwtService;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.model.RefreshToken;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.ExchangeTokenResponse;
import vn.java.springsieutoc.model.dto.LoginRequestDTO;
import vn.java.springsieutoc.model.dto.LoginResponseDTO;
import vn.java.springsieutoc.model.dto.UserResponseDTO;
import vn.java.springsieutoc.service.RefreshTokenService;
import vn.java.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO userLogin) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        User user = this.userService.findUserByEmail(authentication.getName());

        String accessToken = this.jwtService.createAccessToken(authentication, user.getId());
        String refreshToken = this.jwtService.createRefreshToken(user);

        LoginResponseDTO res =  new LoginResponseDTO();
        res.setAccessToken(accessToken);
        res.setUser(new LoginResponseDTO.UserLogin(user.getId() ,authentication.getName(), this.jwtService.getScope(authentication)));
        res.setRefreshToken(refreshToken);


        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)              // bật nếu bạn dùng HTTPS
                .path("/")                 // cookie gửi mọi request
                .maxAge(refreshTokenExpiration)
                .sameSite("None")          // nếu FE/BE khác domain → cần "None"
                .build();

        ApiResponse<LoginResponseDTO> finalData = new ApiResponse<>(HttpStatus.OK, "", res,  "");
//        log.debug("Create user request: username={}, email={}",
//                res.getUsername(),
//                requestDto.getEmail());


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(finalData); //gan them header
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<ExchangeTokenResponse>> postRefreshToken(@RequestParam String token) {

        return ApiResponse.success(this.jwtService.handleExchangeToken(token));
    }

    @PostMapping("/auth/refresh-cookies")
    public ResponseEntity<?> postRefreshTokenWithCookies(@CookieValue(required = false) String refresh_token) {

        ExchangeTokenResponse tokenResponse = this.jwtService.handleExchangeToken(refresh_token);

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)              // bật nếu bạn dùng HTTPS
                .path("/")                 // cookie gửi mọi request
                .maxAge(refreshTokenExpiration)
                .sameSite("None")          // nếu FE/BE khác domain → cần "None"
                .build();

        ApiResponse<ExchangeTokenResponse> finalData = new ApiResponse<>(HttpStatus.OK, "", tokenResponse,  "");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(finalData); //gan them header
    }

    @GetMapping("/auth/account")
    public ResponseEntity<?> getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String email = jwt.getSubject();
        String role = jwt.getClaim("scope");
        Long id = jwt.getClaim("userId");

        LoginResponseDTO.UserLogin user = new LoginResponseDTO.UserLogin();
        user.setId(id.intValue());
        user.setUsername(email);
        user.setRole(role);

        return ApiResponse.success(user);
    }

    @PostMapping("auth/logout")
    public ResponseEntity<?> postLogout(@CookieValue(required = false) String refresh_token) {
        RefreshToken token = this.refreshTokenService.getRefreshToken(refresh_token);
        this.refreshTokenService.deleteRefreshToken(token.getId());
        ResponseCookie deleteCookies = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)              // bật nếu bạn dùng HTTPS
                .path("/")                 // cookie gửi mọi request
                .maxAge(0)
                .sameSite("None")          // nếu FE/BE khác domain → cần "None"
                .build();

        ApiResponse<String> finalData = new ApiResponse<>(HttpStatus.OK, "", "ok",  "");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookies.toString()).body(finalData); //gan them header
    }
}
