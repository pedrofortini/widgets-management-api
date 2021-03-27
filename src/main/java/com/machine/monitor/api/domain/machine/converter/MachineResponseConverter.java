package com.machine.monitor.api.domain.machine.converter;

import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineEventLog;
import io.swagger.model.MachineDetailResponse;
import io.swagger.model.MachineEventLogResponse;
import io.swagger.model.MachineResponse;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MachineResponseConverter {

    public List<MachineResponse> convertListMachines(List<Machine> machines){

        return machines.stream().map(m -> convertMachine(m)).collect(Collectors.toList());
    }

    public MachineDetailResponse convertToDetail(Machine machine, List<MachineEventLog> eventLogs){

        MachineDetailResponse detailResponse = new MachineDetailResponse();

        detailResponse.setId(machine.getId());

        if(Objects.nonNull(machine.getMachineAdminUser())) detailResponse.setAdminUser(machine.getMachineAdminUser().getLogin());

        detailResponse.setLastDowntime(convertDateToString(machine.getLastDownTime(), "Never"));
        detailResponse.setMachineIsUp(machine.isMachineIsUp());
        detailResponse.setIpAddress(machine.getIpAddress());
        detailResponse.setName(machine.getName());

        List<MachineEventLogResponse> eventLogResponses = eventLogs.stream()
                                                                    .map(e -> convert(e))
                                                                    .collect(Collectors.toList());
        detailResponse.setMachineEventsLog(eventLogResponses);

        List<String> userAcess = machine.getUsers().stream().map(u -> u.getUser().getLogin()).collect(Collectors.toList());
        detailResponse.setUsersAcess(userAcess);

        return detailResponse;
    }

    public MachineEventLogResponse convert(MachineEventLog eventLog){

        MachineEventLogResponse eventLogResponse = new MachineEventLogResponse();
        eventLogResponse.setType(eventLog.getType().name());
        eventLogResponse.setTimeStamp(convertDateToString(eventLog.getTimeStamp(), ""));

        return eventLogResponse;
    }

    private MachineResponse convertMachine(Machine machine){

        MachineResponse machineResponse = new MachineResponse();
        machineResponse.setId(machine.getId());

        if(Objects.nonNull(machine.getMachineAdminUser())) machineResponse.setAdminUser(machine.getMachineAdminUser().getLogin());

        machineResponse.setName(machine.getName());
        machineResponse.setMachineIsUp(machine.isMachineIsUp());
        machineResponse.setIpAddress(machine.getIpAddress());
        machineResponse.setLastDowntime(convertDateToString(machine.getLastDownTime(), "Never"));

        return machineResponse;
    }

    private String convertDateToString(Date date, String defaultValue){

        String ret = defaultValue;

        if(date != null){

            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            ret = dateFormat.format(date);
        }

        return ret;
    }
}
