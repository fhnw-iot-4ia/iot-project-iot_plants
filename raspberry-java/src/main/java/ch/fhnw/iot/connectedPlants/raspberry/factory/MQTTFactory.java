package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.MQTTService;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;

public class MQTTFactory extends ServiceFactory {
    @Override
    Service createService(String serviceName) {
        return new MQTTService();
    }
}
