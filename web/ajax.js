

var counter = 0;



function update() {

    var xhr = new XMLHttpRequest();

    var host = "https://greywaterp1942206778trial.hanatrial.ondemand.com/GreyWater";
    xhr.open('GET', '/GreyWater/rest/api/thres', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();


    xhr.onreadystatechange = function() {

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