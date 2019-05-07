package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.MQTTService;
import ch.fhnw.iot.connectedPlants.raspberry.service.ObserverObject;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.service.ThingSpeackService;

public class ThingSpeakFactory extends ServiceFactory {
    @Override
    Service createService(String serviceName) {
        if (serviceName.contains("ThingSpeak")) {
            ObserverObject mqttService = new MQTTService();
            return new ThingSpeackService(mqttService);
        }
        return null;
    }
}
