package vn.java.springsieutoc.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.model.Role;
import vn.java.springsieutoc.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")

public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
        return ApiResponse.success(this.roleService.getAllRoles(),"Get all role successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@Valid  @PathVariable("id") Long id) {

        return ApiResponse.success(this.roleService.findRoleById(id), "Get role successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role role) {
        return ApiResponse.created(this.roleService.createRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable("id") Long id, @Valid @RequestBody Role role) {
        return ApiResponse.success(this.roleService.updateRole(id, role), "Update role successfully");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable("id") Long id) {
        this.roleService.deleteRole(id);
        return ApiResponse.success("Delete role successfully");
    }
}
