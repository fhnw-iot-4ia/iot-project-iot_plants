package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.factory.ServiceFactory;
import ch.fhnw.iot.connectedPlants.raspberry.factory.ThingSpeakFactory;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Properties;

@SpringBootApplication
public class PlantApplication extends SpringBootServletInitializer {
    private static Logger logger = LogManager.getLogger(PlantApplication.class.getName());

    public static void main(String[] args) throws Exception {
        logger.info("Application started");
        SpringApplication.run(PlantApplication.class, args);
        initServices();
    }

    public static void initServices() throws Exception {
        Properties props = ServiceUtil.loadProperty();

        ServiceFactory serviceFactory = new ThingSpeakFactory();
        Service thingSpeakService = serviceFactory.getService((String) props.get(PlantProperties.SERVICE_NAME));


        while (true) {
            try {
                // Pullt Daten von Matlab und verarbeitet diese
                thingSpeakService.runService();
                int intervall = Integer.parseInt(props.getProperty(PlantProperties.SERVICE_INTERVALL));
                Thread.sleep(intervall);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw e;
            }
        }
    }
}
