package GUI;
/**
 * Created by Wes on 9/6/2016.
 */
import API.ImageUploader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import API.Kairos;

import java.io.IOException;

public class DesktopUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    String pathToChosenImage = "";

    @Override
    public void start(Stage stage) {
        stage.setTitle("Facial Interpreter");

        /* ************************************
         * Initialize all elements of the UI
         * ************************************/
        //Organized to show the xml layout of the UI from the SceneBuilder
        BorderPane main = new BorderPane();
            MenuBar menu = new MenuBar();
                Menu fileMenu = new Menu("File");
                    MenuItem openMenuItem = new MenuItem("Open");
                    MenuItem saveMenuItem = new MenuItem("Save");
                    MenuItem closeMenuItem = new MenuItem("Close");
                Menu viewMenu = new Menu("View");
                    MenuItem zoomInMenuItem = new MenuItem("Zoom In");
                    MenuItem zoomOutMenuItem = new MenuItem("Zoom Out");
            HBox container = new HBox();
                ScrollPane imgContainer = new ScrollPane();
                    Group zoomGroup = new Group();
                        System.out.println(System.getProperty("user.dir"));
                        ImageView image = new ImageView(new Image("file:///" + System.getProperty("user.dir") + "/src/main/java/GUI/UnselectedPicture.png"));
                        CurrentScale currentScaling = new CurrentScale();
                VBox btnContainer = new VBox();
                    VBox topBtnPane = new VBox();
                        Button chooseImgBtn = new Button("Choose Image");
                        Button takePicBtn   = new Button("Take Picture");
                    VBox bottomBtnPane = new VBox();
                        Button findFacesBtn = new Button("Find Faces");


        /* ************************************
         * Add Properties to the UI
         * ************************************/

        imgContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        imgContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        image.preserveRatioProperty().set(true);

        chooseImgBtn.setMinWidth(Region.USE_PREF_SIZE);
        chooseImgBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setMinWidth(Region.USE_PREF_SIZE);
        takePicBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setDisable(true); //Taking a picture isn't implemented yet
        findFacesBtn.setMinWidth(Region.USE_PREF_SIZE);
        findFacesBtn.setMaxWidth(Double.MAX_VALUE);

        VBox.setVgrow(topBtnPane, Priority.ALWAYS);
        VBox.setVgrow(bottomBtnPane, Priority.ALWAYS);

        topBtnPane.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(chooseImgBtn, new Insets(10, 0, 10, 0)); // Top, Right, Bottom, Left
        bottomBtnPane.setAlignment(Pos.BOTTOM_CENTER);

        HBox.setMargin(imgContainer, new Insets(10, 10, 10, 10));
        HBox.setMargin(btnContainer, new Insets(10, 10, 10, 10));
        HBox.setHgrow(imgContainer, Priority.ALWAYS);
        HBox.setHgrow(btnContainer, Priority.ALWAYS);


        /* ************************************
         * Add Event Listeners.
         * ************************************/

        //Allow scrolling the image without having to click the image
        image.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                image.requestFocus();
                event.consume();
            }
        });

        chooseImgBtn.setOnAction((ActionEvent event) -> {
            ImageWithPath newImg = UIController.selectImage();

            if(newImg.image == null) // The user didn't load a valid picture
                return;

            pathToChosenImage = newImg.path;

            image.setImage(newImg.image);
            if(!stage.isMaximized())
                stage.sizeToScene();
        });

        findFacesBtn.setOnAction((ActionEvent event) -> {
            image.setImage(UIController.parseImage(image.getImage()));
            System.out.println(pathToChosenImage);
            ImageUploader uploader = new ImageUploader();
            String url = "";
            try {
                url = uploader.upload(pathToChosenImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Kairos.recognize(url, "testGallery", "0.1");
            stage.sizeToScene();
        });

        //MenuItem Events
        openMenuItem.setOnAction((ActionEvent event) -> {
            ImageWithPath newImg = UIController.selectImage();

            if(newImg == null) // The user didn't want to load a valid picture
                return;

            image.setImage(newImg.image);
            if(!stage.isMaximized())
                stage.sizeToScene();
        });
        openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O,  KeyCombination.CONTROL_DOWN));

        saveMenuItem.setOnAction((ActionEvent event) -> {
            UIController.saveImage(image.getImage());
        });
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S,  KeyCombination.CONTROL_DOWN));

        closeMenuItem.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.W,  KeyCombination.CONTROL_DOWN));

        zoomInMenuItem.setOnAction((ActionEvent event) -> {
            currentScaling.zoom(1.25);
            image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
        });
        zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS,  KeyCombination.CONTROL_DOWN));

        zoomOutMenuItem.setOnAction((ActionEvent event) -> {
            currentScaling.zoom(0.8);
            image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            //if(currentScaling.getScale() < 1)
            //    stage.sizeToScene();
        });
        zoomOutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.MINUS,  KeyCombination.CONTROL_DOWN));


        /* ************************************
         * Combine UI Together.
         * ************************************/

        //Combine Menus
        fileMenu.getItems().addAll(openMenuItem, saveMenuItem, closeMenuItem);
        viewMenu.getItems().addAll(zoomInMenuItem, zoomOutMenuItem);
        menu.getMenus().addAll(fileMenu, viewMenu);

        //Combine the Main UI
        topBtnPane.getChildren().addAll(chooseImgBtn, takePicBtn);
        bottomBtnPane.getChildren().add(findFacesBtn);
        zoomGroup.getChildren().add(image);
        imgContainer.setContent(zoomGroup);
        btnContainer.getChildren().addAll(topBtnPane, bottomBtnPane);
        container.getChildren().addAll(imgContainer, btnContainer);

        main.setTop(menu);
        main.setCenter(container);

        //Show the UI
        stage.setScene(new Scene(main));
        stage.show();
    }


}
