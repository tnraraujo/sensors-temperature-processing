package com.tnr.sensors.sensors.temperature.processing.api.controller;

import com.tnr.sensors.sensors.temperature.processing.api.model.TemperatureLogOutput;
import com.tnr.sensors.sensors.temperature.processing.common.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("api/sensors/{sensorId}/temperatures/data")
@Slf4j
public class TemperatureProcessingController {

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable String sensorId, @RequestBody String input) {
        if (input == null || input.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        double temperature;

        try {
            temperature = Double.parseDouble(input);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var logOutput = TemperatureLogOutput.
                builder()
                .id(IdGenerator.generateTimeBaseUUID().toString())
                .sensorId(sensorId)
                .registeredAt(OffsetDateTime.now())
                .value(temperature)
                .build();

        log.info(logOutput.toString());
    }



}
