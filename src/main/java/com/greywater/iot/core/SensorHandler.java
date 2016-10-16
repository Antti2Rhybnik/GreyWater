package com.greywater.iot.core;


public class SensorHandler implements Runnable {

    Long sensorID;
    public static String someThingEvent = "---";

    public SensorHandler(Long sensorID) {
        this.sensorID = sensorID;
    }


    @Override
    public void run() {

        // TODO: требует доработки
        // получаем предикаты от сенсора, который вызвал "HTTP-прерывание" в виде HTTP-запроса
        Sensor s = Core.allSensors.get(sensorID.intValue() - 1);

        // просчитываем предикаты
        s.getPredicates().forEach(p -> {
           boolean res = p.eval();
           if (res) {
               // если он активизировался, то небольшое сообщение - just for test
               someThingEvent = p.toString() + " activated on sensor with id:" + sensorID + " at " + s.getActualTimestamp();
               System.out.println(someThingEvent);
           }
            System.out.println(res);
        });

    }

}
