package ch.fhnw.iot.connectedPlants.raspberry.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConnectedPlantsRepository extends MongoRepository<ConnectedPlants, String> {

//    List<ConnectedPlants> findByTitle(String title);

}
