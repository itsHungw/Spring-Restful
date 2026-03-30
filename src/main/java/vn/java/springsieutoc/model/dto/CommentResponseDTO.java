package vn.java.springsieutoc.model.dto;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;

    private String content;

    private OutputUser user;

    private OutputPost post;

    private boolean isApproved = false;

    private Instant createdAt;

    private Instant updatedAt;

    private String postTitle;

    private String userFullName;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputUser {
        private int id;
        private String name;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputPost {
        private Long id;
        private String title;
    }
}
