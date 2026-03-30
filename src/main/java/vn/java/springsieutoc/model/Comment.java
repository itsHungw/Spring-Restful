

package vn.java.springsieutoc.model;

import java.time.Instant;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import vn.java.springsieutoc.helper.SecurityUtil;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "content không được để trống")
    private String content;

    @Builder.Default
    private boolean isApproved = false;

    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        SecurityUtil.getCurrentIdLogin().ifPresent(id -> {
            User currentUser = new User();
            currentUser.setId(id);
            this.user = currentUser;
        });
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
        SecurityUtil.getCurrentIdLogin().ifPresent(id -> {
            User currentUser = new User();
            currentUser.setId(id);
            this.user = currentUser;
        });
    }
}