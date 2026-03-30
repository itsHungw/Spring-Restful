package vn.java.springsieutoc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.dto.TagRequestFilterDTO;
import vn.java.springsieutoc.model.dto.TagResponseDTO;
import vn.java.springsieutoc.repository.TagRepository;
import vn.java.springsieutoc.service.specification.TagSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagResponseDTO convertToTagResponseDTO(Tag tag) {
        return TagResponseDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Page<TagResponseDTO> findAll(Pageable pageable, TagRequestFilterDTO filter) {
        Specification<Tag> spec = Specification.allOf(
                TagSpecification.hasName(filter));
        Page<Tag> tags = this.tagRepository.findAll(spec, pageable);
        return tags.map(tag -> convertToTagResponseDTO(tag));
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No tag found with id " + id));
    }

    public Tag save(Tag tag) {
        if (this.tagRepository.existsByName(tag.getName())) {
            throw new ResourceAlreadyExistException("Tag with name " + tag.getName() + " existed");
        }
        return tagRepository.save(tag);
    }

    public void deleteById(Long id) {
        findById(id);
        tagRepository.deleteById(id);
    }

    public Tag update(Long id, Tag inputTag) {
        Tag tag = findById(id);
        if (tagRepository.existsByName(inputTag.getName())) {
            throw new ResourceAlreadyExistException("Tag with name " + inputTag.getName() + " existed");
        }
        tag.setName(inputTag.getName());
        return tagRepository.save(tag);
    }
}
