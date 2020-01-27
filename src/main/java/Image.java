import java.io.*;

public class Image {

    private String filename;
    private File image;

    public Image(String filename)   {
        this.filename = filename;
    }

    public File getImageFromServer() {
        String serverPath = "/Users/user/IdeaProjects/MultiServer/src/main/resources/" + filename;
        image = new File(serverPath);
        if (image.exists()) {
            return image;
        } else return null;
    }

    public String saveImageOnServer(String localPath, OutputStream os) {
        image = new File(localPath);
        int buf = (int) image.length();
        try {
            FileInputStream fos = new FileInputStream(image);
            os.write(buf);
        }
        catch (IOException ex) {ex.getMessage(); }

        return filename;
    }

    public String getFilename() {
        return filename;
    }
}