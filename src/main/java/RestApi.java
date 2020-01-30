import java.io.File;

public class RestApi {


    private String charset = "UTF-8";
    private String param = "value";
    File textFile = new File("/path/to/file.txt");

    String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    static String CRLF = "\r\n"; // Line separator required by multipart/form-data.


    static String getRequest(String filename)  {
        StringBuilder request = new StringBuilder();
        request.append("GET /" + filename + " HTTP/1.1");
            //    .append("Host: localhost").append(CRLF);
            //    .append("Content-Type: plain/text").append(CRLF);
        return request.toString();
    }


    static String postRequest(String filename)  {
        StringBuilder request = new StringBuilder();
        request.append("POST /" + filename + " HTTP/1.1");
        //    .append("Host: localhost").append(CRLF);
        //    .append("Content-Type: plain/text").append(CRLF);
        return request.toString();
    }

    static String postResponse(String filename)  {

        return "{\n" +
                "   \"name\": \"" + filename + "\"\n" +
                "}";
    }

}
