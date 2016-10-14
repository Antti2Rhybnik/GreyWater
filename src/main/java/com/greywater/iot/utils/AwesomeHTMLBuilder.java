package com.greywater.iot.utils;

/**
 * Created by antti on 14.10.16.
 */
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

}
