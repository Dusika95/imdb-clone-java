package org.imdb.clone.services;

import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.UserLoginResponseDto;
import org.imdb.clone.DTOs.UserRegisterDto;
import org.imdb.clone.models.User;

public interface AuthService {
    UserLoginResponseDto validateUser(UserLoginDto userLoginDTO) throws Exception;

    User registerUser(UserRegisterDto userRegisterDTO) throws Exception;

}
