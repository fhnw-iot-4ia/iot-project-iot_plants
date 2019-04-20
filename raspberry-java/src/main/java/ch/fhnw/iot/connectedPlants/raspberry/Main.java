package ch.fhnw.iot.connectedPlants.raspberry;

import ch.fhnw.iot.connectedPlants.raspberry.services.CallService;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws IOException {
        CallService callService = new CallService();

        Properties prop = loadProperty();


        String url = prop.getProperty("url.thingspeack.get.updateChannel");
        List<Header> headers = getHeaders(prop);

        callService.httpGet(url, headers);
    }

    @NotNull
    private static List<Header> getHeaders(Properties prop) {
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

    private static Properties loadProperty() {
        Properties result = new Properties();
        try {
            //load a properties file from class path, inside static method
            result.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
