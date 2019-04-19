#include <ESP8266WiFi.h>
#include <time.h>
#include "ThingSpeak.h"
#include "secrets.h"
#include "DHT.h"

// WIFI Settings from secrets.h
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;

// ThingSpeak Channelnumber and API Key, from secrets.h
unsigned long myChannelNumber = SECRET_CH_ID;
const char *myWriteAPIKey = SECRET_WRITE_APIKEY;

/*********** Sensor Connectors ************/
// Humidity & Temperature
#define DHTPIN 0
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// Moisture
int MOISTURE = A0;

// Light
int LIGHT = 2;

void setup()
{
    Serial.begin(115200);

    WiFi.mode(WIFI_STA);
    if (WiFi.status() != WL_CONNECTED)
    {
        Serial.print("Attempting to connect to SSID: ");
        Serial.println(ssid);
        while (WiFi.status() != WL_CONNECTED)
        {
            WiFi.begin(ssid, pass);
            Serial.print(".");
            delay(5000);
        }
        Serial.print("Connected, IP = ");
        Serial.println(WiFi.localIP());
    }
}
void loop()
{
    Serial.print("Moisture = " + getMoistureSensorData());
    delay(1000);
}

int getMoistureSensorData()
{
    return analogRead(MOISTURE);
}
