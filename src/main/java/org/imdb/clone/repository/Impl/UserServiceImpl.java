package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.InternalUserCreateDto;
import org.imdb.clone.DTOs.UserUpdateDto;
import org.imdb.clone.DTOs.UserDto;
import org.imdb.clone.Events.MovieRatingRefreshEvent;
import org.imdb.clone.exceptions.AdminDeleteException;
import org.imdb.clone.models.Rating;
import org.imdb.clone.models.User;
import org.imdb.clone.models.Role;
import org.imdb.clone.repository.RatingRepository;
import org.imdb.clone.repository.UserRepository;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RatingRepository ratingRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           RatingRepository ratingRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ratingRepository = ratingRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void createInternalUser(InternalUserCreateDto internalUserCreateDto) throws Exception {
        User existingUser = userRepository.findByEmail(internalUserCreateDto.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        String hashedPassword = passwordEncoder.encode((internalUserCreateDto.getPassword()));
        String roleinString = internalUserCreateDto.getRole().getValue();
        Role role = Role.valueOf(roleinString);

        userRepository.save(new User(internalUserCreateDto.getNickName(), internalUserCreateDto.getEmail(), role, hashedPassword));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("no user on id: " + id));

        if ("Admin".equals(user.getRole().getValue())) {
            throw new AdminDeleteException("Admin users cannot be deleted.");
        }
        List<Rating> ratingsOfUser = ratingRepository.findByUserId(user.getId());
        userRepository.delete(user);

        for (Rating rating : ratingsOfUser) {
            MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, rating.getMovie().getId());
            applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
        }
    }


    @Override
    public UserDto getProfile() {
        User user = getCurrentUser();
        return new UserDto(user.getEmail(), user.getNickName());
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(getCurrentUser().getId()).get();
        user.setEmail(userUpdateDto.getEmail());

        if (userUpdateDto.getPassword() != null && userUpdateDto.getPasswordConfirm() != null) {
            String passwordHash = passwordEncoder.encode(userUpdateDto.getPassword());
            user.setPasswordHash(passwordHash);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteProfile() {
        User user = userRepository.findById(getCurrentUser().getId()).get();
        List<Rating> ratings = ratingRepository.findByUserId(user.getId());
        userRepository.delete(user);
        for (Rating rating : ratings) {

            MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, rating.getMovie().getId());
            applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
