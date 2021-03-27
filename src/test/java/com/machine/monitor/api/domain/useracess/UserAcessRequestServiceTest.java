package com.machine.monitor.api.domain.useracess;

import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.PersistenceException;
import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineEventLog;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.infrastructure.persistence.UserAcessRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class UserAcessRequestServiceTest {

    private UserAcessRequestService service;
    private UserAcessRepository repository;

    @Before
    public void setUp() {

        this.repository = PowerMockito.mock(UserAcessRepository.class);

        this.service = new UserAcessRequestService(this.repository);
    }

    @Test
    public void shouldCallMethodSaveWithPreviousAdminUserIfMachineAlreadyHadAndAdminUserWhenRequestingUserAcess(){

        User user = new User();
        Machine machine = new Machine();
        UserAcess userAcessSave = new UserAcess(user, machine, true);

        UserAcess userAcessAdminUser = new UserAcess(user, machine, true);
        List<UserAcess> users = new ArrayList<>();
        users.add(userAcessAdminUser);

        Mockito.when(repository.findByMachineAndIsUserAdminTrue(Mockito.eq(machine)))
                .thenReturn(users);
        Mockito.when(repository.save(Mockito.any(UserAcess.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        UserAcess userAcess = service.requestUserAcess(userAcessSave);
        Mockito.verify(repository, times(2)).save(Mockito.eq(userAcessAdminUser));
        assertThat(userAcess.getMachine()).isEqualTo(machine);
        assertThat(userAcess.getUser()).isEqualTo(user);
        assertThat(userAcess.isUserAdmin()).isTrue();
    }

    @Test
    public void shouldThrowPersistenceExceptionWhenProblemPersistingUserAcess(){

        PowerMockito.when(repository.save(Mockito.any(UserAcess.class))).thenThrow(new RuntimeException());

        User user = new User();
        user.setLogin("pedrofortini");
        Machine machine = new Machine();
        machine.setId(1L);
        UserAcess userAcessSave = new UserAcess(user, machine, true);

        assertThatThrownBy(() -> service.requestUserAcess(userAcessSave))
                .isInstanceOf(PersistenceException.class)
                .hasMessage(String.format("An error ocurred while persisting the acess o User %s on Machine %s",
                        userAcessSave.getId().getUserLogin(), userAcessSave.getId().getMachineId()));
    }

    @Test
    public void shouldCallMethodsDeleteByIdAndFlushWhenRevokingUserAcess(){

       UserAcessId userAcessId = new UserAcessId("pedrofortini", 1L);

       service.revokeUserAcess(userAcessId);
       Mockito.verify(repository).deleteById(userAcessId);
       Mockito.verify(repository).flush();
    }

    @Test
    public void shouldThrowPersistenceExceptionWhenProblemDeletingUserAcess(){

        UserAcessId userAcessId = new UserAcessId("pedrofortini", 1L);
        PowerMockito.doThrow(new RuntimeException())
                .when(repository).deleteById(Mockito.eq(userAcessId));

        assertThatThrownBy(() -> service.revokeUserAcess(userAcessId))
                .isInstanceOf(PersistenceException.class)
                .hasMessage(String.format("An error ocurred while persisting the acess o User %s on Machine %s",
                        userAcessId.getUserLogin(), userAcessId.getMachineId()));
    }
}