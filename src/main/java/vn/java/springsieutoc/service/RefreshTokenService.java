package vn.java.springsieutoc.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.RefreshToken;
import vn.java.springsieutoc.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(RefreshToken refreshToken) {
        this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String refreshToken) {
       return this.refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new ResourceNotFoundException("RefreshToken not found"));
    }

    public void deleteRefreshToken(Long id) {
        this.refreshTokenRepository.deleteById(id);
    }
}
