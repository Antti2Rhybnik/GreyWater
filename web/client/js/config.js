var timeOfGraphReload = 3000;//обновление данных на графиках
var timeOfSensorsReload = 1000;//обновление данных сенсоров
var timeOfSensorPanelReload = 5000;//обновляем панель "Датчики"
var timeOfControlPanelReload = 1000;//обновляем "контрольная панель"
var messagesRequestName = "temp.json";// сообщения о значениях
var sensorsRequestName = "body.json";// сообщение с объектами сенсоров
var sensorsOnPanel;//сенсоры выведенные на панель(id)
var sensorsOnGraph;//сенсоры выведенные на графики(id)
var sensorsValue;//значение сенсоров из запроса - messagesRequestName
var timerControlId;//
var sensors = new Array(); //массив id полученных сенсоров
var recievedResponse; //массив объектов сенсоров
var maxGraph = 2;//количество графиков
var panelOpen = false; //панель открыта или закрыта //костыль убрать, все стереть и выкинуть