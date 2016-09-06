/**
 * Created by Wes on 9/6/2016.
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
                ImageView image = new ImageView(new Image("/UnselectedPicture.png"));
                VBox btnContainer = new VBox();
                    VBox topBtnPane = new VBox();
                        Button chooseImgBtn = new Button("Choose Image");
                        Button takePicBtn   = new Button("Take Picture");
                    VBox bottomBtnPane = new VBox();
                        Button findFacesBtn = new Button("Find Faces");

        //Add Properties to the UI



        //Combine UI together
        topBtnPane.getChildren().add(chooseImgBtn);
        topBtnPane.getChildren().add(takePicBtn);

        bottomBtnPane.getChildren().add(findFacesBtn);

        btnContainer.getChildren().add(topBtnPane);
        btnContainer.getChildren().add(bottomBtnPane);

        container.getChildren().add(image);
        container.getChildren().add(btnContainer);

        main.getChildren().add(container);

        stage.setScene(new Scene(main, 633, 400));

        stage.show();



    }
}
