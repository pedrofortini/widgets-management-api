package com.machine.monitor.api.application.controllers;

import com.machine.monitor.api.domain.machine.Machine;
import com.machine.monitor.api.domain.machine.MachineEventLog;
import com.machine.monitor.api.domain.machine.MachineEventLogService;
import com.machine.monitor.api.domain.machine.MachineService;
import com.machine.monitor.api.domain.machine.converter.MachineRequestConverter;
import com.machine.monitor.api.domain.machine.converter.MachineResponseConverter;
import io.swagger.api.MachinesApi;
import io.swagger.model.MachineDetailResponse;
import io.swagger.model.MachineRequest;
import io.swagger.model.MachineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@RestController
public class MachinesController implements MachinesApi {

	private MachineService machineService;
	private MachineEventLogService eventLogService;
	private MachineRequestConverter requestConverter;
	private MachineResponseConverter responseConverter;

	public MachinesController(MachineService machineService,
							  MachineEventLogService eventLogService,
							  MachineRequestConverter requestConverter,
							  MachineResponseConverter responseConverter){

		this.machineService = machineService;
		this.eventLogService = eventLogService;
		this.requestConverter = requestConverter;
		this.responseConverter = responseConverter;
	}

	@Override
	public ResponseEntity<MachineDetailResponse> getMachineById(@PathVariable("id") Long id) {

		Machine machine = machineService.findMachineById(id);
		List<MachineEventLog> machineEventLogs = eventLogService.findEventsOfMachine(machine);
		MachineDetailResponse response = responseConverter.convertToDetail(machine, machineEventLogs);

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<List<MachineResponse>> getMachines() {

		List<Machine> machines = machineService.getAllMachines();
		List<MachineResponse> machineResponses = responseConverter.convertListMachines(machines);
		return ResponseEntity.ok(machineResponses);
	}

	@Override
	public ResponseEntity<Void> saveMachine(@Valid @RequestBody MachineRequest machine) {

		Machine domainMachine = requestConverter.convert(machine);
		machineService.saveMachine(domainMachine);

		return ResponseEntity.ok().build();
	}
}
