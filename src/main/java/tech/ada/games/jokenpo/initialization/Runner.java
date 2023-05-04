package tech.ada.games.jokenpo.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.repository.RoleRepository;

@Component
@Slf4j
public class Runner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public Runner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            Role savedRole = roleRepository.save(newRole);
            log.info("ROLE_USER adicionado com sucesso");
            return savedRole;
        });
    }
}
