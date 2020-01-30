import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TaskServer {


    public static void main(String[] args) throws IOException {

        String resourcesPath = "/Users/user/IdeaProjects/MultiServer/src/main/resources"; // server image store
        File resources = new File(resourcesPath);
        String clientImagesPath = "/Users/user/Downloads"; // client's image store

        try (ServerSocket server = new ServerSocket(4444))  {

            Socket client = server.accept();    // server socket opened
            System.out.println("Connection accepted.\n");

            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
            DataInputStream din = new DataInputStream(client.getInputStream());


            while (!client.isClosed()) {

                System.out.println("Server waiting for request...");

                // ожидание данных в канале чтения
                String req = din.readUTF();

                if (req.contains("GET /")) {

                    String filename = req.substring(req.indexOf("/"), req.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File image = new File(resourcesPath + filename);
                    Image.getImageFromServer(image, dout);
                }

                if (req.contains("POST "))     {
                    String filename = req.substring(req.indexOf(" /"), req.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File image = new File(clientImagesPath + filename);
                    File serverCopy = new File(resourcesPath + filename);

                    if (filename.contains("jpg") || filename.contains("png"))   {
                        String response = Image.saveImageOnServer(image, serverCopy);
                        dout.writeUTF(response);
                    }
                }

                dout.flush();
            }

            System.out.println("Closing connections");

            din.close();
            dout.close();

            client.close(); // server socket closed


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}