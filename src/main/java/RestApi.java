import java.io.File;

public class RestApi {


    private String charset = "UTF-8";
    private String param = "value";
    File textFile = new File("/path/to/file.txt");

    String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    static String CRLF = "\r\n"; // Line separator required by multipart/form-data.


        // writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
//        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
//        writer.append(CRLF).append(param).append(CRLF).flush();

    static String GET_request(String filename)  {
        StringBuilder request = new StringBuilder();
        request.append("GET /" + filename + " HTTP/1.1");
            //    .append("Host: localhost").append(CRLF);
            //    .append("Content-Type: plain/text").append(CRLF);
        return request.toString();
    }

    static String GET_response(String filename)  {

        return filename;
    }

    static String POST_request(String filename)  {
        StringBuilder request = new StringBuilder();
        request.append("GET /" + filename + " HTTP/1.1");
        //    .append("Host: localhost").append(CRLF);
        //    .append("Content-Type: plain/text").append(CRLF);
        return request.toString();
    }
}
