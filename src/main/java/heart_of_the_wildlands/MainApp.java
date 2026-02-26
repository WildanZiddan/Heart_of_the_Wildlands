package heart_of_the_wildlands;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;

public class MainApp extends Application {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/heart_of_the_wildlands/views/main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Heart of the Wildlands");
        Image icon = new Image(getClass().getResource("/heart_of_the_wildlands/assets/icon/icon_hotw.png").toExternalForm());
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
