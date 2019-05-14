package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.service.ThingSpeackService;
import ch.fhnw.iot.connectedPlants.raspberry.service.observer.MQTTService;
import ch.fhnw.iot.connectedPlants.raspberry.service.observer.ObserverObject;
import ch.fhnw.iot.connectedPlants.raspberry.service.observer.PushService;

import java.util.Arrays;

public class ThingSpeakFactory extends ServiceFactory {
    @Override
    Service createService(String serviceName) {
        if (serviceName.contains("ThingSpeak")) {
            ObserverObject mqttService = new MQTTService();
            PushService pushService = new PushService();
            return new ThingSpeackService(Arrays.asList(mqttService));
//            return new ThingSpeackService(Arrays.asList(mqttService, pushService));
        }
        return null;
    }
}
