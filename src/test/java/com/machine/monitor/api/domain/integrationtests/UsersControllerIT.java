package com.machine.monitor.api.domain.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void integratedTestSaveMachineUserSearchRequestAndRevokeAcess() throws Exception {

        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setName("Test Machine 1");
        machineRequest.setIpAddress("localhost");
        machineRequest.setMachineIsUp(true);

        HttpHeaders headersSaveMachine = new HttpHeaders();
        HttpEntity<MachineRequest> entityMachine = new HttpEntity<>(machineRequest, headersSaveMachine);

        ResponseEntity<Void> responseSaveMachine = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/machines"), HttpMethod.PUT, entityMachine, Void.class);

        UserRequest userRequest = new UserRequest();
        userRequest.setLogin("pedrofortini");
        userRequest.setName("Pedro Fortini");

        HttpHeaders headersSaveUser = new HttpHeaders();
        HttpEntity<UserRequest> entityUser = new HttpEntity<>(userRequest, headersSaveUser);

        ResponseEntity<Void> responseSaveUser = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users"), HttpMethod.PUT, entityUser, Void.class);

        UserRequest userRequest1 = new UserRequest();
        userRequest1.setLogin("pedrofortini1");
        userRequest1.setName("Pedro Fortini");

        HttpHeaders headersSaveUser1 = new HttpHeaders();
        HttpEntity<UserRequest> entityUser1 = new HttpEntity<>(userRequest1, headersSaveUser1);

        ResponseEntity<Void> responseSaveUser1 = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users"), HttpMethod.PUT, entityUser1, Void.class);

        ResponseEntity<String> responseFindAllUsers = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users"), HttpMethod.GET, entityUser, String.class);

        ResponseEntity<String> responseFindUserByLogin = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users/pedrofortini"), HttpMethod.GET, entityUser, String.class);

        UserAcessRequest userAcessRequest = new UserAcessRequest();
        userAcessRequest.setUserLogin("pedrofortini");
        userAcessRequest.setMachineId(1L);
        userAcessRequest.setIsUserAdmin(true);

        HttpHeaders headersRequestAcess = new HttpHeaders();
        HttpEntity<UserAcessRequest> entityUserAcessRequest = new HttpEntity<>(userAcessRequest, headersRequestAcess);

        ResponseEntity<Void> responseSaveUserAcessRequest = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users/acess/request"), HttpMethod.PUT, entityUserAcessRequest, Void.class);

        UserAcessRequest userAcessRequest1 = new UserAcessRequest();
        userAcessRequest1.setUserLogin("pedrofortini1");
        userAcessRequest1.setMachineId(1L);
        userAcessRequest1.setIsUserAdmin(true);

        HttpHeaders headersRequestAcess1 = new HttpHeaders();
        HttpEntity<UserAcessRequest> entityUserAcessRequest1 = new HttpEntity<>(userAcessRequest1, headersRequestAcess1);

        ResponseEntity<Void> responseSaveUserAcessRequest1 = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users/acess/request"), HttpMethod.PUT, entityUserAcessRequest1, Void.class);

        HttpHeaders headersRequestRevokeAcess = new HttpHeaders();
        headersRequestRevokeAcess.add("Content-Type", "application/json");
        headersRequestRevokeAcess.add("machineId", "1");
        headersRequestRevokeAcess.add("userLogin", "pedrofortini");

        HttpEntity entityUserRevokeAcessRequest = new HttpEntity<>(headersRequestRevokeAcess);

        ResponseEntity<Void> responseRevokeUserAcessRequest = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/users/acess/request"), HttpMethod.DELETE, entityUserRevokeAcessRequest, Void.class);

        String stringUserLists = responseFindAllUsers.getBody();
        String stringUser = responseFindUserByLogin.getBody();

        ObjectMapper mapper = new ObjectMapper();
        List<UserResponse> userResponseList = Arrays.asList(mapper.readValue(stringUserLists, UserResponse[].class));
        UserResponse userResponse = mapper.readValue(stringUser, UserResponse.class);

        assertThat(responseSaveMachine.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSaveUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSaveUser1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSaveUserAcessRequest.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSaveUserAcessRequest1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseRevokeUserAcessRequest.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userResponseList).isNotEmpty();
        assertThat(userResponseList.size()).isEqualTo(2);
        assertThat(userResponse.getLogin()).isEqualTo("pedrofortini");
        assertThat(userResponse.getName()).isEqualTo("Pedro Fortini");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
