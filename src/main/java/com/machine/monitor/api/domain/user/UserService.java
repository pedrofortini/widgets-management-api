package com.machine.monitor.api.domain.user;

import com.machine.monitor.api.application.MessageConstants;
import com.machine.monitor.api.application.exception.PersistenceException;
import com.machine.monitor.api.application.exception.ResourceNotFoundException;
import com.machine.monitor.api.domain.machine.MachineService;
import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.infrastructure.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	UserRepository userRepository;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	public UserService(UserRepository userRepository){

		this.userRepository = userRepository;
	}

	public User saveUser(User user){

		try{

			return userRepository.save(user);
		}
		catch (Exception e){

			logger.error(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER,
					user.getLogin()), e);

			throw new PersistenceException(String.format(MessageConstants.MESSAGE_ERROR_PERSISTING_USER,
					user.getLogin()));
		}
	}

	public User findUserByLogin(String login){

		Optional<User> userOptional = this.userRepository.findById(login);

		if(!userOptional.isPresent()){
			throw new ResourceNotFoundException(String.format(MessageConstants.MESSAGE_USER_NOT_FOUND, login));
		}

		return userOptional.get();
	}

	public List<User> getAllUsers(){

		return Optional.ofNullable(userRepository.findAll()).orElse(new ArrayList<>());
	}
}
