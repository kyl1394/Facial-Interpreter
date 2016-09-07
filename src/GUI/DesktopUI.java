package GUI; /**
 * Created by Wes on 9/6/2016.
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DesktopUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Facial Interpreter");

        //Initialize all elements of the UI

        //Organized to show the xml layout of the UI from the SceneBuilder
        AnchorPane main = new AnchorPane();
            HBox container = new HBox();
                ImageView image = new ImageView(new Image("/GUI/UnselectedPicture.png"));
                VBox btnContainer = new VBox();
                    VBox topBtnPane = new VBox();
                        Button chooseImgBtn = new Button("Choose Image");
                        Button takePicBtn   = new Button("Take Picture");
                    VBox bottomBtnPane = new VBox();
                        Button findFacesBtn = new Button("Find Faces");

        //Add Properties to the UI

        chooseImgBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setMaxWidth(Double.MAX_VALUE);
        takePicBtn.setDisable(true); //Not implemented yet

        VBox.setVgrow(topBtnPane, Priority.ALWAYS);
        VBox.setVgrow(bottomBtnPane, Priority.ALWAYS);

        topBtnPane.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(chooseImgBtn, new Insets(10, 0, 10, 0)); // Top, Right, Bottom, Left
        bottomBtnPane.setAlignment(Pos.BOTTOM_CENTER);

        HBox.setMargin(image, new Insets(10, 10, 10, 10));
        HBox.setMargin(btnContainer, new Insets(10, 10, 10, 10));
        HBox.setHgrow(image, Priority.ALWAYS);
        HBox.setHgrow(btnContainer, Priority.ALWAYS);

        //Add Button Listeners. Lambda Expressions are really cool, too
        chooseImgBtn.setOnAction((ActionEvent event) -> {
            Image newImg = UIController.selectImage();

            if(newImg == null) // The user didn't want to load a valid picture
                return;

            image.setImage(newImg);

            //Reload Stage with new Image
            stage.hide();
            stage.show();
        });

        findFacesBtn.setOnAction((ActionEvent event) -> {
            image.setImage(UIController.parseImage());

            //Reload Stage with new Image
            stage.hide();
            stage.show();
        });

        //Combine UI together
        topBtnPane.getChildren().add(chooseImgBtn);
        topBtnPane.getChildren().add(takePicBtn);

        bottomBtnPane.getChildren().add(findFacesBtn);

        btnContainer.getChildren().add(topBtnPane);
        btnContainer.getChildren().add(bottomBtnPane);

        container.getChildren().add(image);
        container.getChildren().add(btnContainer);

        main.getChildren().add(container);

        stage.setScene(new Scene(main));

        stage.show();

        stage.setResizable(false);

    }
}
