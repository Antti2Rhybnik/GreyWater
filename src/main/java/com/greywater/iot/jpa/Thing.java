package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name = "THING", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@XmlRootElement
public class Thing {

    @Id
    @Column(name = "ID")
    private UUID id = UUID.randomUUID();

    @OneToMany(mappedBy = "thingID")
    private List<Sensor> sensors;


}
