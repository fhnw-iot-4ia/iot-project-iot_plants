package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.factory.ServiceFactory;
import ch.fhnw.iot.connectedPlants.raspberry.factory.ThingSpeakFactory;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ConnectedPlants implements LifecycleListener {

    @Override
    public void lifecycleEvent(LifecycleEvent lifeCE) {
        if (Lifecycle.AFTER_START_EVENT.equals(lifeCE.getType())) {
            System.out.println("************ TomcatHostLifecycleListener: After Start Event");
        }
        System.out.println("grizi");

    }
    private static Logger logger = LogManager.getLogger(ConnectedPlants.class.getName());

    public void init() {
        logger.info("Application started");
        System.out.println("started");

        try {
            initServices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void initServices() throws Exception {

        logger.info("Start refresh service");
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
