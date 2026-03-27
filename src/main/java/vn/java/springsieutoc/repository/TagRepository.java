package vn.java.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.java.springsieutoc.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    boolean existsByName(String name);

    Tag findByNameOrId(String name, Long id);

}
