package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/*Самый интересный класс. Это штука. Ну как сказать...
  Сложно подобрать слово лучше. Мы пытались, честно.
* Этот класс абстрагируют реальные физические штуки, такие как емкость с водой.
* Возможно нагреватель или еще какую то вещь за которой нам следует наблюдать.*/
@Entity
@Table(name = "THINGS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@XmlRootElement
public class Thing {

    // === FIELDS === //
    @Id
    @Column(name = "THING_ID")
    @GeneratedValue
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "THING__VSENSOR",
            joinColumns = @JoinColumn(name = "THING_ID", referencedColumnName = "THING_ID"),
            inverseJoinColumns = @JoinColumn(name = "VSENSOR_ID", referencedColumnName = "VSENSOR_ID")
    )
    private List<VirtualSensor> virtualSensors;


    // === GETTERS AND SETTERS === //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VirtualSensor> getVirtualSensors() {
        return virtualSensors;
    }

    public void setSensors(List<VirtualSensor> virtualSensors) {
        this.virtualSensors = virtualSensors;
    }

    public void addVirtualSensor(VirtualSensor virtualSensor) {
        if (!virtualSensors.contains(virtualSensor)) {
            virtualSensors.add(virtualSensor);
        }
    }


    @Override
    public String toString() {
        return "Thing{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
