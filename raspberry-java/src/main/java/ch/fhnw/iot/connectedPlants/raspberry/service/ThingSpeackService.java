package ch.fhnw.iot.connectedPlants.raspberry.service;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import ch.fhnw.iot.connectedPlants.raspberry.entity.Channel;
import ch.fhnw.iot.connectedPlants.raspberry.entity.Feed;
import ch.fhnw.iot.connectedPlants.raspberry.entity.ThingSpeakResult;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ThingSpeackService implements Service {
    private static Logger logger = LogManager.getLogger(CallService.class.getName());
    private static String MOISTURE_FIELD = "moisture";

    private final List<ObserverObject> observers = new ArrayList<>();


    public ThingSpeackService(ObserverObject... obj) {
        if (obj == null) {
            throw new IllegalArgumentException("obj not specified");
        }
        observers.addAll(Arrays.asList(obj));
    }

    public void runService() throws IOException, HttpException {
        logger.info("ThingSpeack Service started");

        Properties prop = ServiceUtil.loadProperty();

        String url = prop.getProperty(PlantProperties.URL_THINGSPEAK_GET_UPDATECHANNEL);
        List<Header> headers = getHeaders(prop);
        ThingSpeakResult result;

        try {
            result = CallService.httpGet(url, headers);
            updateStoredObject(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void updateStoredObject(ThingSpeakResult thingSpeakResult) throws IOException, HttpException {
        Document valFromDB = ServiceUtil.getDocument();
        Set<Map.Entry<String, Object>> set = valFromDB.entrySet();

        AtomicReference<Double> threshold = new AtomicReference<>(0.0);
        set.forEach(v -> {
            if (v.getKey().equals("threshold")) {
                threshold.set((Double) v.getValue());
            }
        });

        double moisture = getValue(thingSpeakResult, MOISTURE_FIELD);

        if (moisture < threshold.get()) {
            for (ObserverObject observer : observers) {
                observer.run();
            }
        }
        ServiceUtil.saveDocument(valFromDB, moisture);
    }

    @NotNull
    private Double getValue(ThingSpeakResult thingSpeakResult, String value) {
        int lastElement = thingSpeakResult.getFeeds().size() - 1;
        Feed feed = thingSpeakResult.getFeeds().get(lastElement);
        Channel ch = thingSpeakResult.getChannel();

        if (ch.getField1().toLowerCase().contains(value.toLowerCase())) {
            return Double.parseDouble(feed.getField1());
        } else if (ch.getField2().toLowerCase().contains(value.toLowerCase()))
            return Double.parseDouble(feed.getField2());
        else if (ch.getField3().toLowerCase().contains(value.toLowerCase()))
            return Double.parseDouble(feed.getField3());
        else if (ch.getField4().toLowerCase().contains(value.toLowerCase()))
            return Double.parseDouble(feed.getField4());
        else if (ch.getField5().toLowerCase().contains(value.toLowerCase()))
            return Double.parseDouble(feed.getField5());
        return 0.0;
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
