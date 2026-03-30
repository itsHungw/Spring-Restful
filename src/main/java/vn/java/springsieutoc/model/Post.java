

package vn.java.springsieutoc.model;

import java.time.Instant;
import java.util.List;

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
@Table(name = "posts")
public  class Post {
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title không được để trống")
    private String title;

    @NotBlank(message = "content không được để trống")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany()
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;



    @PrePersist
    public void beforeCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        SecurityUtil.getCurrentIdLogin().ifPresent(id -> {
            User user = new User();
            user.setId(id);
            this.user = user;
        });
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = Instant.now();
        SecurityUtil.getCurrentIdLogin().ifPresent(id -> {
            User user = new User();
            user.setId(id);
            this.user = user;
        });
    }
}