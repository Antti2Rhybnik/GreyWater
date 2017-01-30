//подгрузить список сенсоров и чекбоксы/типы к ним
function updateSensorsPanel() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", sensorsRequestName, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState != 4) return;
        recievedResponse = JSON.parse(xhr.responseText);
        for (var i = 0; i < recievedResponse.length; i++) {
            var sensor_type = recievedResponse[i].parameters.sensor_type;
            var id = recievedResponse[i].parameters.sensor_id;
            var flag = true;
            for (var i = 0; i < sensors.length; i++)
                if (sensors[i] == id)
                    flag = false;

            if (flag) {
                sensors.push(id);

                var checkboxPanel = document.createElement("input");
                checkboxPanel.type = "checkbox";
                //checkboxPanel.setAttribute('val', "panel_" + id);
                checkboxPanel.value = "panel_" + id;

                var text = document.createElement("label");
                text.innerHTML = sensor_type;

                var tmp = document.createElement("LI");
                tmp.appendChild(text);
                tmp.appendChild(checkboxPanel);

                var checkboxGraph = document.createElement("input");
                checkboxGraph.type = "checkbox";
                checkboxGraph.value = "graph_" + id;
                tmp.appendChild(checkboxGraph);

                // выбор варианта вывода
                var opt1 = document.createElement("option");
                opt1.value = "meaning";
                opt1.innerHTML = "Значение";

                var opt2 = document.createElement("option");
                opt2.value = "fulling";
                opt2.innerHTML = "Наполнение";

                var sel = document.createElement("select");
                sel.id = "type_" + id;

                sel.appendChild(opt1);
                sel.appendChild(opt2);
                sel.className = "list";
                tmp.appendChild(sel);

                document.getElementById("insertSensorsHere").appendChild(tmp);
                $(document).ready(function() {
                    $('input[value=' + "panel_" + id + ']').tzCheckbox({ labels: ['ON', 'OFF'] });
                    $('input[value=' + "graph_" + id + ']').tzCheckbox({ labels: ['ON', 'OFF'] });
                });
            }
        }
    }
    xhr.send();
}

setInterval(updateSensorsPanel, timeOfSensorPanelReload); 
//не нравится это место обновляем статичный список - подумать убрать

//возвращает объект нужного сенсора по номеру id
function getSensor(num) {
    var i = 0;
    while (recievedResponse[i].parameters.sensor_id != num)
        i++;
    return recievedResponse[i];
}

//при клике на - "контрольная панель" добавляются строчки выбранных датчиков
function updateControlPanel() {
    $('#wrapperMeaning').empty();
    getValues();//запустить функцию, которая загрузит последние значения датчика
    for (var i = 0; i < sensorsOnPanel.length; i++) {
        var tmp = document.createElement("LI");
        var text = document.createElement("label");
        text.innerHTML = getSensor(sensorsOnPanel[i]).parameters.sensor_type;
        tmp.appendChild(text);
        var mean = document.createElement("label");
        switch (document.getElementById("type_" + sensorsOnPanel[i]).selectedIndex) {
            case 0: //значение
                mean.className = "good";
                mean.innerHTML = "<div id = \"meaning_" + sensorsOnPanel[i] + "\" class=\"meaning\">" + getLastMeanOfSensor(sensorsOnPanel[i]) + "</div>";
                break;
            case 1:
                var tmpBanka = document.createElement("div");
                tmpBanka.id = "banka_" + sensorsOnPanel[i];
                tmpBanka.className = "banka";
                //TODO высчитать процент от максимального значения
                tmpBanka.style.backgroundSize = 'auto, 100% ' +
                    getLastMeanOfSensor(sensorsOnPanel[i])+ '%';
                mean.appendChild(tmpBanka);
                mean.className = "small";
                break;
        }
        tmp.appendChild(mean);
        //единицы измерения
        var ed = document.createElement("label");
        ed.innerHTML = "<div  class=\"edIzm\">" + getSensor(sensorsOnPanel[i]).parameters.sensor_unit + "</div>";
        tmp.appendChild(ed);
        document.getElementById("wrapperMeaning").appendChild(tmp);
    }
}


//обновляет сразу все контрольную панель, вместо только значения датчиков
//TODO наверное стоит поправить на интервальные обновления каждого значения
function runControlPanel() { 
    clearInterval(timerControlId);
    timerControlId = setInterval(updateControlPanel, timeOfControlPanelReload);
}
