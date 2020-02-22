
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;


public class TaskServer {


    public static void main(String[] args) throws IOException {

        String resourcesPath = "/Users/user/IdeaProjects/MultiServer/src/main/resources"; // server image store


        try {

            ServerSocket server = new ServerSocket(4444);
            Socket client = server.accept();    // server socket opened
            System.out.println("Connection accepted.\n");

            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(client.getInputStream());

            //DataInputStream dis = new DataInputStream(client.getInputStream());     // to read HTTP request
            //DataOutputStream dos = new DataOutputStream(client.getOutputStream());     // to write text


            while (!client.isClosed()) {

                System.out.println("Server waiting for request...");
                String request = IOUtils.toString(bis, "UTF-8");
                System.out.println("request: " + request);

                if (request.startsWith("GET /")) {

                    String filename = request.substring(request.indexOf("/"), request.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File downloadImage = new File(resourcesPath + filename + ".jpg");
                    if (downloadImage.exists()) {
                        Image.getImageFromServer(downloadImage, bos);
                    } else {
                        System.out.println("File Not Found\n");
                        bos.write("HTTP 404 File Not Found\n".getBytes());
                    }
                }

                if (request.startsWith("POST ")) {
                    String filename = request.substring(request.indexOf(" /"), request.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File imageToUpload = new File(resourcesPath + filename);

                    String response = Image.saveImageOnServer(imageToUpload, bis);
                    bos.write(response.getBytes());
                }

                bos.flush();
            }

            System.out.println("Closing connections");

            bis.close();
            bos.close();

            client.close(); // server socket closed

        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
}