import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;

public class TaskClient {


    public static void main(String[] args) throws InterruptedException {

        try(
                Socket socket = new Socket("localhost", 4444);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream()) )
        {

            System.out.println("Client connected to socket.");

            // запуск таймера сеанса подключения
            Timer timer = new Timer();
            ConnectTimerTask timerTask = new ConnectTimerTask();
            timer.schedule(timerTask, 20000);

            // проверяем живой ли канал и работаем если живой
            while(!timerTask.getStop())
            {
                // ждём консоли клиента на предмет появления в ней данных
                if (br.ready())
                {
                    // данные появились - работаем
                    String req = RestApi.GET_request("image-1");
                    System.out.println(req);

                    // пишем данные с консоли в канал сокета для сервера
                    dos.writeUTF(req);
                    dos.flush();
                    Thread.sleep(2000);

                    // ждём чтобы сервер успел прочесть сообщение из сокета и ответить

                    // проверяем условие выхода из соединения
                    if(timerTask.getStop())
                    {
                        // если условие выхода достигнуто разъединяемся
                        System.out.println("Connection time expired");
                        Thread.sleep(1000);

                        // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        if(dis.read() > -1)     {
                            System.out.println("reading before closing...");
                            String in = dis.readUTF();
                            System.out.println(in);
                        }

                        break;
                    }

                    // если условие разъединения не достигнуто продолжаем работу
                    System.out.println("Client sent message & starts waiting for data from server...\n");

                    // проверяем, что нам ответит сервер на сообщение
                    // (за предоставленное ему время в паузе он должен был успеть ответить)
                    if (dis.available() > 0)     {

                        // если успел, забираем ответ из канала сервера в сокете и сохраняем его в dis переменную,
                        // печатаем на свою клиентскую консоль
                        System.out.println("Waiting for response...");
                        String in = dis.readUTF();
                        System.out.println(in);
                    }
                }
            }

            // на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Closing connections & channels on clent side - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}