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
     * String to hold the current image's extension
     */
    private static String fileEXT;

    /**
     * String to hold the current image's file name
     */
    private static String fileNAME;

    /**
     * Object to select all valid image files when opening/saving
     */
    private static final FileChooser.ExtensionFilter IMAGE_FILE_EXTENSIONS = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");


    /**
     * Gets an image from the user's file system.
     *
     * This method will ask the user for an image file and return the file as a JavaFX Image Object.
     * All file selection errors will result in an error screen for the user.
     * To change the files that are deemed to be Image Files, modify the IMAGE_FILE_EXTENSIONS object.
     *
     * @return The image file given by the user.
     */
    public static ImageWithPath selectImage()
    {
        ImageWithPath image = new ImageWithPath();
        FileChooser search = new FileChooser();
        search.getExtensionFilters().add(IMAGE_FILE_EXTENSIONS);

        File imgFile = null;
        Image selectedImg = null;

        try {
            imgFile = search.showOpenDialog(null);

            if(imgFile == null)
                throw new NullPointerException();

            selectedImg = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
        } catch (NullPointerException e) {
            //Do nothing. User just cancelled the operation
        } catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);

            a.setTitle("You dun Goofed!");
            a.setHeaderText("An unknown error has occurred");
            a.setContentText("You most likely didn't pick an image. " + e.getMessage());

            a.showAndWait();
        }

        if(selectedImg != null) {
            String name = imgFile.getName();
            int periodLoc = name.indexOf('.');
            fileNAME = name.substring(0, periodLoc);
            fileEXT = name.substring(periodLoc);
            image.image = selectedImg;
            image.path = imgFile.getAbsolutePath();
        }

        return image;
    }

    /**
     * Saves the image using the same name as the uploaded image as default.
     *
     * @param file The image to save
     */
    public static void saveImage(Image file) {
        //TODO
        FileChooser save = new FileChooser();
        if(fileEXT == null || fileEXT.equals("") || fileNAME == null || fileNAME.equals("")) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            a.setTitle("You dun Goofed!");
            a.setHeaderText("Cannot Save Nothing");
            a.setContentText("There is no image here that I can save.\nThe only way for this to happen is if the image was somehow loaded incorrectly or you didn't load an image.");

            a.showAndWait();
            return; // TODO notify the user of error
        }

        save.getExtensionFilters().add(IMAGE_FILE_EXTENSIONS);
        save.setInitialFileName(fileNAME + fileEXT);
        save.showSaveDialog(null);
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
