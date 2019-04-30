package ch.fhnw.iot.connectedPlants.raspberry.service;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import ch.fhnw.iot.connectedPlants.raspberry.entity.Feed;
import ch.fhnw.iot.connectedPlants.raspberry.entity.ThingSpeakResult;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class ThingSpeackService implements Service {
    private static Logger logger = LogManager.getLogger(CallService.class.getName());
    private static String MOISTURE_FIELD = "moisture";

    public void runService() throws IOException, HttpException {
        logger.info("ThingSpeack Service started");
//        CallService callService = new CallService();

        Properties prop = ServiceUtil.loadProperty();

        String url = prop.getProperty(PlantProperties.URL_THINGSPEAK_GET_UPDATECHANNEL);
        List<Header> headers = getHeaders(prop);
        ThingSpeakResult result;

        try {
            result = CallService.httpGet(url, headers);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (HttpException e) {
            logger.error(e.getMessage());
            throw e;
        }

        double moistureValue = getMoistureValue(result);


    }

    private double getMoistureValue(ThingSpeakResult thingSpeakResult) {
        String fieldNumber = getMoistureField(thingSpeakResult);
        int lastElement = thingSpeakResult.getFeeds().size() - 1;
        String methodName = String.format("getField%s", fieldNumber);
        String measureValue = null;
        try {
            Feed obj = thingSpeakResult.getFeeds().get(lastElement);
            Method method = obj.getClass().getMethod(methodName);
            measureValue = (String) method.invoke(obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Double.parseDouble(measureValue);
    }

    private String getMoistureField(ThingSpeakResult thingSpeakResult) {
        if (thingSpeakResult.getChannel().getField1().contains(MOISTURE_FIELD))
            return "1";
        else if (thingSpeakResult.getChannel().getField2().contains(MOISTURE_FIELD))
            return "2";
        else if (thingSpeakResult.getChannel().getField3().contains(MOISTURE_FIELD))
            return "3";
        else if (thingSpeakResult.getChannel().getField4().contains(MOISTURE_FIELD))
            return "4";
        else if (thingSpeakResult.getChannel().getField5().contains(MOISTURE_FIELD))
            return "5";
        return "1";
    }

    @NotNull
    private List<Header> getHeaders(Properties prop) {
        List<Header> headers = new ArrayList<>();

        AtomicReference<String> nameHeader = new AtomicReference<>();
        AtomicReference<String> valueHeader = new AtomicReference<>();

        prop.forEach((key, value) -> {
            String sKey = (String) key;
            if (sKey.contains("header")) {
                if (sKey.contains("name")) {
                    nameHeader.set((String) value);
                } else if (sKey.contains("value")) {
                    valueHeader.set((String) value);
                }
            }
            if (nameHeader.get() != null && valueHeader.get() != null) {
                Header header = new BasicHeader(nameHeader.get(), valueHeader.get());
                headers.add(header);
                nameHeader.set(null);
                valueHeader.set(null);
            }
        });
        return headers;
    }

}
