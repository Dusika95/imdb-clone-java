package org.imdb.clone;

import org.imdb.clone.models.User;
import org.imdb.clone.models.Role;
import org.imdb.clone.repository.UserRepository;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ImdbCloneApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ImdbCloneApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@example.com";
        if (userRepository.findByEmail(email) == null) {
            String nickName = "AdminUser";
            String password = "adminpassword";
            String hashedPassword = passwordEncoder.encode(password);
            Role adminRole = Role.valueOf("Admin");
            User admin = new User(nickName, email, adminRole, hashedPassword);

            userRepository.save(admin);

            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists. Skipping user creation.");
        }
    }
}