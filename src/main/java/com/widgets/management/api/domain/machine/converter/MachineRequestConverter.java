package com.widgets.management.api.domain.machine.converter;

import com.widgets.management.api.domain.machine.Machine;
import io.swagger.model.MachineRequest;
import org.springframework.stereotype.Component;

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
