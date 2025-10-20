package com.bossj.govyreel.seeders;

import com.bossj.govyreel.entities.Role;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.repositories.RoleRepository;
import com.bossj.govyreel.repositories.UserRepository;
import com.bossj.govyreel.repositories.RoleToUserMappingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleToUserMappingRepository roleToUserMappingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding data...");
        if (userRepository.findByEmail("admin@govyreel.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ADMIN");
                newRole.setDescription("Administrator role");
                return roleRepository.save(newRole);
            });

            User adminUser = new User();
            adminUser.setName("Admin User");
            adminUser.setEmail("admin@govyreel.com");
            adminUser.setPassword(passwordEncoder.encode("password"));
            userRepository.save(adminUser);

            var roleToUserMapping = new com.bossj.govyreel.entities.RoleToUserMapping();
            roleToUserMapping.setUser(adminUser);
            roleToUserMapping.setRole(adminRole);
            roleToUserMappingRepository.save(roleToUserMapping);
        }
    }
}
