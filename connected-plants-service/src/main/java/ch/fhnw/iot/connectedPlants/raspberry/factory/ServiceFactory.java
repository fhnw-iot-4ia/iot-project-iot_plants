package ch.fhnw.iot.connectedPlants.raspberry.factory;

import ch.fhnw.iot.connectedPlants.raspberry.service.Service;

public interface ServiceFactory {

    Service getService();
}
