import java.io.*;
import java.net.Socket;


public class TaskClient {


    public static void main(String[] args) throws InterruptedException {

        try(
                Socket socket = new Socket("localhost", 4444);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream()) )
        {

            System.out.println("Client connected to socket.");

            while (socket.isConnected()) {
                System.out.println("Input request type and file name");

                String[] input = br.readLine().split(" ");
                String reqType = input[0];
                String filename = input[1];
                String request = "-_-";

                switch (reqType) {
                    case "get":
                        request = RestApi.getRequest(filename);
                        break;
                    case "post":
                        request = RestApi.postRequest(filename);
                        break;
                }

                System.out.println(request);

                dos.writeUTF(request);
                dos.flush();
                Thread.sleep(1000);

                if (socket.isClosed()) {
                    System.out.println("Connection time expired");

                    if (dis.read() > -1) {
                        System.out.println("reading before closing...");
                        String in = dis.readUTF();
                        System.out.println(in);
                    }
                    break;
                }

                System.out.println("Request sent. Waiting for response...\n");

                if (dis.available() > 0) {
                    System.out.println("Response: " + dis.readUTF());
                    } else {
                        System.out.println("No response");
                    }
                }



            System.out.println("Closing connections & channels on clent side - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}