package br.com.mottag.api.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import br.com.mottag.api.service.MqttMessageHandlerService;

/**
 * Configuration class for MQTT integration.
 * Sets up MQTT client factory, message channels, and inbound adapters for subscribing to topics.
 */
@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Value("${mqtt.qos:1}")
    private int qos;

    @Value("${mqtt.auto.reconnect:true}")
    private boolean autoReconnect;

    @Value("${mqtt.connection.timeout:30}")
    private int connectionTimeout;

    @Value("${mqtt.keep.alive.interval:60}")
    private int keepAliveInterval;

    /**
     * Creates and configures the MQTT client factory with connection options.
     *
     * @return configured MqttPahoClientFactory
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] { brokerUrl });
        options.setAutomaticReconnect(autoReconnect);
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAliveInterval);
        options.setCleanSession(true);

        // Set username and password if provided
        if (username != null && !username.isEmpty()) {
            options.setUserName(username);
        }
        if (password != null && !password.isEmpty()) {
            options.setPassword(password.toCharArray());
        }

        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * Creates a message channel for MQTT inbound messages.
     *
     * @return DirectChannel for MQTT messages
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * Creates an inbound message adapter that subscribes to the configured MQTT topic.
     *
     * @return MessageProducer that receives messages from MQTT broker
     */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter(
                brokerUrl,
                clientId + "-inbound",
                mqttClientFactory(),
                topic
            );

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }

    /**
     * Service activator that processes incoming MQTT messages.
     *
     * @param mqttMessageHandlerService the service that handles message processing
     * @return MessageHandler for the MQTT input channel
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(MqttMessageHandlerService mqttMessageHandlerService) {
        return mqttMessageHandlerService;
    }
}
