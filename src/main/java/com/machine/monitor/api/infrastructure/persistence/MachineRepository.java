package com.machine.monitor.api.infrastructure.persistence;

import com.machine.monitor.api.domain.machine.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

}
