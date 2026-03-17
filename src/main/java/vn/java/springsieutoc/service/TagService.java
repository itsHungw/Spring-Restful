package vn.java.springsieutoc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.dto.TagResponseDTO;
import vn.java.springsieutoc.repository.TagRepository;

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

    public List<TagResponseDTO> findAll() {
        return tagRepository.findAll().stream().map(tag -> convertToTagResponseDTO(tag)).collect(Collectors.toList());
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No tag found with id " + id));
    }

    public Tag save(Tag tag) {
        if (this.tagRepository.existsByName(tag.getName())) {
            throw new ResourceAlreadyExistException("Tag with name " +tag.getName() +" existed");
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
            throw new ResourceAlreadyExistException("Tag with name " +inputTag.getName() +" existed");
        }
        tag.setName(inputTag.getName());
        return tagRepository.save(tag);
    }
}
