import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.Charset;


public class TaskClient {


    public static void main(String[] args) throws IOException {

        String clientImagesPath = "/Users/user/Downloads/downloaded_from_server/"; // client's image store


            Socket socket = new Socket("localhost", 4444);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());

            //DataInputStream dis = new DataInputStream(socket.getInputStream());  // for text request
            //DataOutputStream dos = new DataOutputStream(socket.getOutputStream());  // for text request

            System.out.println("Client connected to socket.");

            while (socket.isConnected()) {
                System.out.println("Input request type and file name");

                // Get request type and filename
                String[] input = br.readLine().split(" ");
                String reqType = input[0];
                String filename = input[1];
                String request = "req";

                switch (reqType) {
                    case "get":
                        request = RestApi.getRequest(filename);
                        break;
                    case "post":
                        request = RestApi.postRequest(filename);
                        break;
                }

                System.out.println(request);

                bos.write(request.getBytes());
                bos.flush();
                System.out.println("Request sent. Waiting for response...");

                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException inter) { inter.getStackTrace(); }

                if (socket.isClosed()) {
                    System.out.println("Connection time expired");
                }

                // GET request scenario
                if (reqType.equals("get")) {
                    int sizeOfImage = bis.available();

                    if (sizeOfImage > 0) {
                        System.out.println("image received:");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] byteArray = baos.toByteArray();

                        FileOutputStream fos = new FileOutputStream(new File(clientImagesPath + filename));
                        fos.write(byteArray);

                    } else {
                        System.out.println("No response");
                    }
                }

                // POST request scenario
                if (reqType.equals("post")) {

                    String resp = IOUtils.toString(bis, Charset.defaultCharset());
                    System.out.println(resp);
                }
            }

            System.out.println("Closing connections & channels on client side - DONE.");

        }
    }