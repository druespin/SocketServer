import java.io.*;
import java.nio.file.Files;


public class Image {


    static void getImageFromServer(File image, DataOutputStream dos) throws IOException {

        if (image.exists()) {
            System.out.println("Image Found on Server");

            byte buf[] = new byte[100 * 1024];
            FileInputStream fis = new FileInputStream(image);

            while (fis.available() > 0)
            {
                //dos.write(fis.read(buf));
                dos.writeUTF(image.getName());
            }
            fis.close();
        }
        else {
            System.out.println("File Not Found");
            dos.writeUTF("HTTP 404 File Not Found\n");
        }
    }

    static String saveImageOnServer(File image, File serverCopy) throws IOException {

        String response = "default";

        if (serverCopy.exists()) {
            response = "Image with same name already exists";
            System.out.println(response);
        }
        else if (!image.exists()) {
            response = "No image found";
            System.out.println(response);
        }
        else {
            Files.copy(image.toPath(), new FileOutputStream(serverCopy));
            response = RestApi.postResponse(image.getName());
        }

        return response;
    }
}