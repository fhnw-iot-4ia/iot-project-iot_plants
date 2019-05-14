package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.service.observer.PushService;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.HttpException;
import org.bson.Document;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, HttpException {

        PushService pushService = new PushService();
        pushService.run();
//        Document doc = ServiceUtil.getDocument();
//        Set<Map.Entry<String, Object>> set = doc.entrySet();
//
//        System.out.println("-------------befor----------");
//        set.forEach(v -> {
//            System.out.println(v.getKey());
//            System.out.println(v.getValue());
//            if (v.getKey().equals("threshold")) {
//                v.setValue(10);
//            }
//        });
//
//        System.out.println("-------------after----------");
//        set.forEach(v -> {
//            System.out.println(v.getKey());
//            System.out.println(v.getValue());
//        });

//        PlantApplication.initServices();
//        Properties props = ServiceUtil.loadProperty();
//
//        ServiceFactory serviceFactory = new ThingSpeakFactory();
//        Service service = serviceFactory.getService((String) props.get(PlantProperties.SERVICE_NAME));
//
//        try {
//            service.runService();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (HttpException e) {
//            e.printStackTrace();
//        }
//
//        ServiceFactory mqttFactory = new MQTTFactory();
//        Service mqttService = mqttFactory.getService("");
//
//        try {
//            mqttService.runService();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (HttpException e) {
//            e.printStackTrace();
//        }
    }
}
