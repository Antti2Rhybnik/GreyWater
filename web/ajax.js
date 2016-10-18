var counter = 0;


function update() {

    var xhr = new XMLHttpRequest();

    xhr.open('GET', '/GreyWater/rest/api/thres', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    function setImage(thisImg) {
        elem = document.getElementById('imageDiv');
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
        var img = document.createElement("IMG");
        img.src = thisImg;
        document.getElementById('imageDiv').appendChild(img);
    }

    xhr.onreadystatechange = function () {

        if (xhr.readyState != 4) return;

        var recievedResponse = JSON.parse(xhr.responseText);

        var info = document.getElementById('info');
        var body = document.getElementById('body');

        if (recievedResponse["state"] == "ALARM OUT OF RANGE") {
            setImage("https://i.ytimg.com/vi/PeVgR4gM1kA/hqdefault.jpg");
            info.setAttribute("class", "bad");
            body.setAttribute("bgcolor", "#917D74");
        } else {
            setImage("http://www.opengaz.ru/sites/www.opengaz.ru/files/u353/yzaj8_9uiie.jpg");
            info.setAttribute("class", "ok");
            body.setAttribute("bgcolor", "#8fbc8f");
        }


        counter++;
        var json = document.createTextNode(xhr.responseText + " - " + counter);

        while (info.firstChild) {
            info.removeChild(info.firstChild);
        }
        info.appendChild(json);

    }

}


setInterval(update, 1000);