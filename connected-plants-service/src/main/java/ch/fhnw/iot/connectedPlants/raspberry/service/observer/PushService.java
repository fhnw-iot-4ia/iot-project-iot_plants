package ch.fhnw.iot.connectedPlants.raspberry.service.observer;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PushService {
    private static final Logger logger = LogManager.getLogger(ObserverObject.class.getName());
    private static double moisture;
    private static final Properties props = ServiceUtil.properties;

    public static void run() {
        logger.info("Started PushService to service: %s");
        getInfos();
        AWSCredentials awsCredentials = new BasicAWSCredentials(props.getProperty(PlantProperties.PUSH_ACCESSKEY), props.getProperty(PlantProperties.PUSH_SECRETKEY));

        AmazonSNS snsClient = AmazonSNSClient.builder()
                .withRegion(Region.getRegion(Regions.US_EAST_1).getName())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        String message = String.format("Alert moisture is low. Time: %s , moister: %s", new Date().toString(), String.valueOf(moisture));
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<>();

        sendSMSMessage(snsClient, message, smsAttributes);

        logger.info("Finished PushService to service: %s");
    }

    private static void sendSMSMessage(AmazonSNS snsClient, String message,
                                       Map<String, MessageAttributeValue> smsAttributes) {
        snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withTopicArn("arn:aws:sns:us-east-1:859981047765:IoT")
                .withMessageAttributes(smsAttributes));
    }

    private static void getInfos() {
        Document document = ServiceUtil.getDocument();
        Set<Map.Entry<String, Object>> set = document.entrySet();

        AtomicReference<Double> moistureVal = new AtomicReference<>(0.0);
        set.forEach(v -> {
            if (v.getKey().equals("measuredMoistureValue")) {
                moistureVal.set((Double) v.getValue());
            }
        });

        moisture = moistureVal.get();
    }
}
