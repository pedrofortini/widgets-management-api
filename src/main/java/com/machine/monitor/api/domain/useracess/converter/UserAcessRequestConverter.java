package com.machine.monitor.api.domain.useracess.converter;

import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.UnprocessableEntityException;
import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineService;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.domain.user.UserService;
import com.machine.monitor.api.domain.useracess.UserAcess;
import com.machine.monitor.api.domain.useracess.UserAcessId;
import io.swagger.model.UserAcessRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserAcessRequestConverter {

    private MachineService machineService;
    private UserService userService;

    public UserAcessRequestConverter(MachineService machineService, UserService userService){

        this.machineService = machineService;
        this.userService = userService;
    }

    public UserAcess convert(UserAcessRequest userAcessRequest){

        Machine machine = machineService.findMachineById(userAcessRequest.getMachineId());
        User user = userService.findUserByLogin(userAcessRequest.getUserLogin());

        return new UserAcess(user, machine, userAcessRequest.getIsUserAdmin());
    }

    public UserAcessId convertRevokeRequest(String userLogin, Long machineId){

        Machine machine = machineService.findMachineById(machineId);

        if(Objects.nonNull(machine.getMachineAdminUser()) &&
                machine.getMachineAdminUser().getLogin().equals(userLogin)){

            throw new UnprocessableEntityException(String.format(MessageConstants.MESSAGE_REVOKE_ADMIN_USER_ACESS, userLogin,
                    machine.getName()));
        }

        return new UserAcessId(userLogin, machineId);
    }
}
