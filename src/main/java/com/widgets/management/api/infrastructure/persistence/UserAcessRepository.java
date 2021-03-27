package com.widgets.management.api.infrastructure.persistence;

import com.widgets.management.api.domain.machine.Machine;
import com.widgets.management.api.domain.useracess.UserAcess;
import com.widgets.management.api.domain.useracess.UserAcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAcessRepository extends JpaRepository<UserAcess, UserAcessId> {

    List<UserAcess> findByMachineAndIsUserAdminTrue(Machine machine);

    @Modifying
    @Query("delete from UserAcess u where u.id = ?1")
    void deleteById(UserAcessId id);
}
