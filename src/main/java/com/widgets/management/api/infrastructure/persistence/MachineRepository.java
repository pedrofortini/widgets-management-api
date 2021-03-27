package com.widgets.management.api.infrastructure.persistence;

import com.widgets.management.api.domain.machine.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

}
