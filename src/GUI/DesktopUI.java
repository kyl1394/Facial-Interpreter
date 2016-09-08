package GUI; /**
 * Created by Wes on 9/6/2016.
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import javax.swing.*;

public class DesktopUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Facial Interpreter");

        //Initialize all elements of the UI

        //Organized to show the xml layout of the UI from the SceneBuilder
        MenuBar menu = new MenuBar(); // Menu not implemented
            Menu fileMenu = new Menu("File");
                MenuItem openMenuItem = new MenuItem("Open");
                MenuItem saveMenuItem = new MenuItem("Save");
                MenuItem closeMenuItem = new MenuItem("Close");
            Menu runMenu = new Menu("Run");
                MenuItem zoomInMenuItem = new MenuItem("Zoom In");
                MenuItem zoomOutMenuItem = new MenuItem("Zoom Out");
                MenuItem chooseImgMenuItem = new MenuItem("Choose Image");
                MenuItem findFacesMenuItem = new MenuItem("Find Faces");

        BorderPane main = new BorderPane();
            HBox container = new HBox();
                ScrollPane imgContainer = new ScrollPane();
                    Group zoomGroup = new Group();
                        ImageView image = new ImageView(new Image("/GUI/UnselectedPicture.png"));
                        CurrentScale currentScaling = new CurrentScale();
                VBox btnContainer = new VBox();
                    VBox topBtnPane = new VBox();
                        Button chooseImgBtn = new Button("Choose Image");
                        Button takePicBtn   = new Button("Take Picture");
                    VBox bottomBtnPane = new VBox();
                        Button findFacesBtn = new Button("Find Faces");



        //Add Properties to the UI

        imgContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        imgContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        image.preserveRatioProperty().set(true);


        chooseImgBtn.setMinWidth(Region.USE_PREF_SIZE);
        chooseImgBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setMinWidth(Region.USE_PREF_SIZE);
        takePicBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setDisable(true); //Taking a picture isn't implemented yet


        VBox.setVgrow(topBtnPane, Priority.ALWAYS);
        VBox.setVgrow(bottomBtnPane, Priority.ALWAYS);

        topBtnPane.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(chooseImgBtn, new Insets(10, 0, 10, 0)); // Top, Right, Bottom, Left
        bottomBtnPane.setAlignment(Pos.BOTTOM_CENTER);

        HBox.setMargin(imgContainer, new Insets(10, 10, 10, 10));
        HBox.setMargin(btnContainer, new Insets(10, 10, 10, 10));
        HBox.setHgrow(imgContainer, Priority.ALWAYS);
        HBox.setHgrow(btnContainer, Priority.ALWAYS);

        //Add Event Listeners. Lambda Expressions are really cool, too

        //Allow scrolling the image withuot having to click the image
        image.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                image.requestFocus();
                event.consume();
            }
        });

        chooseImgBtn.setOnAction((ActionEvent event) -> {
            Image newImg = UIController.selectImage();

            if(newImg == null) // The user didn't want to load a valid picture
                return;

            image.setImage(newImg);
            stage.sizeToScene();
        });

        findFacesBtn.setOnAction((ActionEvent event) -> {
            image.setImage(UIController.parseImage(image.getImage()));
            stage.sizeToScene();
        });

        //MenuItem Events
        openMenuItem.setOnAction((ActionEvent event) -> {

        });
        saveMenuItem.setOnAction((ActionEvent event) -> {

        });
        closeMenuItem.setOnAction((ActionEvent event) -> {

        });
        zoomInMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentScaling.zoom(1.25);

                image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
                stage.sizeToScene();
            }
        });
        zoomOutMenuItem.setOnAction((ActionEvent event) -> {
            
            currentScaling.zoom(0.8);
            image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            stage.sizeToScene();
        });
        chooseImgMenuItem.setOnAction((ActionEvent event) -> {

        });
        findFacesMenuItem.setOnAction((ActionEvent event) -> {

        });
        //Combine UI together

        fileMenu.getItems().addAll(openMenuItem, saveMenuItem, closeMenuItem);
        runMenu.getItems().addAll(zoomInMenuItem, zoomOutMenuItem, chooseImgMenuItem, findFacesMenuItem);
        menu.getMenus().addAll(fileMenu, runMenu);

        topBtnPane.getChildren().addAll(chooseImgBtn, takePicBtn);
        bottomBtnPane.getChildren().add(findFacesBtn);
        zoomGroup.getChildren().add(image);
        imgContainer.setContent(zoomGroup);
        btnContainer.getChildren().addAll(topBtnPane, bottomBtnPane);
        container.getChildren().addAll(imgContainer, btnContainer);

        //main.getChildren().addAll(menu, container);
        main.setTop(menu);
        main.setCenter(container);

        stage.setScene(new Scene(main));
        stage.show();
        //stage.setResizable(false);
    }
}
