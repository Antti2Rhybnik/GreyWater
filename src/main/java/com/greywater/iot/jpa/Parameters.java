package com.greywater.iot.jpa;


import javax.persistence.*;
import java.util.UUID;

/*Таблица конфигурации Sensor.
* Как можно заметить здесь есть MIN, MAX, возможно будет что-то еще*/
@Entity
@Table(name="PARAMETERS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
public class Parameters {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "param_gen")
    @SequenceGenerator(name = "param_gen", sequenceName = "param_seq", initialValue = 100, allocationSize = 50)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MAX", nullable = true)
    private Double max;

    @Column(name = "MIN", nullable = true)
    private Double min;

    @OneToOne(mappedBy = "parameters")
    private Sensor sensor;

    public Parameters() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
