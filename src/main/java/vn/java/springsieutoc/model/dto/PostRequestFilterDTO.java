package vn.java.springsieutoc.model.dto;

import java.util.List;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostRequestFilterDTO {
    private String title;
    private String content;
    private List<String> tags;
}