package ch.fhnw.iot.connectedPlants.raspberry.factories;

import ch.fhnw.iot.connectedPlants.raspberry.services.Service;
import ch.fhnw.iot.connectedPlants.raspberry.services.ThingSpeackService;

public class ThingSpeakFactory extends ServiceFactory {
    @Override
    Service createService(String serviceName) {
        if (serviceName.contains("ThingSpeak")) {
            return new ThingSpeackService();
        }
        return null;
    }
}
