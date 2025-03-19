package com.dacnguyen.ecommerce.service.interf;

import com.dacnguyen.ecommerce.dto.general.UserDto;
import com.dacnguyen.ecommerce.dto.request.LoginRequest;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getLoginUser();

    Response getUserInfoAndOrderHistory();
}
