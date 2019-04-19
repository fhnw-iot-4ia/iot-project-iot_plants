#include <DHT.h>
#include <ESP8266WiFi.h>
#include <time.h>
#include "ThingSpeak.h"
#include "secrets.h"

// WIFI Settings from secrets.h
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;
WiFiClient client;

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

float h = 0;
float t = 0;

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

  const int timezone = 0;
  const int dst_off = 0;
  configTime(timezone * 3600, dst_off, "pool.ntp.org", "time.nist.gov");
  // wait for time() being adjusted, as a side effect of configTime
  while (time(NULL) < 28800 * 2)
  {
    delay(500);
  }
}
void loop()
{

  h = dht.readHumidity();
  t = dht.readTemperature();
  printToConsole();
}

int getMoistureSensorData()
{
  return analogRead(MOISTURE);
}

int getLightSensorData()
{
}

void printToConsole()
{
  // Check for error
  if (isnan(h) || isnan(t))
  {
    return;
  }

  Serial.print("Humidity = ");
  Serial.println(h, 3);
  Serial.print("Temperature = ");
  Serial.println(t, 3);

  Serial.print("Moisture = ");
  Serial.println(getMoistureSensorData());
  delay(1000);
}
