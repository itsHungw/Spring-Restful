package vn.java.springsieutoc.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {



    @NotBlank(message = "title không được để trống")
    private String title;

    @NotBlank(message = "content không được để trống")
    private String content;

    @NotNull(message = "user ko đc để trống")
    @Valid
    private InputUser user;

    @NotNull(message = "tags ko đc để trống")
    @Valid
    private List<InputTag> tags;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputTag{
        @NotNull(message = "tag ID không được để trống")
        private Long id;

        @NotBlank(message = "name không được để trống")
        private String name;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputUser{
        @NotNull(message = "user ID không được để trống")
        private int id;
    }
}
