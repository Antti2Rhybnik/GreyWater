package com.greywater.iot.utils;


/*Это мастерский костыль имени Андрея. Да, именно так работают веб приложения*/
public class AwesomeHTMLBuilder {

    public static String getAwesomeHtml(String t1, String t2, String color) {
        return "<!DOCTYPE HTML>" +
                "<html lang=\"ru-ru\">" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<title>GreyWater</title>" +
                "</head>" +
                "<body>" +
                "<div style=\"width: 100%; height: 100%; position: absolute; background: "
                + color + "; right: 0; left: 0; top: 0; bottom: 0;\">" +
                "<div style=\"position: absolute; right: 60%; left: 10%; top: 10%; bottom: 10%;\">"+
                t1 +"</div><div style=\"position: absolute; right: 10%; left: 60%; top: 10%; bottom: 10%;\">"
                + t2 +"</div>" +
                "</div>" +
                "</body></html>";
    }
    public static String getAwesomeHtmlWithPhoto(String t1, String t2, String color) {
        return "<!DOCTYPE HTML>" +
                "<html lang=\"ru-ru\">" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<title>GreyWater</title>" +
                "</head>" +
                "<body background=\""+color+"\" >" +
                "<div style=\"width: 100%; height: 100%; position: absolute;  right: 0; left: 0; top: 0; bottom: 0;\">" +
                "<div style=\"position: absolute; right: 60%; left: 10%; top: 10%; bottom: 10%;\">"+
                t1 +"</div><div style=\"position: absolute; right: 10%; left: 60%; top: 10%; bottom: 10%;\">"
                + t2 +"</div></div></body></html>";
    }
    public static String getMyFailureInClient(){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <title>React Tutorial</title>\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js\">\n" +
                "    <script src=\"https://unpkg.com/react@15.3.2/dist/react.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/react-dom@15.3.2/dist/react-dom.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/babel-standalone@6.15.0/babel.min.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/remarkable@1.7.1/dist/remarkable.min.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/axios/dist/axios.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\"></div>\n" +
                "<script type=\"text/babel\">\n" +
                "    var baseHanaUrl = 'https://greywaterp1942206778trial.hanatrial.ondemand.com/GreyWater/';\n" +
                "    var urlToThresholder = 'GreyWater/rest/api/thres';\n" +
                "    var fullUrl = baseHanaUrl + urlToThresholder;\n" +
                "    var AutoUpdate = React.createClass({\n" +
                "        getDefaultProps: function() {\n" +
                "            return {\n" +
                "                url: null,\n" +
                "                interval: 3000,\n" +
                "                callback: null\n" +
                "            };\n" +
                "        },\n" +
                "        fetchData: function(url) {\n" +
                "            $.ajax({\n" +
                "                url: this.props.url,\n" +
                "                success: function(data) {\n" +
                "                    this.props.callback(data);\n" +
                "                }.bind(this)\n" +
                "            });\n" +
                "        },\n" +
                "        componentDidMount: function () {\n" +
                "            setInterval(this.fetchData, parseInt(this.props.interval));\n" +
                "        },\n" +
                "        render: function() {\n" +
                "            return (<div className=\"loader\" />);\n" +
                "        }\n" +
                "    });\n" +
                "    var testComp = React.createClass({\n" +
                "        render: function () {\n" +
                "            return (\n" +
                "                    <div>\n" +
                "                        <AutoUpdate url=fullUrl interval=\"2000\"\n" +
                "                                    callback={this.updateMe}/>\n" +
                "                        <p>{this.props.view}</p>\n" +
                "                    </div>\n" +
                "\n" +
                "            )\n" +
                "        },\n" +
                "        updateMe: function (data) {\n" +
                "            this.props.view =\n" +
                "                    <ol>\n" +
                "                {data.map((result) => (\n" +
                "                        <li >result</li>\n" +
                "                ))}\n" +
                "            </ol>;\n" +
                "\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "    });\n" +
                "\n" +
                "    ReactDOM.render(\n" +
                "            <testComp/>,\n" +
                "            document.getElementById('content')\n" +
                "    );\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
    }

}
