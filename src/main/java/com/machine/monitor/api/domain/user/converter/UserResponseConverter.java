package com.machine.monitor.api.domain.user.converter;

import com.machine.monitor.api.domain.user.User;
import io.swagger.model.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserResponseConverter {

    public List<UserResponse> convertUserList(List<User> users){

        return users.stream().map(u -> convert(u)).collect(Collectors.toList());
    }

    public UserResponse convert(User user){

        UserResponse userTemplate = new UserResponse();
        userTemplate.setLogin(user.getLogin());
        userTemplate.setName(user.getName());

        List<String> machinesAcess = user.getMachines().stream().map(m -> m.getMachine().getName()).collect(Collectors.toList());
        userTemplate.setMachinesAcess(machinesAcess);

        return userTemplate;
    }
}
