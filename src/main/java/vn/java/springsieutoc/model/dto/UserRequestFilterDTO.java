package vn.java.springsieutoc.model.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestFilterDTO {

    private String name;
    private String email;
    private String address;
    private String roleName;

}
