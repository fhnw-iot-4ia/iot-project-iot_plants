#include <DHTesp.h>
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
DHTesp dht;

// Moisture
// http://wiki.seeedstudio.com/Grove-Moisture_Sensor/
int MOISTURE = A0;
#define MOISTURE_PIN A0

// Light
#define LIGHT_PIN 2

float humidity = 0;
float temperature = 0;

String myStatus = "";

void setup()
{
  Serial.begin(115200);

  // Setup DHTesp for Temperature and Humidity Sensor
  dht.setup(DHTPIN, DHTesp::DHTTYPE);

  // Set timezone
  const int timezone = 0;
  const int dst_off = 0;
  configTime(timezone * 3600, dst_off, "pool.ntp.org", "time.nist.gov");
  // wait for time() being adjusted, as a side effect of configTime
  while (time(NULL) < 28800 * 2)
  {
    delay(500);
  }

  connectWifi();

  ThingSpeak.begin(client); // Initialize ThingSpeak
}
void loop()
{

  humidity = dht.getHumidity();
  temperature = dht.getTemperature();
  printToConsole();
  updateThinkSpeak();

  delay(10000);
}

int getMoistureSensorData()
{
  return analogRead(MOISTURE_PIN);
}

int getLightSensorData()
{
  return analogRead(LIGHT_PIN);
}

void printToConsole()
{
  // Check for error
  if (isnan(humidity) || isnan(temperature))
  {
    return;
  }

  Serial.print("Moisture = ");
  Serial.println(getMoistureSensorData());
  Serial.print("Light = ");
  Serial.println(getLightSensorData());
  Serial.print("Temperature = ");
  Serial.println(temperature);
  Serial.print("Humidity = ");
  Serial.println(humidity);

  delay(1000);
}

void connectWifi()
{

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

void updateThinkSpeak()
{

  // Check for error
  if (isnan(humidity) || isnan(temperature))
  {
    return;
  }
  // set the fields with the values
  ThingSpeak.setField(1, getMoistureSensorData());
  ThingSpeak.setField(2, getLightSensorData());
  ThingSpeak.setField(3, temperature);
  ThingSpeak.setField(4, humidity);

  // set the status
  ThingSpeak.setStatus(myStatus);

  int x = ThingSpeak.writeFields(myChannelNumber, myWriteAPIKey);

  if (x == 200)
  {
    Serial.println("Channel update successful.");
  }
  else
  {
    Serial.println("Problem updating channel. HTTP error code " + String(x));
  }
}
