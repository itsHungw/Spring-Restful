package vn.java.springsieutoc.model.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Instant createdDate;
    private Instant modifiedDate;
    private String authorName;
    private List<OutputTag> tags;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputTag {
        private Long id;
        private String name;
    }
}
