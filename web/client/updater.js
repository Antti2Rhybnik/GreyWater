var sensors = new Array;
var numOfSensors = new Array;
var countSens = 3;
function tree_toggle(event) {
    event = event || window.event;
    var clickedElem = event.target || event.srcElement;

    if (!hasClass(clickedElem, 'Expand')) {
        return;
    }

    var node = clickedElem.parentNode;
    if (hasClass(node, 'ExpandLeaf')) {
        return;
    }

    var newClass = hasClass(node, 'ExpandOpen') ? 'ExpandClosed' : 'ExpandOpen';
    var re = /(^|\s)(ExpandOpen|ExpandClosed)(\s|$)/;
    node.className = node.className.replace(re, '$1' + newClass + '$3');
}


function hasClass(elem, className) {
    return new RegExp("(^|\\s)" + className + "(\\s|$)").test(elem.className);
}

function chooseChildren(elem) {
    for (var j = 0; j < document.getElementsByTagName('ul').length - 1; j++) {
        var child = document.getElementsByTagName('ul')[j].childNodes;
        if (elem.checked) {
            for (var i = 1; i < child.length - 1; i++) {
                flag = false;
                if (child[i].getElementsByTagName('input')[0].checked & elem == child[i].getElementsByTagName('input')[0]) {
                    flag = true;
                }
                if (flag) {
                    for (var k = 0; k < child[i].getElementsByTagName('input').length; k++)
                        child[i].getElementsByTagName('input')[k].checked = true;
                }
            }
        } else {
            for (var i = 1; i < child.length - 1; i++) {
                flag = false;
                if (child[i].getElementsByTagName('input')[0] == elem) {
                    flag = true;
                }
                if (flag) {
                    for (var k = 0; k < child[i].getElementsByTagName('input').length; k++)
                        child[i].getElementsByTagName('input')[k].checked = false;
                }
            }
        }
    }
    numOfSensors.length = 0;
    for (var k = 1; k < countSens; k++)
        if (document.getElementById("check_" + k).checked)
            numOfSensors.push(k);
    drawSensor(numOfSensors);
    curSensor = numOfSensors[0];
}

var allSensors = new Array;
var curSensor = 1;

