package hr.fer.styletrack.backend.misc;

import hr.fer.styletrack.backend.entities.Role;
import hr.fer.styletrack.backend.repos.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SetupRolesData implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        createRoleIfNotFound("PERSONAL_USER");
        createRoleIfNotFound("ADVERTISER_USER");

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);

        if (role.isPresent()) return role.get();
        else {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
            return newRole;
        }
    }
}
