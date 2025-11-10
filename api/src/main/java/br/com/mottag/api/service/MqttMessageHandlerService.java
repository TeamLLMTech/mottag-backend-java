package br.com.mottag.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mottag.api.dto.AntennaInfoDto;
import br.com.mottag.api.dto.DevicePositionUpdateDto;
import br.com.mottag.api.dto.DeviceReadingDto;
import br.com.mottag.api.dto.MqttMessagePayloadDto;
import br.com.mottag.api.dto.MqttMessagePayloadEventDto;
import br.com.mottag.api.dto.PositionDto;

/**
 * Service that handles incoming MQTT messages.
 * Implements MessageHandler to process messages from the MQTT input channel.
 */
@Service
public class MqttMessageHandlerService implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandlerService.class);
    private final ObjectMapper objectMapper;
    private final AntennaPositioningService positioningService;
    private final DeviceReadingBuffer readingBuffer;
    private final SimpMessagingTemplate messagingTemplate;

    // Map of antenna IDs to their physical positions
    // TODO: This should be moved to a configuration file or database
    private final Map<String, PositionDto> antennaPositions;

    public MqttMessageHandlerService(AntennaPositioningService positioningService,
                                    DeviceReadingBuffer readingBuffer,
                                    SimpMessagingTemplate messagingTemplate) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); // Register modules for Java 8 date/time support
        this.positioningService = positioningService;
        this.readingBuffer = readingBuffer;
        this.messagingTemplate = messagingTemplate;
        this.antennaPositions = initializeAntennaPositions();

        logger.info("MqttMessageHandlerService initialized with WebSocket support");
    }

    /**
     * Initializes the antenna positions map.
     * In a production system, this should be loaded from configuration or database.
     *
     * @return map of antenna IDs to their positions
     */
    private Map<String, PositionDto> initializeAntennaPositions() {
        Map<String, PositionDto> positions = new HashMap<>();
        // Example antenna positions (adjust these based on your actual antenna layout)
        positions.put("scan1", new PositionDto(0.0, 0.0));
        positions.put("scan2", new PositionDto(10.0, 0.0));
        positions.put("scan3", new PositionDto(0.0, 10.0));
        positions.put("scan4", new PositionDto(10.0, 10.0));
        positions.put("scan5", new PositionDto(5.0, 5.0));
        positions.put("scan6", new PositionDto(15.0, 5.0));
        return positions;
    }

    /**
     * Handles incoming MQTT messages.
     * Parses the message payload and processes it accordingly.
     *
     * @param message the incoming MQTT message
     * @throws MessagingException if message processing fails
     */
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            String payload = message.getPayload().toString();

            logger.info("Received MQTT message from topic '{}': {}", topic, payload);

            // Parse the message payload
            MqttMessagePayloadDto parsedMessage = parseMessage(payload, topic);

            // Process the parsed message
            processMessage(parsedMessage);

        } catch (Exception e) {
            logger.error("Error processing MQTT message: {}", e.getMessage(), e);
            throw new MessagingException("Failed to process MQTT message", e);
        }
    }

    /**
     * Parses the raw message payload into a structured DTO.
     * Attempts to parse as JSON first, falls back to plain text if that fails.
     *
     * @param payload the raw message payload
     * @param topic the MQTT topic the message was received from
     * @return parsed MqttMessagePayloadDto
     */
    private MqttMessagePayloadDto parseMessage(String payload, String topic) {
        try {
            MqttMessagePayloadDto dto = objectMapper.readValue(payload, MqttMessagePayloadDto.class);

            return dto;
        } catch (JsonProcessingException e) {
            logger.warn("Failed to parse message as JSON, treating as plain text: {}", e.getMessage());

            // If JSON parsing fails, create a simple DTO with the payload as data
            MqttMessagePayloadDto dto = new MqttMessagePayloadDto();
            return dto;
        }
    }

    /**
     * Processes the parsed MQTT message.
     * Collects antenna readings and calculates device position using the center4 algorithm.
     *
     * @param message the parsed message
     */
    protected void processMessage(MqttMessagePayloadDto message) {
        logger.info("Processing message: {}", message);

        // Collect antenna readings from multiple messages
        // In a real system, you would aggregate readings from multiple antennas over a time window
        // For demonstration, we'll process if we have position data for the reporting antenna

        String antennaId = message.getAntennaId();
        PositionDto antennaPosition = antennaPositions.get(antennaId);

        if (antennaPosition == null) {
            logger.warn("Unknown antenna ID: {}, skipping position calculation", antennaId);
            return;
        }

        // Process each detected device (MAC address)
        if (message.getEvents() != null) {
            for (MqttMessagePayloadEventDto event : message.getEvents()) {
                processDeviceReading(antennaId, antennaPosition, event);
            }
        }
    }

    /**
     * Processes a single device reading from an antenna.
     * Stores the reading in the buffer and calculates position when enough data is available.
     *
     * @param antennaId the ID of the antenna that detected the device
     * @param antennaPosition the position of the antenna
     * @param event the device detection event
     */
    private void processDeviceReading(String antennaId, PositionDto antennaPosition,
                                      MqttMessagePayloadEventDto event) {
        String deviceAddress = event.getAddress();
        int rssi = event.getRssi();
        long timestamp = System.currentTimeMillis();

        logger.info("Device {} detected by antenna {} at position ({}, {}) with RSSI: {}",
                   deviceAddress, antennaId, antennaPosition.getX(), antennaPosition.getY(), rssi);

        // Create and store the reading in the buffer
        DeviceReadingDto reading = new DeviceReadingDto(
                deviceAddress,
                antennaId,
                antennaPosition,
                rssi,
                timestamp
        );

        readingBuffer.addReading(reading);

        // Check if we have enough readings to calculate position
        if (readingBuffer.hasEnoughReadings(deviceAddress)) {
            List<AntennaInfoDto> aggregatedReadings = readingBuffer.getAggregatedReadings(deviceAddress);

            logger.debug("Device {} has {} aggregated readings from unique antennas",
                        deviceAddress, aggregatedReadings.size());

            PositionDto estimatedPosition = positioningService.calculatePositionCenter4(aggregatedReadings);

            if (estimatedPosition != null) {
                logger.info("Estimated position for device {}: ({}, {})",
                           deviceAddress, estimatedPosition.getX(), estimatedPosition.getY());

                // Send position update via WebSocket to all connected clients
                broadcastPositionUpdate(deviceAddress, estimatedPosition, aggregatedReadings.size());

                // TODO: Store the calculated position in database
                // TODO: Trigger any business logic based on device position
            }
        } else {
            List<AntennaInfoDto> currentReadings = readingBuffer.getAggregatedReadings(deviceAddress);
            logger.debug("Device {} has {} readings, need at least {} for position calculation",
                        deviceAddress, currentReadings.size(), readingBuffer.getMinReadings());
        }
    }

    /**
     * Broadcasts device position update to all WebSocket subscribers.
     *
     * @param deviceAddress the MAC address of the device
     * @param position the calculated position
     * @param antennaCount number of antennas used in calculation
     */
    private void broadcastPositionUpdate(String deviceAddress, PositionDto position, int antennaCount) {
        DevicePositionUpdateDto update = new DevicePositionUpdateDto(
                deviceAddress,
                position.getX(),
                position.getY(),
                antennaCount
        );

        try {
            messagingTemplate.convertAndSend("/topic/device-positions", update);
            logger.info("✓ Broadcast position update for device {} to WebSocket: ({}, {})",
                        deviceAddress, position.getX(), position.getY());
        } catch (Exception e) {
            logger.error("Failed to broadcast position update: {}", e.getMessage(), e);
        }
    }

}
