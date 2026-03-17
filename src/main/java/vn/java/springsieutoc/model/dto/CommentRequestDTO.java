package vn.java.springsieutoc.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    @NotBlank(message = "content không được để trống")
    private String content;


    @Valid
    @NotNull(message = "user không được để trống")
    private InputUser user;

    @Valid
    @NotNull(message = "post không được để trống")
    private InputPost post;


    private boolean isApproved = false;

    private Instant createdAt;

    private Instant updatedAt;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class InputUser{

        @NotNull(message = "id user ko dc de trong")
        private int id;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class InputPost{

        @NotNull(message = "id post ko dc de trong")
        private Long id;
    }
}
