package com.greywater.iot.jpa;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name = "THING", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT s from MessageTableEntity s")
@XmlRootElement
public class Thing {
}
