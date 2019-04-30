package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.factory.MQTTFactory;
import ch.fhnw.iot.connectedPlants.raspberry.factory.ServiceFactory;
import ch.fhnw.iot.connectedPlants.raspberry.factory.ThingSpeakFactory;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.HttpException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class PlantApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlantApplication.class, args);

        initServices();
    }

    private static void initServices() {

        Properties props = ServiceUtil.loadProperty();

        new Thread(() -> {
            ServiceFactory serviceFactory = new ThingSpeakFactory();
            Service service = serviceFactory.getService((String) props.get(PlantProperties.SERVICE_NAME));

            try {
                service.runService();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            ServiceFactory mqttFactory = new MQTTFactory();
            Service mqttService = mqttFactory.getService("");

            try {
                mqttService.runService();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
