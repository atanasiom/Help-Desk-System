package application;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.io.IOException;


/**
 * This class serves to clean up the main Help Desk code. It's a wrapper for a normal ImageView that has the code
 * that loads the image built in.
 */
public class IconView extends ImageView {

    public IconView(String path) throws IOException {
        super(SwingFXUtils.toFXImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(path)), null));
    }

}