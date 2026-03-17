package vn.java.springsieutoc.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.java.springsieutoc.model.Role;
import vn.java.springsieutoc.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(Role role) {
        if (roleRepository.existsRoleByName(role.getName())) {
            throw new ResourceAlreadyExistException("Role with name " + role.getName() + " already exists");
        }
        return this.roleRepository.save(role);
    }

    public Role updateRole(Long id, Role input) {
        Role newRole = findRoleById(id);
        //check role name if role get by ID == role input => update role description and role Name no change
        if(newRole.getName().equals(input.getName())){
            newRole.setDescription(input.getDescription());
            return this.roleRepository.save(newRole);
        }
        //if role input name != newRole name && existed => throw exception
        if (roleRepository.existsRoleByName(input.getName()) ) {
            throw new ResourceAlreadyExistException("Role with name " + input.getName() + " already exists");
        }

        newRole.setName(input.getName());
        newRole.setDescription(input.getDescription());
        return this.roleRepository.save(newRole);
    }

    public void deleteRole(Long id) {
        this.roleRepository.delete(findRoleById(id));

    }

    public Role findRoleById(Long id) {
        return this.roleRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Role not found"));
    }

    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }


}
