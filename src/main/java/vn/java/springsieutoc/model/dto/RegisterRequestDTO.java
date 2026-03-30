package vn.java.springsieutoc.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Do not left empty")
    private String email;

    @NotBlank(message = "Do not left empty")
    private String password;

    @NotBlank(message = "Do not left empty")
    private String name;

    @NotBlank(message = "Do not left empty")
    private String address;
}