function drawSensor(sensorID) {
    var numOfPoints = 4;
    var rawFile = new XMLHttpRequest();
    var file;
    var sen  = curSensor;
    var send = "/GreyWater/rest/api/sensorValues?limit=30&id=" + sen;
    rawFile.open("GET",send, true);
    console.log(send);
    rawFile.onreadystatechange = function () {
        if (rawFile.readyState === 4) {
            file = new String(rawFile.responseText);

            var myData = JSON.parse(rawFile.responseText);
            var data = {
                series: [[]]
            };
            for (var i = 0; i < myData.length; i++) {
                if (sensorID == myData[i].vsensor_id) {
                    var tmp = {x: i, y: myData[i].sensorValue};
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

setInterval(drawSensor, 1000);

function chooseInList(id) {
    document.getElementById('check_' + id).checked = !document.getElementById('check_' + id).checked;
}

function giveNames() {
    var string = '', ids = '';
    var child = document.getElementsByTagName('div');
    for (var i = 1; i < child.length; i++)
        if (child[i].parentNode.getElementsByTagName('input')[0].checked) {
            string = child[i].innerHTML;
            if (string.search('Sensor') != -1) {
                string = string.replace(/\s/g, '');
                string = string[string.length - 1];
                ids += string + ' ';
            }
        }
    alert(ids);

}

function setup() {
    document.getElementById('ass').style.left = document.body.clientWidth * 0.05 + 'px';
    document.getElementById('ass').style.top = '0px';
    document.getElementById('ass').style.width = document.body.clientWidth * 0.9 + 'px';
    document.getElementById('ass').style.height = parseInt(document.getElementById('ass').style.width) / 2.875 + 'px';

    document.getElementById('1').style.left = parseInt(document.getElementById('ass').style.left) +
        parseInt(document.getElementById('ass').width) * 0.6 + 'px';
    document.getElementById('1').style.top = parseInt(document.getElementById('ass').style.top) +
        parseInt(document.getElementById('ass').height) * 0.5 + 'px';

    document.getElementById('2').style.left = parseInt(document.getElementById('ass').style.left) +
        parseInt(document.getElementById('ass').width) * 0.3 + 'px';
    document.getElementById('2').style.top = parseInt(document.getElementById('ass').style.top) +
        parseInt(document.getElementById('ass').height) * 0.3 + 'px';

    document.getElementById('graph').style.left = document.body.clientWidth * 0.05 + 'px';
    document.getElementById('graph').style.top = (parseInt(document.getElementById('ass').style.top)
        + parseInt(document.getElementById('ass').style.height)) + 'px';
    document.getElementById('graph').style.width = document.body.clientWidth * 0.9 + 'px';
    document.getElementById('graph').style.height = parseInt(document.getElementById('graph').style.width) / 2.875 + 'px';

    document.getElementById('gauge_div').style.left = document.body.clientWidth * 0.05 + 'px';
    document.getElementById('gauge_div').style.top = (parseInt(document.getElementById('graph').style.top)
        + parseInt(document.getElementById('graph').style.height)) + 'px';

    document.getElementById('chart_div').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('chart_div').style.top = (parseInt(document.getElementById('gauge_div').style.top)
        + parseInt(document.getElementById('gauge_div').style.height)) + 'px';


    document.getElementById('table_div').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('table_div').style.top = (parseInt(document.getElementById('chart_div').style.top)
        + parseInt(document.getElementById('chart_div').style.height)) + 'px';

    document.getElementById('host1').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host1').style.top = (parseInt(document.getElementById('table_div').style.top)
        + parseInt(document.getElementById('table_div').style.height)) + 'px';
    document.getElementById('host1Cpu').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host1Cpu').style.top = (parseInt(document.getElementById('host1').style.top)
        + parseInt(document.getElementById('host1').style.height)) + 'px';

    document.getElementById('host2').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host2').style.top = (parseInt(document.getElementById('host1Cpu').style.top)
        + parseInt(document.getElementById('host1Cpu').style.height)) + 'px';
    document.getElementById('host2Cpu').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host2Cpu').style.top = (parseInt(document.getElementById('host2').style.top)
        + parseInt(document.getElementById('host2').style.height)) + 'px';

    document.getElementById('host3').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host3').style.top = (parseInt(document.getElementById('host2Cpu').style.top)
        + parseInt(document.getElementById('host2Cpu').style.height)) + 'px';
    document.getElementById('host3Cpu').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host3Cpu').style.top = (parseInt(document.getElementById('host3').style.top)
        + parseInt(document.getElementById('host3').style.height)) + 'px';

    document.getElementById('host4').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host4').style.top = (parseInt(document.getElementById('host3Cpu').style.top)
        + parseInt(document.getElementById('host3Cpu').style.height)) + 'px';
    document.getElementById('host4Cpu').style.left = document.body.clientWidth * 0.15 + 'px';
    document.getElementById('host4Cpu').style.top = (parseInt(document.getElementById('host4').style.top)
        + parseInt(document.getElementById('host4').style.height)) + 'px';


    var fs, width = document.body.clientWidth;
    if (width >= 250) fs = "10px";
    if (width >= 500) fs = "15px";
    if (width >= 1000) fs = "20px";
    if (width >= 1500) fs = "25px";
    document.body.style.fontSize = fs;
}
function init() {
    window.onload = window.onresize = setup;
}

function agent(v) {
    return (Math.max(navigator.userAgent.toLowerCase().indexOf(v), 0));
}
function xy(e, v) {
    return (v ? (agent('msie') ? event.clientY + document.body.scrollTop : e.pageY) : (agent('msie') ? event.clientX + document.body.scrollTop : e.pageX));
}
function dragOBJ(d, e) {
    function drag(e) {
        if (!stop) {
            d.style.top = (tX = xy(e, 1) + oY - eY + 'px');
            d.style.left = (tY = xy(e) + oX - eX + 'px');
        }
    }

    var oX = parseInt(d.style.left), oY = parseInt(d.style.top), eX = xy(e), eY = xy(e, 1), tX, tY, stop;

    document.onmousemove = drag;
    document.onmouseup = function () {
        stop = 1;
        document.onmousemove = '';
        document.onmouseup = '';

        var left = parseInt(document.getElementById('graph').style.left),
            top = parseInt(document.getElementById('graph').style.top),
            width = parseInt(document.getElementById('graph').style.width),
            height = parseInt(document.getElementById('graph').style.height);

        var curX = parseInt(d.style.left),
            curY = parseInt(d.style.top);

        if (curX > left && curX < (left + width) && curY > top && curY < (top + height)) {
            sensors.push(d.id);
            curSensor = d.id;
            drawSensor();
        } else {
            drawSensor("-1");
        }

        setup();
    };
}
