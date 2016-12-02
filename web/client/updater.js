var sensors = new Array;

function drawSensor() {
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

      for(var j = 0; j < sensors.length; j++){
        data.series[j] = new Array;
        for(var i = 0; i < myData.length; i++){ 
          if(sensors[j] == myData[i].vsensor_id){
            var tmp = {x: myData[i].value, y: myData[i].timestamp};
            data.series[j].push(tmp);
          }
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

function setup(){
	document.getElementById('ass').style.left = document.body.clientWidth * 0.05 +  'px';
	document.getElementById('ass').style.top = document.body.clientHeight * 0.2 +  'px';
	document.getElementById('ass').style.width = document.body.clientWidth * 0.9+  'px';
	document.getElementById('ass').style.height = parseInt(document.getElementById('ass').style.width)/2.875 + 'px';

	document.getElementById('1').style.left = parseInt(document.getElementById('ass').style.left)+ 
	parseInt(document.getElementById('ass').width) * 0.6 + 'px';
	document.getElementById('1').style.top = parseInt(document.getElementById('ass').style.top)+ 
	parseInt(document.getElementById('ass').height) * 0.5 + 'px';

	document.getElementById('2').style.left = parseInt(document.getElementById('ass').style.left)+ 
	parseInt(document.getElementById('ass').width) * 0.3 + 'px';
	document.getElementById('2').style.top = parseInt(document.getElementById('ass').style.top)+ 
	parseInt(document.getElementById('ass').height) * 0.3 + 'px';

	document.getElementById('graph').style.left = document.body.clientWidth * 0.05 +  'px';
	document.getElementById('graph').style.top = (parseInt(document.getElementById('ass').style.top) 
		+ parseInt(document.body.clientHeight * 0.2) 
		+ parseInt(document.getElementById('ass').style.height)) +  'px';
	document.getElementById('graph').style.width = document.body.clientWidth * 0.9+  'px';
	document.getElementById('graph').style.height = parseInt(document.getElementById('graph').style.width)/2.875 + 'px';

	var fs, width=document.body.clientWidth;
	if(width >= 250) fs = "10px";
	if(width >= 500) fs = "15px";
	if(width >= 1000) fs = "20px";
	if(width >= 1500) fs = "25px";
	document.body.style.fontSize = fs;
}
function init() {
	window.onload = window.onresize = setup;
}

function agent(v) { 
	return(Math.max(navigator.userAgent.toLowerCase().indexOf(v),0)); 
}
function xy(e,v) { 
	return(v?(agent('msie')?event.clientY+document.body.scrollTop:e.pageY):(agent('msie')?event.clientX+document.body.scrollTop:e.pageX)); 
}
function dragOBJ(d,e) {
    function drag(e) { 
    	if(!stop) { 
    		d.style.top=(tX=xy(e,1)+oY-eY+'px'); 
    		d.style.left=(tY=xy(e)+oX-eX+'px'); 
    	}
    }

    var oX=parseInt(d.style.left),oY=parseInt(d.style.top),eX=xy(e),eY=xy(e,1),tX,tY,stop;

    document.onmousemove=drag; document.onmouseup=function(){ 
    	stop=1; 
    	document.onmousemove=''; 
    	document.onmouseup='';
    	
      var left = parseInt(document.getElementById('graph').style.left),
    		top = parseInt(document.getElementById('graph').style.top),
    		width = parseInt(document.getElementById('graph').style.width),
    		height = parseInt(document.getElementById('graph').style.height);
    	
      var curX = parseInt(d.style.left),
    		curY = parseInt(d.style.top);

    	if (curX > left && curX < (left + width) && curY > top && curY < (top + height)){
		      sensors.push(d.id);
    		  drawSensor();
        } else {
    		  drawSensor("-1");
        }

    	setup();
    };
}
