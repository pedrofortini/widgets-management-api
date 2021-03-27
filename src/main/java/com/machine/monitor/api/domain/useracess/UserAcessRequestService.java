package com.machine.monitor.api.domain.useracess;

import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.PersistenceException;
import com.machine.monitor.api.application.exception.UnprocessableEntityException;
import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineService;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.domain.user.UserService;
import com.machine.monitor.api.infrastructure.persistence.UserAcessRepository;
import io.swagger.model.UserAcessRequest;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.UserTransactionAdapter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class UserAcessRequestService {

    private UserAcessRepository repository;

    Logger logger = LoggerFactory.getLogger(UserAcessRequestService.class);

    public UserAcessRequestService(UserAcessRepository repository){

        this.repository = repository;
    }

    public UserAcess requestUserAcess(UserAcess userAcess) {

        try{

            if(userAcess.isUserAdmin()){

                this.changeCurrentAdminUser(userAcess.getMachine());
            }
            return this.repository.save(userAcess);
        }
        catch (Exception e){

            logger.error(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER_ACESS,
                    userAcess.getId().getUserLogin(), userAcess.getId().getMachineId()), e);

            throw new PersistenceException(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER_ACESS,
                    userAcess.getId().getUserLogin(), userAcess.getId().getMachineId()));
        }
    }

    private void changeCurrentAdminUser(Machine machine){

        List<UserAcess> machineAdminUsers = this.repository.findByMachineAndIsUserAdminTrue(machine);

        if(!CollectionUtils.isEmpty(machineAdminUsers)){

            UserAcess machineAdminUser = machineAdminUsers.stream().findFirst().get();
            machineAdminUser.setUserAdmin(false);
            this.repository.save(machineAdminUser);
        }
    }

    public void revokeUserAcess(UserAcessId userAcessId) {

        try {
            this.repository.deleteById(userAcessId);
            this.repository.flush();
        }
        catch (Exception e){

            logger.error(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER_ACESS,
                    userAcessId.getUserLogin(), userAcessId.getMachineId()), e);

            throw new PersistenceException(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER_ACESS,
                    userAcessId.getUserLogin(),userAcessId.getMachineId()));
        }
    }
}
