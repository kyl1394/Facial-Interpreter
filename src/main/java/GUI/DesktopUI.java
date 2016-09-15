package GUI;
/**
 * Created by Wes on 9/6/2016.
 */
import API.ImageUploader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import javafx.stage.Popup;
import javafx.stage.PopupBuilder;
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
                    MenuItem resizeUIMenuItem = new MenuItem("Resize UI");
                    MenuItem zoomInMenuItem = new MenuItem("Zoom In");
                    MenuItem zoomOutMenuItem = new MenuItem("Zoom Out");
                Menu settingsMenu = new Menu("Settings");
                    CheckMenuItem darkModeMenuItem = new CheckMenuItem("Dark Mode");
                    boolean isDarkMode = false;
            HBox container = new HBox();
                StackPane infoStack = new StackPane();
                    //ScrollPane imgContainer = new ScrollPane();
                        //Group zoomGroup = new Group();
                            ImageView image = new ImageView(new Image("file:///" + System.getProperty("user.dir") + "/src/main/java/GUI/UnselectedPicture.png"));
                        //    CurrentScale currentScaling = new CurrentScale();
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

        darkModeMenuItem.setDisable(true);
        zoomInMenuItem.setDisable(true);
        zoomOutMenuItem.setDisable(true);
        saveMenuItem.setDisable(true);

        //imgContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //imgContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        image.preserveRatioProperty().set(true);
        image.fitWidthProperty().bind(infoStack.widthProperty());
        image.fitHeightProperty().bind(infoStack.heightProperty());


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
            //main.getStylesheets().add(DARK_THEME_CSS);
            //darkModeMenuItem.setSelected(true);
        }

        /* ************************************
         * Add Event Listeners.
         * ************************************/

        //Allow scrolling the image without having to click the image
        image.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            image.requestFocus();
            event.consume();
        });

        chooseImgBtn.setOnAction((ActionEvent event) -> {
            ImageWithPath newImg = UIController.selectImage();

            if(newImg.image == null) // The user didn't load a valid picture
                return;

            pathToChosenImage = newImg.path;
            faceBtnContainer.getChildren().clear();

            image.setImage(newImg.image);
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
            System.out.println(pathToChosenImage);
            ImageUploader uploader = new ImageUploader();
            String url = "";
            try {
                url = uploader.upload(pathToChosenImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = Kairos.recognize(url, "testGallery", "0.6");

            JsonElement root = new JsonParser().parse(response);
            JsonArray jsonArray =  root.getAsJsonObject().get("images").getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject transaction = jsonArray.get(i).getAsJsonObject().get("transaction").getAsJsonObject();
                JsonElement subjectJson = transaction.get("subject");
                JsonElement widthJson = transaction.get("width");
                JsonElement heightJson = transaction.get("height");
                JsonElement xJson = transaction.get("topLeftX");
                JsonElement yJson = transaction.get("topLeftY");

                String x, y, width, height, subject;

                x = xJson == null ? null : xJson.toString();
                y = yJson == null ? null : yJson.toString();
                width = widthJson == null ? null : widthJson.toString();
                height = heightJson == null ? null : heightJson.toString();
                subject = subjectJson == null ? null : subjectJson.toString();

                System.out.println("Found: " + subject);
                System.out.println("X: " + x);
                System.out.println("Y: " + y);
                if (subject != null) {
                    faceBtnContainer.getChildren().add(DesktopUI.createNewFaceButton(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height), subject));
                } else {
                    //enroll
                }
            }
        });

        //MenuItem Events
        openMenuItem.setOnAction((ActionEvent event) -> {
            ImageWithPath newImg = UIController.selectImage();

            if(newImg == null) // The user didn't want to load a valid picture
                return;

            image.setImage(newImg.image);
            //if(!stage.isMaximized())
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
            //if(!stage.isMaximized())
                stage.sizeToScene();
        });

        zoomInMenuItem.setOnAction((ActionEvent event) -> {
            //currentScaling.zoom(1.25);
            //image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            /*for(Node n: faceBtnContainer.getChildren()) {
                n.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
            }*/
        });
        zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS,  KeyCombination.CONTROL_DOWN));

        zoomOutMenuItem.setOnAction((ActionEvent event) -> {
            //currentScaling.zoom(0.8);
            //image.getTransforms().setAll(new Scale(currentScaling.getScale(), currentScaling.getScale()));
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
        //zoomGroup.getChildren().add(image);
        //imgContainer.setContent(zoomGroup);

        faceBtnContainer.getChildren().add(test);
        infoStack.getChildren().addAll(image, faceBtnContainer);


        btnContainer.getChildren().addAll(topBtnPane, bottomBtnPane);
        container.getChildren().addAll(infoStack, btnContainer);

        main.setTop(menu);
        main.setCenter(container);

        //Show the UI
        stage.setScene(new Scene(main));
        //stage.setResizable(false);
        stage.show();
    }

    public static Button createNewFaceButton(int xPos, int yPos, int width, int height, String text) {
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

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem editInfo = new MenuItem("Edit Face's Information");

        editInfo.setOnAction((ActionEvent event) -> {
            addInfoScene(text);
        });

        rightClickMenu.getItems().add(editInfo);

        test.setContextMenu(rightClickMenu);

        test.setOnAction((ActionEvent event) -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            a.setTitle("Face Information");
            a.setHeaderText(text);

            a.showAndWait();
        });

        return test;
    }

    private final static String DARK_THEME_CSS = "file:///" + System.getProperty("user.dir") + "/src/main/java/GUI/darkTheme.css";


    private static void addInfoScene(String currentText) {
        Stage tempStage = new Stage();

        AnchorPane root = new AnchorPane();
            VBox container = new VBox();
                TextArea input = new TextArea(currentText);
                HBox btnContainer = new HBox();
                    Button confirmBtn = new Button("Confirm");
                    Button cancelBtn = new Button("Cancel");


        confirmBtn.setOnAction((ActionEvent event) -> {
            UIController.changeInfo(input.getText());
            tempStage.hide();
        });

        cancelBtn.setOnAction((ActionEvent event) -> {
            tempStage.hide();
        });


        btnContainer.getChildren().addAll(confirmBtn, cancelBtn);
        container.getChildren().addAll(input, btnContainer);
        root.getChildren().add(container);

        tempStage.setScene(new Scene(root));

        tempStage.show();

    }
}
