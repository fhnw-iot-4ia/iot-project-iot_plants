package ch.fhnw.iot.connectedPlants.raspberry.services;

import org.apache.http.HttpException;

import java.io.IOException;

public interface Service {

    void runService() throws IOException, HttpException;
}
