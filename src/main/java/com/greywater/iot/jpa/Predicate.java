package com.greywater.iot.jpa;


import javax.persistence.*;

@Entity
@Table(name="PREDICATES_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
public class Predicate {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pred_gen")
    @SequenceGenerator(name = "pred_gen", sequenceName = "pred_seq", initialValue = 100, allocationSize = 50)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VALUE")
    private Double value;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SENSOR_ID")
    private Sensor sensor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Sensor getSensorEntityEntity() {
        return sensor;
    }

    public void setSensorEntityEntity(Sensor sensor) {
        this.sensor = sensor;
    }
}
