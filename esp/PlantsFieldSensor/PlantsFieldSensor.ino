#include <ChainableLED.h>
#include <DHTesp.h>
#include <ESP8266WiFi.h>
#include <ESP8266MQTTClient.h> // v1.0.4
#include <time.h>
#include <ThingSpeak.h>
#include "secrets.h"

// WIFI Settings from secrets.h
const char ssid[] = SECRET_SSID;
const char pass[] = SECRET_PASS;
WiFiClient wifiClient;

// ThingSpeak Channelnumber and API Key, from secrets.h
unsigned long myChannelNumber = SECRET_CH_ID;
const char *myWriteAPIKey = SECRET_WRITE_APIKEY;

// MQTT
MQTTClient client;

const char *mqttBrokerHost = MQTT_BROKER_HOST;
const char *mqttPortnumber = MQTT_PORT;
const char *mqttTopic = MQTT_TOPIC;
const char *mqttUser = MQTT_USER;
const char *mqttPassword = MQTT_PASSWORD;
char mqttBrokerUri[64];

/*********** Sensor Connectors ************/
// Humidity & Temperature
#define DHT_PIN 2
#define DHT_TYPE DHT11
DHTesp dht;

// Moisture http://wiki.seeedstudio.com/Grove-Moisture_Sensor/
#define MOISTURE_PIN A0

// Light
#define LIGHT_PIN 0

// ChainableRGB
// #define RGB_PIN A2
// #define RGB_PIN2 A3

//Defines the num of LEDs used, The undefined
//will be lost control.
// #define NUM_LEDS  5
// ChainableLED leds(RGB_PIN, RGB_PIN2, NUM_LEDS);

// Variables to update
float moisture = 0;
float light = 0;
float humidity = 0;
float temperature = 0;
String myStatus = "";

bool giveSomeWater = false;
bool debug = false;

int updateThingspeakCounter = 0;

void connectWifi()
{
  WiFi.mode(WIFI_STA);

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

void updateThinkSpeak()
{
  // Check for error and return
  if (isnan(humidity) || isnan(temperature))
  {
    return;
  }
  // set the fields with the values
  ThingSpeak.setField(1, moisture);
  ThingSpeak.setField(2, light);
  ThingSpeak.setField(3, temperature);
  ThingSpeak.setField(4, humidity);
  ThingSpeak.setField(8, giveSomeWater);

  // set the status
  ThingSpeak.setStatus(myStatus);

  int x = ThingSpeak.writeFields(myChannelNumber, myWriteAPIKey);

  if (x >= 200 && x < 300)
  {
    Serial.println("Channel update successful.");
  }
  else
  {
    Serial.println("Problem updating channel. HTTP error code " + String(x));
  }
}

void handleConnected()
{
  if (debug)
    Serial.println("Connected to broker");
  client.subscribe(mqttTopic);
}

void handleSubscribed(int topicId)
{
  if (debug)
  {
    Serial.print("Subscribed with topicID ");
    Serial.println(topicId);
  }
}

void handleDataReceived(String topic, String data, bool b)
{
  Serial.print("Received topic: ");
  Serial.print(topic);
  Serial.print(", data: ");
  Serial.println(data);

  giveSomeWater = data == "1";
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
}

void setup()
{
  Serial.begin(115200);
  if (debug)
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

  ThingSpeak.begin(wifiClient); // Initialize ThingSpeak

  sprintf(mqttBrokerUri, "mqtt://%s:%s@%s:%s", mqttUser, mqttPassword, mqttBrokerHost, mqttPortnumber);
  if (debug)
  {
    Serial.println("Starting MQTT Setup");
    Serial.print("Connecting to ");
    Serial.println(mqttBrokerUri);
  }

  client.onConnect(handleConnected);
  client.onSubscribe(handleSubscribed);
  client.onData(handleDataReceived);
  client.begin(mqttBrokerUri);

  Serial.println("Setup finished");
}
void loop()
{
  if (WiFi.status() != WL_CONNECTED)
  {
    if (debug)
    {
      Serial.println(wifiClient.available());
      Serial.print(WiFi.status());
      Serial.println(" = status of wifi");
    }
    connectWifi();
  }
  if (giveSomeWater)
  {
    if (debug)
      Serial.println("Have to give some water!");
    //leds.setColorHSB(1, 0.0, 1.0, 0.5);
  }
  moisture = analogRead(MOISTURE_PIN);
  light = analogRead(LIGHT_PIN);
  humidity = dht.getHumidity();
  temperature = dht.getTemperature();

  if (debug)
  {
    printToConsole();
  }

  if (updateThingspeakCounter > 40000)
  {
    updateThinkSpeak();
    updateThingspeakCounter = 0;
  }
  updateThingspeakCounter++;
  client.handle();
}