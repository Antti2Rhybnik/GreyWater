var gauge;
var gaugeData;
var gaugeOptions;
function initGraphics()
{
	google.charts.load('current', {'packages':['gauge','corechart','table']});
	google.charts.setOnLoadCallback(drawGauge);
	gaugeOptions = {min: 0, max: 480000, yellowFrom: 300000, yellowTo: 350000,
	redFrom: 400000, redTo: 480000, minorTicks: 5};

	function drawGauge() {
		gaugeData = new google.visualization.DataTable();
		gaugeData.addColumn('number', 'Light');
		gaugeData.addColumn('number', 'Water');
		gaugeData.addColumn('number', 'Temperature');
		gaugeData.addRows(1);
		gaugeData.setCell(0, 0, 120);
		gaugeData.setCell(0, 1, 120);
		gaugeData.setCell(0, 2, 120);

		gauge = new google.visualization.Gauge(document.getElementById('gauge_div'));
		gauge.draw(gaugeData, gaugeOptions)
		{
	      	var data = new google.visualization.DataTable();
	      	data.addColumn('string', 'Topping');
	      	data.addColumn('number', 'Slices');
	      	data.addRows([
	      		['Mushrooms', 3],
	      		['Onions', 1],
	      		['Olives', 1],
	      		['Zucchini', 1],
	      		['Pepperoni', 2]
	      		]);
	      	var options = {'title':'How Much Pizza I Ate Last Night',
	      	'width':400,
	      	'height':300};
	      	
	      	var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
	      	chart.draw(data, options);
	    }
	    {
	    	var cssClassNames = {
	    		'headerRow': 'italic-darkblue-font large-font bold-font',
	      		'tableRow': '',
	      		'oddTableRow': 'beige-background',
	      		'selectedTableRow': 'orange-background large-font',
	      		'hoverTableRow': '',
	      		'headerCell': 'gold-border',
	      		'tableCell': '',
	      		'rowNumberCell': 'underline-blue-font'
	      	};

	      	var options = {'showRowNumber': true, 'allowHtml': true, 'cssClassNames': cssClassNames};

	      	var data = new google.visualization.DataTable();
	      	data.addColumn('string', 'Name');
	      	data.addColumn('number', 'Salary');
	      	data.addColumn('boolean', 'Full Time');
	      	data.addRows(5);
	      	data.setCell(0, 0, 'John');
	      	data.setCell(0, 1, 10000, '$10,000', {'className': 'bold-green-font large-font right-text'});
	      	data.setCell(0, 2, true, {'style': 'background-color: red;'});
	      	data.setCell(1, 0, 'Mary', null, {'className': 'bold-font'});
	      	data.setCell(1, 1, 25000, '$25,000', {'className': 'bold-font right-text'});
	      	data.setCell(1, 2, true, {'className': 'bold-font'});
	      	data.setCell(2, 0, 'Steve', null, {'className': 'deeppink-border'});
	      	data.setCell(2, 1, 8000, '$8,000', {'className': 'deeppink-border right-text'});
	      	data.setCell(2, 2, false, null);
	      	data.setCell(3, 0, 'Ellen', null, {'className': 'italic-purple-font large-font'});
	      	data.setCell(3, 1, 20000, '$20,000');
	      	data.setCell(3, 2, true);
	      	data.setCell(4, 0, 'Mike');
	      	data.setCell(4, 1, 12000, '$12,000');
	      	data.setCell(4, 2, false);
	      	var container = document.getElementById('table_div');
	      	var table = new google.visualization.Table(container);
	      	table.draw(data, options);
	      	table.setSelection([{'row': 4}]);
	    }
	}
}

function changeTemp(dir) {
	gaugeData.setValue(0, 0, gaugeData.getValue(0, 0) + dir * 25);
	gaugeData.setValue(0, 1, gaugeData.getValue(0, 1) + dir * 20);
	gauge.draw(gaugeData, gaugeOptions);
}

function updateSpeed() {


		var xhrLight = new XMLHttpRequest();
	    //var file;

		xhrLight.open("GET", "/GreyWater/rest/api/getNodeState?id=_" + 6, true);
		xhrLight.onreadystatechange = function () {
	        if (xhrLight.readyState != 4) return;

	        var fileLight = xhrLight.responseText;
	     	var lightData = parseFloat(fileLight*1000);

	     	console.log("light " + lightData);
	     	gaugeData.setValue(0, 0, lightData);

			gauge.draw(gaugeData, gaugeOptions);
	    };

	xhrLight.send();

	var xhrWater = new XMLHttpRequest();
	xhrWater.open("GET", "/GreyWater/rest/api/getNodeState?id=_" + 2, true);
	xhrWater.onreadystatechange = function () {
		if (xhrWater.readyState != 4) return;

		var fileWater = xhrWater.responseText;
		var waterData = parseFloat(fileWater*1000);

		console.log("water " + waterData);
		gaugeData.setValue(0, 1, waterData);

		gauge.draw(gaugeData, gaugeOptions);
	};

	xhrWater.send();



	var xhrLight2 = new XMLHttpRequest();
	//var file;

	xhrLight2.open("GET", "/GreyWater/rest/api/getNodeState?id=_" + 8, true);
	xhrLight2.onreadystatechange = function () {
		if (xhrLight2.readyState != 4) return;

		var fileLight2 = xhrLight2.responseText;
		var lightData2 = parseFloat(fileLight2*1000);

		console.log("light " + lightData2);
		gaugeData.setValue(0, 2, lightData2);

		gauge.draw(gaugeData, gaugeOptions);
	};

	xhrLight2.send();

}

setInterval(updateSpeed, 1000);