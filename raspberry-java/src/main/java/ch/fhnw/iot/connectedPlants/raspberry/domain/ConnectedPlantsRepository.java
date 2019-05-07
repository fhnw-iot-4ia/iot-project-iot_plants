package ch.fhnw.iot.connectedPlants.raspberry.domain;

import ch.fhnw.iot.connectedPlants.raspberry.domain.ConnectedPlants;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConnectedPlantsRepository extends MongoRepository<ConnectedPlants, String> {

//    List<ConnectedPlants> findByTitle(String title);

}
