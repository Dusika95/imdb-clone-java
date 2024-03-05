package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.UserLoginResponseDto;
import org.imdb.clone.DTOs.UserRegisterDto;
import org.imdb.clone.models.User;
import org.imdb.clone.models.Role;
import org.imdb.clone.repository.UserRepository;
import org.imdb.clone.security.jwt.JwtUtil;
import org.imdb.clone.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserLoginResponseDto validateUser(UserLoginDto userLoginDTO) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getEmail(),
                            userLoginDTO.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new NullPointerException("Email or password is incorrect");
        }
        User verifiedUser = userRepository.findByEmail(userLoginDTO.getEmail());
        if (verifiedUser == null || !passwordEncoder.matches(userLoginDTO.getPassword(), verifiedUser.getPassword())) {
            throw new NullPointerException("Email or password do not match.");
        } else {
            String token = jwtUtil.generateToken(verifiedUser);

            return new UserLoginResponseDto(verifiedUser.getId(), verifiedUser.getNickName(), verifiedUser.getEmail(), verifiedUser.getRole(), token);
        }
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterDto userRegisterDTO) throws Exception {
        User existingUser = userRepository.findByEmail(userRegisterDTO.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        String hashedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        Role userRole = Role.valueOf("User");
        User user = new User(userRegisterDTO.getNickName(), userRegisterDTO.getEmail(), userRole, hashedPassword);
        userRepository.save(user);
        return user;
    }

}
