package GUI;

import API.ImageUploader;
import API.Kairos;
import FireBaseCalls.Student;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

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
     * String to hold the path of the image
     */
    private static String imgPath;

    //private static final String TAKEN_PIC_SAVE_LOCATION = "";

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
    public static Image selectImage()
    {
        FileChooser search = new FileChooser();
        search.getExtensionFilters().add(IMAGE_FILE_EXTENSIONS);

        File imgFile = null;
        Image selectedImg = null;

        try {
            imgFile = search.showOpenDialog(null);

            if(imgFile == null)
                throw new NullPointerException();

            selectedImg = openImage(imgFile);
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
            imgPath = imgFile.getPath();

            String name = imgFile.getName();
            int periodLoc = name.indexOf('.');
            fileNAME = name.substring(0, periodLoc);
            fileEXT = name.substring(periodLoc);
        }

        return selectedImg;
    }

    /**
     * Saves the image using the same name as the uploaded image as default.
     *
     * @param file The image to save
     */
    public static void saveImage(Image file) {
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
     * @return An image containing the metadata of all recognized faces
     */
    private static String url = "";
    public static JsonArray parseImage() {

        if(imgPath == null || imgPath.equals(""))
            return new JsonArray();

        System.out.println(imgPath);
        ImageUploader uploader = new ImageUploader();
        url = "";
        try {
            url = uploader.upload(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = Kairos.recognize(url, "testGallery", "0.6");

        JsonArray jsonArray = new JsonArray();
        try {
            JsonElement root = new JsonParser().parse(response);
            jsonArray = root.getAsJsonObject().get("images").getAsJsonArray();
        } catch(NullPointerException e) {
            return jsonArray; // There were no faces found
        }
        return jsonArray;
    }

    /**
     * Takes a picture using the given camera, if available.
     *
     * @return The picture taken by the camera
     */
    public static Image takePicture() {
        //Take the picture and save it
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            Alert a = new Alert(Alert.AlertType.ERROR);

            a.setTitle("Error in Taking a Picture");
            a.setHeaderText("Cannot Open the Camera");
            a.setContentText("This application cannot open your camera. Either you don't have one, or it doesn't have permission to open it.");

            a.showAndWait();
        } else {
            Mat frame = new Mat();
            while (true) {
                if (camera.read(frame)) {
                    System.out.println("Frame Obtained");
                    System.out.println("Captured Frame Width " +
                            frame.width() + " Height " + frame.height());
                    Highgui.imwrite("camera.jpg", frame);
                    System.out.println("OK");
                    break;
                }
            }
        }
        camera.release();

        //Load the Picture
        Image capturedImg = null;
        try {
            capturedImg = openImage(new File("camera.jpg"));
            fileNAME = "camera";
            fileEXT = ".jpg";
            imgPath = System.getProperty("user.dir") + "\\camera.jpg";

        } catch(Exception e) {
            e.printStackTrace();
        }
        return capturedImg;
    }

    private static Image openImage(File imgToOpen) throws Exception{
        return new Image(new FileInputStream(imgToOpen));
        //return SwingFXUtils.toFXImage(ImageIO.read(imgToOpen), null);
    }

    public static void changeInfo(String name, String lastSeen, String notes) {
    }

    public static void createNewInfo(String name, String lastSeen, String notes) {
        name = name.replaceAll("\\s","");
        Student student = new Student(name, notes, lastSeen);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/test/student");
        ref.push().setValue(student);
        Kairos.enroll(url, name.trim(), "testGallery");
    }
}
