package com.tnr.sensors.sensors.temperature.processing.api.controller;

import com.tnr.sensors.sensors.temperature.processing.api.model.TemperatureLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.tnr.sensors.sensors.temperature.processing.infra.config.RabbitMQNamesConfig.EXCHANGE_NAME;

@RestController
@RequestMapping("api/sensors/{sensorId}/temperatures/data")
@Slf4j
@AllArgsConstructor
public class TemperatureProcessingController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable UUID sensorId, @RequestBody String input) {
        if (input == null || input.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        double temperature;

        try {
            temperature = Double.parseDouble(input);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var logOutput = TemperatureLog.
                builder()
                .id(UUID.randomUUID())
                .sensorId(sensorId)
                .registeredAt(OffsetDateTime.now())
                .temperature(temperature)
                .build();

        log.info(logOutput.toString());

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", logOutput, message -> {
            var properties = message.getMessageProperties();
            properties.setHeader("sensorId", sensorId);
            return message;
        });
    }
}
