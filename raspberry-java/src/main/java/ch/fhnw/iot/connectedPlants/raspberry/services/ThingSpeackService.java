package ch.fhnw.iot.connectedPlants.raspberry.services;

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
    private static String MESSURE_VALUE = "moisture";

    public void runService() throws IOException, HttpException {
        logger.info("ThingSpeack Service started");
        CallService callService = new CallService();

        Properties prop = ServiceUtil.loadProperty();


        String url = prop.getProperty("url.thingspeack.get.updateChannel");
        List<Header> headers = getHeaders(prop);
        ThingSpeakResult result = null;

        try {
            result = CallService.httpGet(url, headers);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (HttpException e) {
            logger.error(e.getMessage());
            throw e;
        }

        int moistureValue = getMoistureValue(result);
    }

    private int getMoistureValue(ThingSpeakResult result) {
        int fieldNumber = getFieldNumberMoister(result);

        int lastElement = result.getFeeds().size() - 1;
        String methodeName = String.format("getField%s", String.valueOf(1));
        String messureValue = null;
        try {
            Object obj = result.getFeeds().get(lastElement);
            Method method = result.getFeeds().get(lastElement).getClass().getMethod(methodeName);
            messureValue = (String) method.invoke(obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(messureValue);
    }

    private int getFieldNumberMoister(ThingSpeakResult result) {
        if (result.getChannel().getField1().contains(MESSURE_VALUE))
            return 1;
        else if (result.getChannel().getField2().contains(MESSURE_VALUE))
            return 2;
        else if (result.getChannel().getField3().contains(MESSURE_VALUE))
            return 3;
        else if (result.getChannel().getField4().contains(MESSURE_VALUE))
            return 4;
        else if (result.getChannel().getField5().contains(MESSURE_VALUE))
            return 5;
        return 0;
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
