package ch.fhnw.iot.connectedPlants.raspberry.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;

@Document(collection = "connected_plants")
public class ConnectedPlants {
    @Id
    private String _id;

    @Min(0)
    private Double threshold;

    @Min(0)
    private Double measuredMoistureValue;


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Double getMeasuredMoistureValue() {
        return measuredMoistureValue;
    }

    public void setMeasuredMoistureValue(Double measuredMoistureValue) {
        this.measuredMoistureValue = measuredMoistureValue;
    }
}
