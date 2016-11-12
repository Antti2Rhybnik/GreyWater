package com.greywater.iot.jpa;

import javax.persistence.*;
import java.util.UUID;

/*Таблица конфигурации Sensor.
* Как можно заметить здесь есть MIN, MAX, возможно будет что-то еще*/
@Entity
@Table(name="PARAMETERS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
public class Parameters {

    @Id
    @Column(name = "PARAMETERS_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "MAX", nullable = true)
    private Double max;

    @Column(name = "MIN", nullable = true)
    private Double min;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VSENSOR_ID")
    private VirtualSensor virtualSensor;

    public Parameters() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "id='" + id + '\'' +
                ", max=" + max +
                ", min=" + min +
                '}';
    }
}
