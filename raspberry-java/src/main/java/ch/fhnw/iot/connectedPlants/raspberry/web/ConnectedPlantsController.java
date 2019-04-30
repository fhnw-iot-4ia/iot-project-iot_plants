package ch.fhnw.iot.connectedPlants.raspberry.web;

import ch.fhnw.iot.connectedPlants.raspberry.entity.ConnectedPlants;
import ch.fhnw.iot.connectedPlants.raspberry.entity.ConnectedPlantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/connected-plants")
public class ConnectedPlantsController {

    @Autowired
    private ConnectedPlantsRepository connectedPlantRepository;

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<ConnectedPlants>> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<ConnectedPlants> result = connectedPlantRepository.findAll(sort);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
