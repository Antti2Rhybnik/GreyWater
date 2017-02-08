//обновить и пихнуть данные из файла в графики
function drawSensorGraph() {
    updateSensorsData();

    console.log("drawSensorGraph");
       for (var j = 0; j < sensorsOnGraph.length; j++) { //id датчиков, выведенных на график
        var data = {
            series: [
                []
            ]
        };
        data.series[0] = new Array;
        for (var i = 0; i < sensorsValue.length; i++) {
            if (sensorsOnGraph[j] == sensorsValue[i].sensorId) {
                var tmp = { x: sensorsValue[i].gCreated, y: sensorsValue[i].sensorValue };
                data.series[0].push(tmp);
            }
        }
        var options = {
            height: 200,
            axisX: {
                type: Chartist.AutoScaleAxis,
                onlyInteger: true
            }
        };
        new Chartist.Line('.graph' + j, data, options);
    }
}

setInterval(drawSensorGraph, timeOfGraphReload);
