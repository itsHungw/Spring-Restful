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

package vn.java.springsieutoc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.UserResponseDTO;
import vn.java.springsieutoc.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    // unit test
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody User user) {
        return ApiResponse.created(this.userService.createUser(user));
    }


    @GetMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(@RequestParam(required = false) String role) {
        List<UserResponseDTO> users = this.userService.fetchUsers(role);
        return ApiResponse.success(users, "Successfully retrieved all users");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@Valid @PathVariable int id) {
        UserResponseDTO user = this.userService.transformToDTO(this.userService.findUserById(id));
        return ApiResponse.success(user, "Successfully retrieved user");
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@Valid @RequestBody User user, @PathVariable int id) {
        return ApiResponse.success(this.userService.updateUser(user, id), "Successfully updated user");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id) {
        this.userService.deleteUserById(id);
        return ApiResponse.success("Successfully deleted user");
    }
}
