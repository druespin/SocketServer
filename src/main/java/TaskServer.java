import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TaskServer {



    public static void main(String[] args) throws InterruptedException {

        String imageStoreOnServerPath = "/Users/user/IdeaProjects/MultiServer/src/main/resources";

        try (ServerSocket server = new ServerSocket(4444))  {

            // сокету "client" ожидает подключения
            Socket client = server.accept();

            // после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
            System.out.println("Connection accepted.");

            // каналы чтения/записи в/из сокета
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
            DataInputStream din = new DataInputStream(client.getInputStream());


            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            while(!client.isClosed()) {

                System.out.println("Server waiting for request...");

                // сервер ждёт в канале чтения din получения данных клиента
                String req = din.readUTF();

                // после получения данных считывает их
                if (req.contains("GET /")) {

                    String filename = req.substring(req.indexOf(" /"), req.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File image = new File(filename);
                    if (image.exists()) {
                        dout.write((int) image.length());
                    }
                    else {
                        dout.writeUTF("404 File Not Found\n");
                    }

                    dout.flush();
                }

                if (req.contains("POST "))     {
                    String filename = req.substring(req.indexOf(" /"), req.indexOf(" HTTP"));
                    System.out.println("Filename: " + filename);

                    File image = new File(filename);
                    if (image.exists()) {
                        dout.write((int) image.length());
                    }
                }


                // если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
                System.out.println("Server Wrote message to client.");
                dout.flush();
            }

            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            // закрываем сначала каналы сокета !
            din.close();
            dout.close();

            // потом закрываем сам сокет общения на стороне сервера!
            client.close();

            // потом закрываем сокет сервера который создаёт сокеты общения
            // хотя при многопоточном применении его закрывать не нужно
            // для возможности поставить этот серверный сокет обратно в ожидание нового подключения

            System.out.println("Closing connections & channels - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}