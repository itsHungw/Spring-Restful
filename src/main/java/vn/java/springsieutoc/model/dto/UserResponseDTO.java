package vn.java.springsieutoc.model.dto;

import lombok.*;
import vn.java.springsieutoc.model.Comment;
import vn.java.springsieutoc.model.Post;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponseDTO {


    private int id;

    private String name;

    private String email;

//    private String password;

    private String address;

    private RoleResponseDTO role;

    private List<Comment> comments;
    private List<Post> posts;
}
