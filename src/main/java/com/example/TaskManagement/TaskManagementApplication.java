package com.example.TaskManagement;

import com.example.TaskManagement.model.Role;
import com.example.TaskManagement.model.User;
import com.example.TaskManagement.repository.RoleRepository;
import com.example.TaskManagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class TaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}

    // seed some users and roles
    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (roleRepo.count() == 0) {
                Role adminRole = new Role(null, "ADMIN");
                Role userRole = new Role(null, "USER");
                roleRepo.save(adminRole);
                roleRepo.save(userRole);


                if (userRepo.count() == 0) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(encoder.encode("adminpass"));
                    admin.setRoles(Set.of(adminRole));
                    userRepo.save(admin);


                    User user = new User();
                    user.setUsername("user");
                    user.setPassword(encoder.encode("userpass"));
                    user.setRoles(Set.of(userRole));
                    userRepo.save(user);
                }
            }
        };
    }

}
