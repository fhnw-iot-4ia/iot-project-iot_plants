package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.service.ThingSpeakService;
import ch.fhnw.iot.connectedPlants.raspberry.service.observer.MQTTService;
import ch.fhnw.iot.connectedPlants.raspberry.service.observer.ObserverObject;

import java.util.Arrays;

public class ThingSpeakFactory implements ServiceFactory {

    @Override
    public Service getService() {
        // Hier könnten mehrere Services als ArrayListe übergeben werden
        ObserverObject mqttService = new MQTTService();
        return new ThingSpeakService(Arrays.asList(mqttService));
    }
}
