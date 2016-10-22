package com.greywater.iot.jpa;


import com.greywater.iot.predicates.PredicateDelegate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="PREDICATES_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
public class Predicate {

    // TODO: fix ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pred_gen")
    @SequenceGenerator(name = "pred_gen", sequenceName = "pred_seq", initialValue = 100, allocationSize = 50)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VALUE")
    private Double value;

    // виртуальный сенсор, для которого этот "предикат" предназначен
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "VIRTUAL_SENSOR_ID")
    private VSensor vsensor;

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

    // делегат предиката, делегируемый для вычисления предиката
    transient PredicateDelegate predicateDelegate;

    public PredicateDelegate getPredicateDelegate() {
        return predicateDelegate;
    }

    public void setPredicateDelegate(PredicateDelegate pd) {
        this.predicateDelegate = pd;
    }

    // непосредственно делегирующий метод
    public boolean eval() {
        return predicateDelegate.eval();
    }
}
