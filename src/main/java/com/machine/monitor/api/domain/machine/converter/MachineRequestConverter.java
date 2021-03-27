package com.machine.monitor.api.domain.machine.converter;

import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.UnprocessableEntityException;
import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.domain.user.UserService;
import io.swagger.model.MachineRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class MachineRequestConverter {

    public Machine convert(MachineRequest request){

        Machine machine = new Machine();

        if(request.getId() != null){
            machine.setId(request.getId());
        }

        machine.setName(request.getName());
        machine.setMachineIsUp(request.getMachineIsUp());
        machine.setIpAddress(request.getIpAddress());

        return  machine;
    }
}
