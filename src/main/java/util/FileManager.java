package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: brianfroschauer
 * Date: 06/06/2018
 */
public class FileManager {

    private FileManager() {}

    public static void writeImage(InputStream inputStream, String filePath) throws IOException {
        final File file = new File("/resources/images" + filePath);
        final BufferedImage image = ImageIO.read(inputStream);
        ImageIO.write(image, "png", file);
    }
}