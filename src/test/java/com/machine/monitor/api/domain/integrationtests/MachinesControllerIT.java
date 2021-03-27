package com.machine.monitor.api.domain.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.MachineDetailResponse;
import io.swagger.model.MachineRequest;
import io.swagger.model.MachineResponse;
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
public class MachinesControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void integratedTestSaveMachineAndSearch() throws Exception {

        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setName("Test Machine 1");
        machineRequest.setIpAddress("localhost");
        machineRequest.setMachineIsUp(true);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MachineRequest> entity = new HttpEntity<>(machineRequest, headers);

        ResponseEntity<Void> responseSaveMachine = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/machines"), HttpMethod.PUT, entity, Void.class);

        ResponseEntity<String> responseFindAllMachines = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/machines"), HttpMethod.GET, entity, String.class);

        ResponseEntity<String> responseFindMachineById = restTemplate.exchange(
                createURLWithPort("/machine-monitor-api/v1/machines/1"), HttpMethod.GET, entity, String.class);

        String stringMachineLists = responseFindAllMachines.getBody();
        String stringMachine = responseFindMachineById.getBody();

        ObjectMapper mapper = new ObjectMapper();
        List<MachineResponse> machineList = Arrays.asList(mapper.readValue(stringMachineLists, MachineResponse[].class));
        MachineDetailResponse machineDetailResponse = mapper.readValue(stringMachine, MachineDetailResponse.class);

        assertThat(responseSaveMachine.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(machineList).isNotEmpty();
        assertThat(machineList.size()).isEqualTo(2);
        assertThat(machineDetailResponse.getId()).isEqualTo(1);
        assertThat(machineDetailResponse.getName()).isEqualTo("Test Machine 1");
        assertThat(machineDetailResponse.getIpAddress()).isEqualTo("localhost");
        assertThat(machineDetailResponse.getMachineIsUp()).isTrue();
        assertThat(machineDetailResponse.getMachineEventsLog()).isNotEmpty();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
