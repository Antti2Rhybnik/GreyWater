function drawSensor(sensorID) {
  /*
  var info = document.getElementById('selecter');
  var sensorID = info[info.selectedIndex].value;*/
  var numOfPoints = 4;
  var rawFile = new XMLHttpRequest();
  var file;
  rawFile.open("GET", "temp.json", true);
  rawFile.onreadystatechange = function () {
    if(rawFile.readyState === 4) {
      file = new String(rawFile.responseText);
      var myData = JSON.parse(file);
      var data = {
        series: [[ ]]
      };
      for(var i = 0; i < myData.length; i++){
        if(sensorID == myData[i].vsensor_id){
          var tmp = {x: myData[i].value, y: myData[i].timestamp};
          data.series[0].push(tmp);
        }
      }
      var options = {
        axisX: {
          type: Chartist.AutoScaleAxis,
          onlyInteger: true
        },
        axisY: {
          low: 0
          //type: Chartist.FixedScaleAxis,
        },
      };
      new Chartist.Line('.ct-chart', data, options);
    }
  }
  
  rawFile.send();
}