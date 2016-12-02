var counter = 0;

function update() {
    var xhr = new XMLHttpRequest();
    var file;
    xhr.open("GET", "temp.json", true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState != 4) return;


        var recievedResponse = JSON.parse(xhr.responseText);
        var info = document.getElementById('selecter');

        for (var i = 0; i < recievedResponse.length; ++i){
            var flag = "false";
            var res = "";
            for (var j = 0; j < info.options.length; ++j){
                res += info.options[j].value;
                if (recievedResponse[i].vsensor_id == info.options[j].value){
                    flag = "true";
                    break;
                }
            }

            if (flag == "false"){
                var json = document.createElement("option");
                json.value = recievedResponse[i].vsensor_id;
                json.text = "Sensor " + recievedResponse[i].vsensor_id;
                json.type = "button";
                json.onclick = drawSensor;

                //alert(json.onclick);

                info.appendChild(json);
            }
        }

        counter++;
    }

    xhr.send();

}


setInterval(update, 1000);
