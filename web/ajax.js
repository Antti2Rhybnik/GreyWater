

var counter = 0;



function update() {

    var xhr = new XMLHttpRequest();

    var host = "https://greywaterp1942206778trial.hanatrial.ondemand.com/GreyWater";
    xhr.open('GET', '/GreyWater/rest/api/thres', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    function setImage(thisImg) {
        elem = document.getElementById('imageDiv');
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
        var img = document.createElement("IMG");
        img.src =thisImg;
        document.getElementById('imageDiv').appendChild(img);
    }

    xhr.onreadystatechange = function() {
        var recievedResponse = JSON.parse(xhr.responseText);

        if(recievedResponse["state"]=="ALARM OUT OF RANGE"){
            setImage("https://i.ytimg.com/vi/PeVgR4gM1kA/hqdefault.jpg")
        }else{
            setImage("http://www.opengaz.ru/sites/www.opengaz.ru/files/u353/yzaj8_9uiie.jpg")
        }

        if (xhr.readyState != 4) return;

        counter++;
        var json = document.createTextNode(xhr.responseText + " - " + counter);
        var elem = document.getElementById('info');
        while (elem.firstChild) {
            elem.removeChild(elem.firstChild);
        }
        elem.appendChild(json);



    }

}


setInterval(update, 1000);