import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;


public class Image {


    static void getImageFromServer(File image, BufferedOutputStream bos) throws IOException {

            System.out.println("Image Found on Server");

            IOUtils.copy(new FileInputStream(image), bos);
            System.out.println("Image sent to client");
    }

    static String saveImageOnServer(File image, BufferedInputStream bis) throws IOException {

        String response = "default";

        IOUtils.copy(bis, new FileOutputStream(image));
        response = RestApi.postResponse(image.getName());

        return response;
    }
}