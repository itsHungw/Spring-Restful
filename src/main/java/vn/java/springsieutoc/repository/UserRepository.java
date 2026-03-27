
package vn.java.springsieutoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.UserResponseDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByName(String name);

    Optional<User> findByNameAndEmail(String name, String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole_Name(String name);
}
