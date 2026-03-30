/*
 * Author: Hỏi Dân IT - @hoidanit
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.java.springsieutoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.java.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.Role;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.RegisterRequestDTO;
import vn.java.springsieutoc.model.dto.RoleResponseDTO;
import vn.java.springsieutoc.model.dto.UserRequestFilterDTO;
import vn.java.springsieutoc.model.dto.UserResponseDTO;
import vn.java.springsieutoc.repository.RoleRepository;
import vn.java.springsieutoc.repository.UserRepository;
import vn.java.springsieutoc.service.specification.UserSpecification;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // public UserService(UserRepository userRepository, PasswordEncoder
    // passwordEncoder) {
    // this.userRepository = userRepository;
    // this.passwordEncoder = passwordEncoder;
    // }

    public UserResponseDTO transformToDTO(User user) {
        // UserResponseDTO userResponseDTO = new UserResponseDTO();
        // userResponseDTO.setId(user.getId());
        // userResponseDTO.setName(user.getName());
        // userResponseDTO.setEmail(user.getEmail());
        // userResponseDTO.setRole(user.getRole());

        return UserResponseDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
                .role(new RoleResponseDTO(user.getRole().getId(), user.getRole().getName())).address(user.getAddress())
                .build();
    }

    public Page<UserResponseDTO> fetchUsers(Pageable pageable, UserRequestFilterDTO filter) {
        Page<UserResponseDTO> userList = null;

        System.out.println(filter);
        Specification<User> spec = Specification.allOf(
                UserSpecification.hasName(filter),
                UserSpecification.hasEmail(filter),
                UserSpecification.hasAddress(filter),
                UserSpecification.hasRole(filter));
        userList = this.userRepository.findAll(spec, pageable).map(user -> transformToDTO(user));

        return userList;
    }

    public UserResponseDTO createUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistException("Email already exists");
        }
        String hashedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        Role roleInDB = this.roleRepository.findRoleByNameOrId(user.getRole().getName(), user.getRole().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRole(roleInDB);
        this.userRepository.save(user);

        return transformToDTO(user);
    }

    public User findUserById(int id) {

        return this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponseDTO updateUser(User inputUser, int id) {
        User currentUserInDB = this.findUserById(id);

        if (currentUserInDB != null) {
            currentUserInDB.setName(inputUser.getName());
            currentUserInDB.setEmail(inputUser.getEmail());
            currentUserInDB.setAddress(inputUser.getAddress());
            if (inputUser.getPassword() != null) {

                currentUserInDB.setPassword(this.passwordEncoder.encode(inputUser.getPassword()));
            }
            this.userRepository.save(currentUserInDB);

            return transformToDTO(currentUserInDB);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public void deleteUserById(int id) {
        User user = findUserById(id);
        if (user.getEmail().equals("admin@example.com") || user.getEmail().equals("user@example.com")) {
            throw new ResourceNotFoundException("Admin user cannot be deleted");
        }
        this.userRepository.deleteById(id);
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        // check email
        if (this.userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new ResourceAlreadyExistException("Email already exists");
        }
        String hashedPassword = this.passwordEncoder.encode(registerRequestDTO.getPassword());

        user.setPassword(hashedPassword);

        user.setEmail(registerRequestDTO.getEmail());
        user.setAddress(registerRequestDTO.getAddress());
        user.setName(registerRequestDTO.getName());
        user.setRole(this.roleRepository.findRoleByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));

        this.userRepository.save(user);

        return transformToDTO(user);
    }
}
