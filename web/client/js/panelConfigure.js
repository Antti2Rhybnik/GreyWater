// использование Math.round() даст неравномерное распределение!
function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

//псмотреть поставленные чекбоксы, обновить массивы sensorsOnPanel и sensorsOnGraph
function getValues() {
    sensorsOnPanel = new Array();
    sensorsOnGraph = new Array();
    var list = null;
    list = $(':checkbox:checked');
    for (var i = 0; i < list.length; i++)
        if (list[i].value.search('graph_') != -1)
            sensorsOnGraph.push(list[i].value.slice(6, list[i].value.length));
        else if (list[i].value.search('panel_') != -1)
        sensorsOnPanel.push(list[i].value.slice(6, list[i].length));
}


//перезагружает массив данных сенсоров
function updateSensorsData()
{
    var xhr = new XMLHttpRequest();
    xhr.open("GET", messagesRequestName, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            sensorsValue = JSON.parse(xhr.responseText);
        }
    }
    xhr.send();
}

setInterval(updateSensorsData, timeOfSensorsReload); //обовляем значения датчиков


//последнее значение сенсора заданного id
function getLastMeanOfSensor(num) {
    /*версия с рандомом для тестов*/
    //return sensorsValue[getRandomInt(0, sensorsValue.length - 1)].value;
    var i = sensorsValue.length;
    while (i > 0 && sensorsValue.sensorId != num)
        i--;
    return sensorsValue[i].sensorValue;
}
