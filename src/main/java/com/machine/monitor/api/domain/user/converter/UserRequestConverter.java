package com.machine.monitor.api.domain.user.converter;

import com.machine.monitor.api.domain.user.User;
import io.swagger.model.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestConverter {

    public User convert(UserRequest request){

        User user = new User();

        user.setLogin(request.getLogin());
        user.setName(request.getName());

        return  user;
    }
}
