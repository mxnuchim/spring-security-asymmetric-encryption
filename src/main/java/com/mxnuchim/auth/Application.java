package com.mxnuchim.auth;

import com.mxnuchim.auth.domain.entities.Role;
import com.mxnuchim.auth.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(final RoleRepository roleRepository){
        return args -> {
            final Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            if(userRole.isEmpty()){
                final Role role = new Role();
                role.setName("ROLE_USER");
                role.setCreatedBy("APP");
                roleRepository.save(role);
            }
        };
    }

}
