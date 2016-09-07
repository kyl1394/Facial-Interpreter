package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Wes on 9/7/2016.
 */
public class UIController {
    public static Image selectImage()
    {
        //TODO implement file crawler
        FileChooser.ExtensionFilter IMAGE_FILE_EXTENSIONS = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        FileChooser search = new FileChooser();
        search.getExtensionFilters().add(IMAGE_FILE_EXTENSIONS);

        Image selectedImg = null;

        try {
            File imgFile = search.showOpenDialog(null);

            selectedImg = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
        } catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);

            a.setTitle("You dun Goofed!");
            a.setHeaderText("An unknown error has occurred");
            a.setContentText("You most likely didn't pick an image.");

            a.showAndWait();
        }

        return selectedImg;
    }

    public static Image parseImage() {

        //TODO implement facial recognition code
        return null;
    }
}
