package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.factories.ServiceFactory;
import ch.fhnw.iot.connectedPlants.raspberry.factories.ThingSpeakFactory;
import ch.fhnw.iot.connectedPlants.raspberry.services.Service;
import ch.fhnw.iot.connectedPlants.raspberry.services.ThingSpeackService;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, HttpException {
        Properties props = ServiceUtil.loadProperty();

        ServiceFactory serviceFactory = new ThingSpeakFactory();
        Service service = serviceFactory.getService((String) props.get("service.name"));

        service.runService();
    }
}