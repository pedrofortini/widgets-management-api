package com.machine.monitor.api.application.controllers;

import com.machine.monitor.api.domain.user.User;
import com.machine.monitor.api.domain.user.UserService;
import com.machine.monitor.api.domain.user.converter.UserRequestConverter;
import com.machine.monitor.api.domain.user.converter.UserResponseConverter;
import com.machine.monitor.api.domain.useracess.UserAcess;
import com.machine.monitor.api.domain.useracess.UserAcessId;
import com.machine.monitor.api.domain.useracess.UserAcessRequestService;
import com.machine.monitor.api.domain.useracess.converter.UserAcessRequestConverter;
import io.swagger.api.UsersApi;
import io.swagger.model.UserAcessRequest;
import io.swagger.model.UserRequest;
import io.swagger.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UsersController implements UsersApi {

	private UserService userService;
	private UserAcessRequestService userAcessRequestService;
	private UserRequestConverter userRequestConverter;
	private UserResponseConverter userResponseConverter;
	private UserAcessRequestConverter userAcessRequestConverter;

	public UsersController(UserService userService,
						   UserAcessRequestService userAcessRequestService,
						   UserRequestConverter userRequestConverter,
						   UserResponseConverter userResponseConverter,
						   UserAcessRequestConverter userAcessRequestConverter){

		this.userService = userService;
		this.userAcessRequestService = userAcessRequestService;
		this.userRequestConverter = userRequestConverter;
		this.userResponseConverter = userResponseConverter;
		this.userAcessRequestConverter = userAcessRequestConverter;
	}

	@Override
	public ResponseEntity<UserResponse> getUserByLogin(@PathVariable("login") String login) {

		User user = userService.findUserByLogin(login);
		UserResponse userTemplate = userResponseConverter.convert(user);

		return ResponseEntity.ok(userTemplate);
	}

	@Override
	public ResponseEntity<List<UserResponse>> getUsers() {

		List<User> users = userService.getAllUsers();
		List<UserResponse> userTemplates = userResponseConverter.convertUserList(users);

		return ResponseEntity.ok(userTemplates);
	}

	@Override
	public ResponseEntity<Void> saveUser(@Valid @RequestBody UserRequest user) {

		User domainUser = userRequestConverter.convert(user);
		userService.saveUser(domainUser);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> saveUserAcessRequest(@Valid @RequestBody UserAcessRequest user) {

		UserAcess userAcessGrant = userAcessRequestConverter.convert(user);
		userAcessRequestService.requestUserAcess(userAcessGrant);

		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> deleteUserAcessRequest(@RequestHeader(value="machineId", required=true) Long machineId,
													   @RequestHeader(value="userLogin", required=true) String userLogin){

		UserAcessId userAcessRevoke = this.userAcessRequestConverter.convertRevokeRequest(userLogin, machineId);
		userAcessRequestService.revokeUserAcess(userAcessRevoke);

		return ResponseEntity.noContent().build();
	}
}
