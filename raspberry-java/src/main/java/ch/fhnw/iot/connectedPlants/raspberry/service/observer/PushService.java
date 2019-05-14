package ch.fhnw.iot.connectedPlants.raspberry.service.observer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PushService implements ObserverObject {
    private static final Logger logger = LogManager.getLogger(ObserverObject.class.getName());
    private final String phoneNumber = "+41797477320";

    @Override
    public void run() {
        logger.info(String.format("Started PushService to service: %s", phoneNumber));

        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAIQQK62YRKSF62R4Q", "H43+7VseF+M39/OEXu8w9iCr0HYYX3jzhkKVQxK1");
        final AmazonSNSClient client = new AmazonSNSClient(awsCredentials);
        client.setRegion(Region.getRegion(Regions.EU_WEST_1));

        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);
        String message = "Alert moisture is low";
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<>();
        //<set SMS attributes>
        sendSMSMessage(snsClient, message, smsAttributes);

        logger.info(String.format("Finished PushService to service: %s", phoneNumber));
    }

    private void sendSMSMessage(AmazonSNSClient snsClient, String message,
                                Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }
}
