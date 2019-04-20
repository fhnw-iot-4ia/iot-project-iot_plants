package ch.fhnw.iot.connectedPlants.raspberry.factories;

import ch.fhnw.iot.connectedPlants.raspberry.services.Service;

public abstract class ServiceFactory {

    public Service getService(String serviceName) {
        if (serviceName == null || serviceName.isEmpty())
            throw new IllegalArgumentException("serviceName is not specified");

        return createService(serviceName);
    }

    abstract Service createService(String sericeName);
}
