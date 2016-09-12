package GUI;
/**
 * Created by Wes on 9/6/2016.
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
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


public class DesktopUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

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
                    MenuItem resizeUIMenuItem = new MenuItem("Resize UI");
                    MenuItem zoomInMenuItem = new MenuItem("Zoom In");
                    MenuItem zoomOutMenuItem = new MenuItem("Zoom Out");
                Menu settingsMenu = new Menu("Settings");
                    CheckMenuItem darkModeMenuItem = new CheckMenuItem("Dark Mode");
                    boolean isDarkMode = true;
            HBox container = new HBox();
                StackPane infoStack = new StackPane();
                    ScrollPane imgContainer = new ScrollPane();
                        Group zoomGroup = new Group();
                            ImageView image = new ImageView(new Image("/GUI/UnselectedPicture.png"));
                            CurrentScale currentScaling = new CurrentScale();
                    AnchorPane faceBtnContainer = new AnchorPane();
                VBox btnContainer = new VBox();
                    VBox topBtnPane = new VBox();
                        Button chooseImgBtn = new Button("Choose Image");
                        Button takePicBtn   = new Button("Take Picture");
                        Label infoLabel     = new Label("");
                    VBox bottomBtnPane = new VBox();
                        Button findFacesBtn = new Button("Find Faces");

        infoStack.setAlignment(Pos.TOP_LEFT);
        Button test = createNewFaceButton(50, 50, 100, 100, "");




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
        findFacesBtn.setMinWidth(Region.USE_PREF_SIZE);
        findFacesBtn.setMaxWidth(Double.MAX_VALUE);
        infoLabel.setWrapText(true);

        VBox.setVgrow(topBtnPane, Priority.ALWAYS);
        VBox.setVgrow(bottomBtnPane, Priority.ALWAYS);

        topBtnPane.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(chooseImgBtn, new Insets(10, 0, 10, 0)); // Top, Right, Bottom, Left
        bottomBtnPane.setAlignment(Pos.BOTTOM_CENTER);

        HBox.setMargin(infoStack, new Insets(10, 10, 10, 10));
        HBox.setMargin(btnContainer, new Insets(10, 10, 10, 10));
        HBox.setHgrow(infoStack, Priority.ALWAYS);
        HBox.setHgrow(btnContainer, Priority.ALWAYS);

        if(isDarkMode) {
            main.getStylesheets().add(DARK_THEME_CSS);
            darkModeMenuItem.setSelected(true);
        }
        //darkModeMenuItem.setDisable(true);

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
            Image newImg = UIController.selectImage();

            if(newImg == null) // The user didn't load a valid picture
                return;

            image.setImage(newImg);
            if(!stage.isMaximized())
                stage.sizeToScene();
        });

        takePicBtn.setOnAction((ActionEvent event) -> {
            infoLabel.setText("Taking a Picture...");
            Image takenPic = UIController.takePicture();

            infoLabel.setText("");

            if(takenPic != null) {
                image.setImage(takenPic);
            }
            else {
                infoLabel.setText("Image Failed to Load");
            }
        });

        findFacesBtn.setOnAction((ActionEvent event) -> {
            image.setImage(UIController.parseImage(image.getImage()));
            stage.sizeToScene();
        });

        //MenuItem Events
        openMenuItem.setOnAction((ActionEvent event) -> {
            Image newImg = UIController.selectImage();

            if(newImg == null) // The user didn't want to load a valid picture
                return;

            image.setImage(newImg);
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

        resizeUIMenuItem.setOnAction((ActionEvent event) -> {
            if(!stage.isMaximized()) {
                stage.sizeToScene();
            }
        });

        zoomInMenuItem.setOnAction((ActionEvent event) -> {
            currentScaling.zoom(1.25);
            image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            /*for(Node n: faceBtnContainer.getChildren()) {
                n.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            }*/
        });
        zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS,  KeyCombination.CONTROL_DOWN));

        zoomOutMenuItem.setOnAction((ActionEvent event) -> {
            currentScaling.zoom(0.8);
            image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            /*for(Node n: faceBtnContainer.getChildren()) {
                n.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            }*/
        });
        zoomOutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.MINUS,  KeyCombination.CONTROL_DOWN));

        darkModeMenuItem.setOnAction((ActionEvent event) -> {
            if(darkModeMenuItem.isSelected())
                main.getStylesheets().add(DARK_THEME_CSS);
            else
                main.getStylesheets().remove(DARK_THEME_CSS);

        });

        stage.maximizedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean becomesMaximized) -> {
            //Triggers when the screen becomes maximized and when it becomes unmaxiximized. Can't resize UI when full screen
            resizeUIMenuItem.setDisable(becomesMaximized);
        });

        /* ************************************
         * Combine UI Together.
         * ************************************/

        //Combine Menus
        fileMenu.getItems().addAll(openMenuItem, saveMenuItem, closeMenuItem);
        viewMenu.getItems().addAll(resizeUIMenuItem, zoomInMenuItem, zoomOutMenuItem);
        settingsMenu.getItems().add(darkModeMenuItem);
        menu.getMenus().addAll(fileMenu, viewMenu, settingsMenu);

        //Combine the Main UI
        topBtnPane.getChildren().addAll(chooseImgBtn, takePicBtn, infoLabel);
        bottomBtnPane.getChildren().add(findFacesBtn);
        zoomGroup.getChildren().add(image);
        imgContainer.setContent(zoomGroup);

        faceBtnContainer.getChildren().add(test);
        infoStack.getChildren().addAll(imgContainer, test);


        btnContainer.getChildren().addAll(topBtnPane, bottomBtnPane);
        container.getChildren().addAll(infoStack, btnContainer);

        main.setTop(menu);
        main.setCenter(container);

        //Show the UI
        stage.setScene(new Scene(main));
        stage.show();
    }

    private static Button createNewFaceButton(int xPos, int yPos, int width, int height, String text) {
        Button test = new Button();
        test.setId("face");
        test.setStyle("-fx-background-color: rgba(0, 0, 0, 0), rgba(0, 0, 0, 0), rgba(0, 0, 0, 0), rgba(0, 0, 0, 0);\n" +
                "    -fx-border-color: -fx-focus-color;");
        test.setLayoutX(xPos);
        test.setLayoutY(yPos);
        test.setMinHeight(height);
        test.setMaxHeight(height);
        test.setMinWidth(width);
        test.setMaxWidth(width);
        test.setCursor(Cursor.HAND);

        test.setOnAction((ActionEvent event) -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            a.setTitle("Face Information");
            a.setHeaderText(text);

            a.showAndWait();
        });

        return test;
    }

    private final static String DARK_THEME_CSS = "/GUI/darkTheme.css";
}
