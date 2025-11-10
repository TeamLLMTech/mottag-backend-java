package br.com.mottag.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mottag.api.dto.MqttMessagePayloadDto;

/**
 * Service that handles incoming MQTT messages.
 * Implements MessageHandler to process messages from the MQTT input channel.
 */
@Service
public class MqttMessageHandlerService implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandlerService.class);
    private final ObjectMapper objectMapper;

    public MqttMessageHandlerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); // Register modules for Java 8 date/time support
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
     * Override this method or extend this class to implement custom business logic.
     *
     * @param message the parsed message
     */
    protected void processMessage(MqttMessagePayloadDto message) {
        logger.info("Processing message: {}", message);



    }

}
