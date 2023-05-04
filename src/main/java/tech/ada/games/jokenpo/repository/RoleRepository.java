package tech.ada.games.jokenpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ada.games.jokenpo.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
