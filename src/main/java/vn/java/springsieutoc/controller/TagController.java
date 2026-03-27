package vn.java.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.helper.PageResponse;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.dto.TagRequestFilterDTO;
import vn.java.springsieutoc.model.dto.TagResponseDTO;
import vn.java.springsieutoc.service.TagService;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable, TagRequestFilterDTO filter) {
        Page<TagResponseDTO> tags = this.tagService.findAll(pageable, filter);
        return ApiResponse.success(PageResponse.from(tags), "get all tags successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponseDTO>> findOne(@PathVariable Long id) {
        return ApiResponse.success(this.tagService.convertToTagResponseDTO(this.tagService.findById(id)),
                "get tag successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Tag>> create(@Valid @RequestBody Tag inputTag) {
        return ApiResponse.created(this.tagService.save(inputTag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponseDTO>> update(@PathVariable Long id, @RequestBody Tag inputTag) {
        return ApiResponse.success(this.tagService.convertToTagResponseDTO(this.tagService.update(id, inputTag)),
                "update tag successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return ApiResponse.success("tag delete successfully");
    }
}
