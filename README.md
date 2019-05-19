# IoT Engineering
## Project Connected Plants
A moisture and environmental sensor for your plants to get a information, whenever there is need for action. 

## Introduction
This project is part of the [IoT Engineering](../../../fhnw-iot) course.

* 2-person teams, building an IoT system.
* 32 hours of work per person, 1 prototype.
* 10' presentation of the project at Demo Day.
* Slides, source code and setup steps on GitHub.
* Both team members are able to explain the project.

### Team members
* [@Prinzenrolle90], Mischa Imbiscuso
* [@cudemo], Vito Cudemo

## Deliverables
The following deliverables are mandatory.

### Source code
Source code consists of: Arduino device for sensor information, Java Service running on Tomcat with REST API and Angular Webserver https://plants.imbiscuso.ch

[PlantsFieldSensor for ESP8266](esp/PlantsFieldSensor/PlantsFieldSensor.ino)

[Plants Service Java used on Raspberry or cloud](/raspberry-java/src/main/java/ch/fhnw/iot/connectedPlants/raspberry/) 

[Plants Webservice used on Webserver](/connected-plants-web/src)

Furthermore, we implemented an own MQTT Broker on a Raspberry Pi 3+, running hassio Home Assistant and DuckDNS Service for serving mqtt://mqtt.cudemo.ch:16290. The MQTT Service has been setup to only allow authenticated connections. User: esp / raspberry, password: iotmqtt

### Reference model / Domain overview

![Reference Model, Connected Plants](Images/Plants_ReferenceModel.png)


##### Setup software
* ESP8266

1) Edit [secrets.h](esp/PlantsFieldSensor/secrets.h) to set your ThingSpeak API Keys and WIFI Settings 
2) Attach sensors and actuator to ESP8266 
* Sensors and Actuators:

Type | Name | Pin ESP8266
--- | --- | ---
Sensor | DHT11 Temperature & Humidity Sensor | D2 -> 2
Sensor | Grove Moisture Sensor | A0
Actuator | Chainable RGB LED | D4, D5 -> 0, 15
(Future) Actuator | PUM

3) Attach to energy source and check ThingSpeak. Otherwise enable debug mode on ESP source-code

* Plants Service (can be run on Raspberry)

###TODO IMBISCUSO

### Presentation
4-slide presentation, PDF format, committed to (this) project repo.

[MY_TEAM_PROJECT_PRESENTATION.pdf](MY_TEAM_PROJECT_PRESENTATION.pdf)

1) Use-case of your project.
2) Reference model of your project.
3) Single slide interface documentation.
4) Issues you faced, how you solved them.

### Live demo
Working end-to-end prototype, "device to cloud", part of your 10' presentation.

[Connected Plants Webservice - https://plants.imbiscuso.ch](https://plants.imbiscuso.ch)

1) ESP8266 is used in field to monitor plants.
2) Every x minutes, the ESP sends the sensor data to ThingSpeak API.
3) The Webservice (written in Angular) displays all relevant information to the user and gives the possibility to set the threshold for moisture-sensor based on plant.
4) The Java Service takes the threshold and monitors the sensor information from ThingSpeak API. 
5) If the threshold is reached, the Service sents the "Give some water" signal to the ESP using MQTT service, which was set up on a Raspberry Pi 3+ running hassio home assistant: mqtt.cudemo.ch
6) Furthermore, the service sents an SMS to the users.
7) The ESP then reacts to the event. 


## Submission deadline
Commit and push to (this) project repo before Demo Day, _03.06.2019, 00:00_.

## Bibliography
* https://www.instructables.com/id/ESP8266-Soil-Moisture-Sensor-With-Arduino-IDE/