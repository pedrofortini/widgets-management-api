package com.machine.monitor.api.infrastructure.persistence;

import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineEventLogRepository extends JpaRepository<MachineEventLog, Long> {

    List<MachineEventLog> findByMachine(Machine machine);
}
