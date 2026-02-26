package heart_of_the_wildlands.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAppController {

    @FXML
    private Label labelWelcome;

    @FXML
    private Button btnStart;

    @FXML
    private void initialize() {
//        labelWelcome.setText("Welcome, Adventurer!");
//        System.out.println(getClass().getResource("/heart_of_the_wildlands/assets/image/StartButton.png"));

    }

    @FXML
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/heart_of_the_wildlands/views/game.fxml"));
            Parent pane = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(pane);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load character.views: " + e.getMessage());
        }
    }


    @FXML
    private void exitGame(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}

