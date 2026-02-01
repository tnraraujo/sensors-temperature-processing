package com.tnr.sensors.sensors.temperature.processing.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class TemperatureLog {
    private UUID id;
    private UUID sensorId;
    private OffsetDateTime registeredAt;
    private Double temperature;
}
