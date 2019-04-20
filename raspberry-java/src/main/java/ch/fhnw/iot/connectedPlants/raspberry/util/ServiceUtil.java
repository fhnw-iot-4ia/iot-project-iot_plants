package ch.fhnw.iot.connectedPlants.raspberry.util;

import ch.fhnw.iot.connectedPlants.raspberry.services.Service;

import java.io.IOException;
import java.util.Properties;

public class ServiceUtil {

    public static Properties loadProperty() {
        Properties result = new Properties();
        try {
            //load a properties file from class path, inside static method
            result.load(Service.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
