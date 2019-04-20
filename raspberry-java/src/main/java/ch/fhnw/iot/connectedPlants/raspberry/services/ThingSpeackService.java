package ch.fhnw.iot.connectedPlants.raspberry.services;

import ch.fhnw.iot.connectedPlants.raspberry.Main;
import ch.fhnw.iot.connectedPlants.raspberry.entity.ThingSpeakResult;
import ch.fhnw.iot.connectedPlants.raspberry.util.ServiceUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class ThingSpeackService implements Service {

    public void runService() {

    CallService callService = new CallService();

    Properties prop = ServiceUtil.loadProperty();


    String url = prop.getProperty("url.thingspeack.get.updateChannel");
    List<Header> headers = getHeaders(prop);

        try {
            CallService.httpGet(url,headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
