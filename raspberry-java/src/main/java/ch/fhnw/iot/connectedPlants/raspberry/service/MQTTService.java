package ch.fhnw.iot.connectedPlants.raspberry.service;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import org.apache.http.HttpException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.Properties;

public class MQTTService implements Service {

    @Override
    public void runService() throws IOException, HttpException {
        Properties props = new Properties();
        try {
            int threshold = (Integer) props.get(PlantProperties.PLANT_THRESHOLD);
            while (threshold < 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
            MqttClient client = new MqttClient("tcp://test.mosquitto.org:1883", MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload("Hello world from Java".getBytes());
            client.publish("testChannel", message);
            client.disconnect();
        } catch (
                MqttException e) {
            e.printStackTrace();
        }
    }
}
