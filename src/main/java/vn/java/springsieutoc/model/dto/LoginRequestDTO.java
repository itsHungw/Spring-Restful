package vn.java.springsieutoc.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username do not empty")
    private String username;

    @NotBlank(message = "password do not empty")
    private String password;

}
