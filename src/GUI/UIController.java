package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * This static class is used for the logic of the UI.
 * The UI logic should be dealt with here. Anything dealing directly with the UI should be in the DesktopUI.java file.
 *
 * Created by Wes on 9/7/2016.
 */
public class UIController {
    /**
     * Gets an image from the user's file system.
     *
     * This method will ask the user for an image file and return the file as a JavaFX Image Object.
     * All file selection errors will result in an error screen for the user.
     * To change the files that are deemed to be Image Files, modify the IMAGE_FILE_EXTENSIONS object.
     *
     * @return The image file given by the user.
     */
    public static Image selectImage()
    {
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
            a.setContentText("You most likely didn't pick an image. " + e.getMessage());

            a.showAndWait();
        }

        return selectedImg;
    }

    /**
     * This method takes an image and returns the new image to show.
     *
     * Specifically, this method will determine the faces in an image and add metadata about the image to make a new image.
     *
     * @param imgToParse The image to parse
     * @return An image containing the metadata of all recognized faces
     */
    public static Image parseImage(Image imgToParse) {

        //TODO implement facial recognition code
        return imgToParse;
    }
}
