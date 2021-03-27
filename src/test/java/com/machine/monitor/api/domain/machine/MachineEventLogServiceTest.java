package com.machine.monitor.api.domain.machine;

import com.machine.monitor.api.infrastructure.persistence.MachineEventLogRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class MachineEventLogServiceTest {

    private MachineEventLogService service;
    private MachineEventLogRepository repository;

    @Before
    public void setUp() {

        this.repository = PowerMockito.mock(MachineEventLogRepository.class);
        this.service = new MachineEventLogService(this.repository);
    }

    @Test
    public void shouldReturnEmptyListIfFindByMachineReturnsNull(){

        Machine machine = new Machine();

        Mockito.when(repository.findByMachine(Mockito.eq(machine)))
                .thenReturn(null);

        List<MachineEventLog> eventLogServiceList = service.findEventsOfMachine(machine);

        assertThat(eventLogServiceList).isEmpty();
    }

    @Test
    public void shouldReturnMachineEventLogListIfFindByMachineReturnsList(){

        Machine machine = new Machine();
        List<MachineEventLog> machineEventLogs = new ArrayList<>();
        machineEventLogs.add(new MachineEventLog());

        Mockito.when(repository.findByMachine(Mockito.eq(machine)))
                .thenReturn(machineEventLogs);

        List<MachineEventLog> eventLogServiceList = service.findEventsOfMachine(machine);

        assertThat(eventLogServiceList).isNotEmpty();
    }
}