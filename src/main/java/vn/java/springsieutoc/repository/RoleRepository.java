package vn.java.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.java.springsieutoc.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    public boolean existsRoleByName(String name);

    boolean existsRoleByNameAndId(String name, Long id);
    Optional<Role> findRoleByNameOrId(String name, Long id);
}
