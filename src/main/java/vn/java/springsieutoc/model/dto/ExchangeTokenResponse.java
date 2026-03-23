package vn.java.springsieutoc.model.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType =  "Bearer";
    private LoginResponseDTO.UserLogin userLogin;


}
