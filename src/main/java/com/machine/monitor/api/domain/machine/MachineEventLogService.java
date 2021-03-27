package com.machine.monitor.api.domain.machine;

import com.machine.monitor.api.infrastructure.persistence.MachineEventLogRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MachineEventLogService {

    private MachineEventLogRepository repository;

    public MachineEventLogService(MachineEventLogRepository repository){

        this.repository = repository;
    }

    public List<MachineEventLog> findEventsOfMachine(Machine machine){

        return Optional.ofNullable(repository.findByMachine(machine)).orElse(new ArrayList<>());
    }
}
