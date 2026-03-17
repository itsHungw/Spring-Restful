package vn.java.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.Post;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.PostRequestDTO;
import vn.java.springsieutoc.model.dto.PostResponseDTO;
import vn.java.springsieutoc.repository.PostRepository;
import vn.java.springsieutoc.repository.TagRepository;
import vn.java.springsieutoc.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<PostResponseDTO> getAllPosts() {
        return this.postRepository.findAll().stream().map(post -> transformToResponseDTO(post)).collect(Collectors.toList());
    }

    public PostResponseDTO transformToResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .tags(post.getTags().stream().map(tag -> PostResponseDTO.OutputTag.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build())
                        .collect(Collectors.toList()))
                .content(post.getContent())
                .build();
    }

    public Post transformToPost(PostRequestDTO post) {
        User user = this.userRepository.findById(post.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("user not found"));

        return Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .user(user)
                .tags(post.getTags().stream().map(tags -> this.tagRepository.findById(tags.getId()).orElseThrow(() -> new ResourceNotFoundException("tags with id: " + tags.getId() + " not found"))).collect(Collectors.toList()))
                .build();
    }

    public PostResponseDTO createPost(PostRequestDTO inputPost) {
        return this.transformToResponseDTO(this.postRepository.save(transformToPost(inputPost)));
    }

    public PostResponseDTO getPost(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        return transformToResponseDTO(post);
    }

    public PostResponseDTO updatePost(PostRequestDTO inputPost, Long id) {
        Post outPost = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        outPost.setTitle(this.transformToPost(inputPost).getTitle());
        outPost.setContent(this.transformToPost(inputPost).getContent());
        outPost.setTags(this.transformToPost(inputPost).getTags());
        outPost.setUser(this.transformToPost(inputPost).getUser());
        this.postRepository.save(outPost);
        return this.transformToResponseDTO(outPost);
    }

    public void deletePost(Long id) {
        getPost(id);
        this.postRepository.deleteById(id);
    }
}
