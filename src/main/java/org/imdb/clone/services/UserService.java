package org.imdb.clone.services;

import org.imdb.clone.DTOs.InternalUserCreateDto;
import org.imdb.clone.DTOs.UserUpdateDto;
import org.imdb.clone.DTOs.UserDto;
import org.imdb.clone.models.User;

public interface UserService {

    User getCurrentUser();

    void createInternalUser(InternalUserCreateDto internalUserCreateDto) throws Exception;

    void deleteUser(Long id) throws Exception;

    UserDto getProfile();

    void updateUser(UserUpdateDto userUpdateDto);

    void deleteProfile();
}
