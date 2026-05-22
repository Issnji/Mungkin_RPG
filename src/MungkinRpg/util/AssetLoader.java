package MungkinRpg.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        if (images.containsKey(path)) return images.get(path);

        try {
            BufferedImage img = ImageIO.read(new File("src/MungkinRpg/assets/image/" + path));
            images.put(path, img);
            return img;
        } catch (IOException e) {
            System.out.println("Failed to load: " + path);
            return null;
        }
    }
}