package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.service.ThingSpeackService;

public class ThingSpeakFactory extends ServiceFactory {
    @Override
    Service createService(String serviceName) {
        if (serviceName.contains("ThingSpeak")) {
            return new ThingSpeackService();
        }
        return null;
    }
}
