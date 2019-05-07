package ch.fhnw.iot.connectedPlants.raspberry.service;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Properties;

public class MQTTService implements ObserverObject {
    Properties properties = ServiceUtil.loadProperty();
    private final String userName;
    private final String password;
    private final String url;
    private final String port;
    private final String channel;
    private final String channelInfo;

    public MQTTService() {
        this.userName = properties.getProperty(PlantProperties.MQTT_USERNAME);
        this.password = properties.getProperty(PlantProperties.MQTT_PASSWORD);
        this.url = properties.getProperty(PlantProperties.MQTT_URL);
        this.port = properties.getProperty(PlantProperties.MQTT_PORT);
        this.channel = properties.getProperty(PlantProperties.MQTT_CHANNEL);
        this.channelInfo = properties.getProperty(PlantProperties.MQTT_CHANNEL_INFO);
    }

    @Override
    public void run() {
        try {
            String serviceURI = url + ":" + port;
            MqttClient client = new MqttClient(serviceURI, MqttClient.generateClientId());
            MqttConnectOptions connOpts = setUpConnectionOptions(userName, password);
            client.connect(connOpts);
            MqttMessage message = new MqttMessage();
            message.setPayload(channelInfo.getBytes());
            client.publish(channel, message);
            client.disconnect();
        } catch (
                MqttException e) {
            e.printStackTrace();
        }
    }

    private static MqttConnectOptions setUpConnectionOptions(String username, String password) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        return connOpts;
    }
}
