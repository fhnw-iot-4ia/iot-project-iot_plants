#include <DHTesp.h>
#include <ESP8266WiFi.h>
#include <time.h>
#include <ThingSpeak.h>
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
#define DHT_PIN 2
#define DHT_TYPE DHT11
DHTesp dht;

// Moisture
// http://wiki.seeedstudio.com/Grove-Moisture_Sensor/
#define MOISTURE_PIN A0

// Light
#define LIGHT_PIN 0

// Variables to update
float moisture = 0; 
float light = 0; 
float humidity = 0;
float temperature = 0;
String myStatus = "";

void setup()
{
  Serial.begin(115200);
  Serial.println("Setup started"); 
  // Setup DHTesp for Temperature and Humidity Sensor
  dht.setup(DHT_PIN, DHTesp::DHT_TYPE);
  
  connectWifi();
  
  // Set timezone
  const int timezone = 0;
  const int dst_off = 0;
  configTime(timezone * 3600, dst_off, "pool.ntp.org", "time.nist.gov");
  // wait for time() being adjusted, as a side effect of configTime
  while (time(NULL) < 28800 * 2)
  {
    delay(500);
  }

  ThingSpeak.begin(client); // Initialize ThingSpeak
  Serial.println("Setup finished"); 
}
void loop()
{
  moisture = analogRead(MOISTURE_PIN);
  light = analogRead(LIGHT_PIN);
  humidity = dht.getHumidity();
  temperature = dht.getTemperature();
  printToConsole();
  updateThinkSpeak();

  delay(20000);
}

void printToConsole()
{
  // Check for error
  if (isnan(humidity) || isnan(temperature))
  {
    return;
  }

  Serial.print("Moisture = ");
  Serial.println(moisture);
  Serial.print("Light = ");
  Serial.println(light);
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
  ThingSpeak.setField(1, moisture);
  ThingSpeak.setField(2, light);
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
