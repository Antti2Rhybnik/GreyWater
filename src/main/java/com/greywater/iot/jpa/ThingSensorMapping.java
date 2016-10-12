package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 * Created by alexander on 10/13/16.
 */

@Entity
@Table(name = "THING_SENSOR_MAPPING", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT s from MessageTableEntity s")
@XmlRootElement
public class ThingSensorMapping {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name="BINDED_SENSOR")
    private String bindedSensor;








}
