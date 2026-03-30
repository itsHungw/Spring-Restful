package vn.java.springsieutoc.model.dto;

import java.time.Instant;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentRequestFilterDTO {
    private String userId;
    private String postId;
    private Boolean isApproved;
    private Instant createdAt;
    private Instant updatedAt;
}
