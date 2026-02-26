package heart_of_the_wildlands.controllers;

import heart_of_the_wildlands.inventory.*;
import heart_of_the_wildlands.quest.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class GameController {
    @FXML private AnchorPane gameRoot, inventoryPane;
    @FXML private Pane characterPane, slimebossPane;
    @FXML private ProgressBar hpBar, levelBar;
    @FXML public Label hpLabel, levelLabel, levelUpLabel, levelUpSubLabel;
    @FXML private Rectangle wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, wall9, wall10, wall11, wall12, wall13, wall14, wall15, wall16, wall17, wall18; // Add more if you have more walls like wall2, wall3, etc.

    @FXML private StackPane levelUpPane;


    @FXML
    private GridPane inventoryGrid;

    public Inventory inventory = new Inventory(); // connect your DLL here

    public void showInventory(Inventory inventory) {
        inventoryGrid.getChildren().clear();

        int col = 0, row = 0;
        InventoryNode current = inventory.getHead();

        while (current != null) {
            try {
                // Make a final copy of current for use inside lambdas
                final InventoryNode thisNode = current;

                Image image = new Image(getClass().getResource(thisNode.getIconPath()).toExternalForm());
                ImageView iconView = new ImageView(image);
                iconView.setFitWidth(40);
                iconView.setFitHeight(40);

                Label quantityLabel = new Label("x" + thisNode.getQuantity());
                quantityLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");

                VBox itemBox = new VBox(iconView, quantityLabel);
                itemBox.setAlignment(Pos.CENTER);
                itemBox.setSpacing(4);
                itemBox.setUserData(thisNode); // Store the node

                // Drag start
                itemBox.setOnDragDetected(e -> {
                    Dragboard db = itemBox.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(thisNode.getItemName());
                    db.setContent(content);
                    db.setDragView(iconView.snapshot(null, null));
                    e.consume();
                });

                // Drag over target
                itemBox.setOnDragOver(e -> {
                    if (e.getGestureSource() != itemBox && e.getDragboard().hasString()) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    }
                    e.consume();
                });

                // Drop
                itemBox.setOnDragDropped(e -> {
                    Dragboard db = e.getDragboard();
                    if (db.hasString()) {
                        InventoryNode draggedItem = inventory.findItem(db.getString());
                        InventoryNode targetItem = (InventoryNode) itemBox.getUserData();
                        if (draggedItem != null && targetItem != null && draggedItem != targetItem) {
                            inventory.swapItems(draggedItem, targetItem);
                            showInventory(inventory); // Refresh the UI
                        }
                    }
                    e.setDropCompleted(true);
                    e.consume();
                });

                inventoryGrid.add(itemBox, col++, row);
                if (col >= 5) {
                    col = 0;
                    row++;
                }

            } catch (Exception e) {
                System.out.println("Error loading icon for item: " + current.getItemName());
                e.printStackTrace();
            }

            current = current.getNext();
        }
    }





    private List<Rectangle> obstacles = new ArrayList<>();
    private MyArrayList<Quest> questList = new MyArrayList<>();
    private int currentQuestIndex = 0;
    private Scene scene;

    public Quest getCurrentQuest() {
        if (currentQuestIndex < questList.size()) {
            return questList.get(currentQuestIndex);
        }
        return null;
    }

    public void completeCurrentQuest() {
        if (currentQuestIndex < questList.size()) {
            questList.get(currentQuestIndex).setCompleted(true); // if you have a `setCompleted` method
            currentQuestIndex++;
        }
    }



    public void initializeQuests() {
        questList.add(new Quest("The Proving!","Defeat the Slime Boss!"));
        questList.add(new Quest("Heart of The Wildlands","Find the Crystal."));

        System.out.println();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        setupInputHandling();
    }

    private void setupInputHandling() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.I) {
                boolean currentlyVisible = inventoryPane.isVisible();
                inventoryPane.setVisible(!currentlyVisible);
            }
        });
    }


    @FXML private CharacterController characterController;
    @FXML private SlimeBossController slimeBossController;
    private boolean isGamePaused = false;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private Node playerNode;
    SoundPlayer soundPlayer = new SoundPlayer();
//soundPlayer.play("/sounds/pickup.mp3");

    // Called to update HP bar and label
    public void updateHP() {
        int currentHP = characterController.getCurrentHP();
        int maxHP = characterController.getMaxHP();
        hpBar.setProgress((double) currentHP / maxHP);
        hpLabel.setText(currentHP + "/" + maxHP);
    }

    // Called to update level bar and label
    public void updateLevel() {
        levelLabel.setText("Lvl " + characterController.getCurrentLevel() + " - EXP: " + characterController.getCurrentLevelPoints() + "/" + characterController.getMaxLevelPoints());
        double progress = (double) characterController.getCurrentLevelPoints() / characterController.getMaxLevelPoints();
        levelBar.setProgress(progress);
    }

    public void showLevelUpOverlay(int level) {
        // Set label text
        levelUpLabel.setText("LEVEL UP!");
        levelUpSubLabel.setText("You reached level " + level + ". New skill unlocked!");

        // Styling the labels
        levelUpLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 48px; -fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, white, 10, 0.5, 0, 0);");
        levelUpSubLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;" +
                "-fx-effect: dropshadow(gaussian, lightblue, 5, 0.5, 0, 0);");

        // Style the pane background
        levelUpPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); " +
                "-fx-background-radius: 10; -fx-border-color: gold; " +
                "-fx-border-width: 2; -fx-border-radius: 10;");
        levelUpPane.setVisible(true);
        levelUpPane.setOpacity(0);
        levelUpPane.setScaleX(0.5);
        levelUpPane.setScaleY(0.5);

        // OPTIONAL: Play level-up sound
        try {
//            AudioClip levelUpSound = new AudioClip(getClass().getResource("/sounds/levelup.mp3").toString());
//            levelUpSound.play();
        } catch (Exception e) {
            System.out.println("Level-up sound missing or failed to play.");
        }

        // Zoom animation
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.4), levelUpPane);
        scaleUp.setFromX(0.5);
        scaleUp.setFromY(0.5);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);

        // Fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.4), levelUpPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Stay duration
        PauseTransition stay = new PauseTransition(Duration.seconds(2));

        // Fade-out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.4), levelUpPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> levelUpPane.setVisible(false));

        // Play animations in order
        SequentialTransition sequence = new SequentialTransition(scaleUp, fadeIn, stay, fadeOut);
        sequence.play();
    }

    public void handlePlayerDeath() {
        // Example logic: show game over screen, stop game loop, etc.
        soundPlayer.play("/heart_of_the_wildlands/assets/sounds/game_over.mp3");
        System.out.println("Game Over!");
        // Add more actions here, such as showing an alert or restarting the level.
        slimeBossController.pause();
        isGamePaused = true;

        Platform.runLater(() -> {
            slimeBossController.pause();
            // Create a semi-transparent overlay with darker dungeon feel
            VBox gameOverOverlay = new VBox(30);
            gameOverOverlay.setAlignment(Pos.CENTER);
            gameOverOverlay.setPrefSize(gameRoot.getWidth(), gameRoot.getHeight());
            gameOverOverlay.setStyle("-fx-background-color: rgba(10, 0, 0, 0.85);");

            // Create "GAME OVER" text with glowing effect
            Label gameOverLabel = new Label("☠ GAME OVER ☠");
            gameOverLabel.setTextFill(Color.DARKRED);
            gameOverLabel.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 64));
            gameOverLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 8, 0.8, 0, 0);");

            // Create styled buttons
            Button retryButton = new Button("↻ Retry");
            Button exitButton = new Button("⤫ Exit");

            for (Button button : new Button[]{retryButton, exitButton}) {
                button.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
                button.setStyle("""
            -fx-background-color: #222;
            -fx-text-fill: #fff;
            -fx-border-color: darkred;
            -fx-border-width: 2;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """);
                button.setOnMouseEntered(e -> button.setStyle("""
            -fx-background-color: darkred;
            -fx-text-fill: white;
            -fx-border-color: white;
            -fx-border-width: 2;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """));
                button.setOnMouseExited(e -> button.setStyle("""
            -fx-background-color: #222;
            -fx-text-fill: #fff;
            -fx-border-color: darkred;
            -fx-border-width: 2;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """));
            }

            retryButton.setOnAction(e -> restartGame(e));
            exitButton.setOnAction(e -> exitToMainMenu());

            // Arrange buttons in HBox
            HBox buttonBox = new HBox(30);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(exitButton, retryButton);

            // Add label and buttons to overlay
            gameOverOverlay.getChildren().addAll(gameOverLabel, buttonBox);

            // Add overlay to the game root
            gameRoot.getChildren().add(gameOverOverlay);

            // Refocus on root
            gameRoot.requestFocus();
        });

    }

    public void restartGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/heart_of_the_wildlands/views/game.fxml"));
            Parent pane = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load game view: " + e.getMessage());
        }
    }


    // Getter for all obstacles (useful for debugging or additional logic)
    public List<Rectangle> getObstacles() {
        return obstacles;
    }

    @FXML
    public void initialize() {
        obstacles = new ArrayList<>();
        obstacles.addAll(Arrays.asList(wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, wall9, wall10, wall11, wall12, wall13, wall14, wall15, wall16, wall17, wall18));


        // Load the character
        try {
            FXMLLoader characterLoader = new FXMLLoader(getClass().getResource("/heart_of_the_wildlands/views/character.fxml"));
            Node characterNode = characterLoader.load();
            this.characterController = characterLoader.getController();
            characterController.setGameController(this);
            characterPane.getChildren().add(characterNode); // Or wherever it belongs
            playerNode = characterController.getPlayer(); // ✅ now it's the actual sprite


//            characterController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/heart_of_the_wildlands/views/slime_boss.fxml"));
            Node slimeBossNode = loader.load();

            this.slimeBossController = loader.getController();
            slimeBossController.setGameController(this);
            slimeBossController.start();

            // Add the boss node into the slimebossPane
            slimebossPane.getChildren().add(slimeBossNode);
            slimeBossController.setPlayerNode(characterPane); // pass reference to the player
            slimeBossController.setCharacterController(characterController);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        characterController.gainExperience(150);

        Timeline attackDetectionLoop = new Timeline(
                new KeyFrame(Duration.millis(150), e -> {
                    Bounds attackBounds = characterController.getAttackBounds();
                    Bounds bossBounds = slimeBossController.getBossBounds();

                    if (attackBounds != null && bossBounds != null && bossBounds.intersects(attackBounds)) {
                        slimeBossController.takeDamage(characterController.getBasicAttack());
//                        soundPlayer.play("/heart_of_the_wildlands/assets/sounds/slime_hit_by_player.mp3");
                    }
                })
        );
        attackDetectionLoop.setCycleCount(Timeline.INDEFINITE);
        attackDetectionLoop.play();



        // Create Pause Menu UI
        VBox pauseMenu = new VBox(25);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle("-fx-background-color: rgba(20, 20, 20, 0.85); -fx-border-color: gold; -fx-border-width: 3px;");
        pauseMenu.setPadding(new Insets(60));
        pauseMenu.setPrefSize(1920, 1080);
        pauseMenu.setVisible(false); // Hide at start

// Title Label
        Label title = new Label("Game Paused");
        title.setTextFill(Color.GOLD);
        title.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 48));
        title.setEffect(new DropShadow(15, Color.DARKGOLDENROD));

// Buttons
        Button btnResume = new Button("Resume");
        Button btnQuest = new Button("Quest");
        Button btnExit = new Button("Exit to Main Menu");

// Common Style
        String baseButtonStyle = "-fx-background-color: linear-gradient(to bottom, #3e8e41, #2e7031);"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 2;"
                + "-fx-padding: 12 25 12 25;"
                + "-fx-cursor: hand;";

        String exitButtonStyle = "-fx-background-color: linear-gradient(to bottom, #b71c1c, #7f0000);"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 2;"
                + "-fx-padding: 12 25 12 25;"
                + "-fx-cursor: hand;";

// Apply Styles
        btnResume.setStyle(baseButtonStyle);
        btnQuest.setStyle(baseButtonStyle);
        btnExit.setStyle(exitButtonStyle);

// Button Hover Effects
        btnResume.setOnMouseEntered(e -> btnResume.setScaleX(1.1));
        btnResume.setOnMouseExited(e -> btnResume.setScaleX(1.0));

        btnQuest.setOnMouseEntered(e -> btnQuest.setScaleX(1.1));
        btnQuest.setOnMouseExited(e -> btnQuest.setScaleX(1.0));

        btnExit.setOnMouseEntered(e -> btnExit.setScaleX(1.1));
        btnExit.setOnMouseExited(e -> btnExit.setScaleX(1.0));

// Set Button Widths
        btnResume.setPrefWidth(240);
        btnQuest.setPrefWidth(240);
        btnExit.setPrefWidth(240);

        // Quest Pane (replaces FXML questPane)
        VBox questPane = new VBox(25);
        questPane.setPadding(new Insets(40));
        questPane.setAlignment(Pos.TOP_CENTER);
        questPane.setPrefSize(1920, 1080);
        questPane.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");

        VBox questCard = new VBox(20);
        questCard.setAlignment(Pos.TOP_LEFT);
        questCard.setPadding(new Insets(30));
        questCard.setMaxWidth(600);
        questCard.setStyle("-fx-background-color: rgba(40, 40, 40, 0.95); -fx-border-color: gold; -fx-border-width: 3px; -fx-background-radius: 15; -fx-border-radius: 15;");
        questCard.setEffect(new DropShadow(15, Color.DARKGOLDENROD));

        // Quest Title
        Label questTitleLabel = new Label("Current Quest");
        questTitleLabel.setTextFill(Color.web("#FFD700")); // bright gold
        questTitleLabel.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 34));
        questTitleLabel.setEffect(new DropShadow(8, Color.BLACK));
        questTitleLabel.setStyle(
                "-fx-letter-spacing: 2px;" +
                        "-fx-padding: 0 0 10 0;" +
                        "-fx-text-alignment: center;" +
                        "-fx-underline: true;"
        );

// Quest Description
        Label questDescLabel = new Label();
        questDescLabel.setTextFill(Color.web("#DDDDDD")); // soft white
        questDescLabel.setWrapText(true);
        questDescLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 18));
        questDescLabel.setMaxWidth(540);
        questDescLabel.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: gray;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 12;" +
                        "-fx-line-spacing: 6px;"
        );


// Button Actions
        btnResume.setOnAction(e -> {
            pauseMenu.setVisible(false);
            questPane.setVisible(false);
            slimeBossController.resume();
            isGamePaused = false;
        });


// Back Button
        Button btnBackToPause = new Button("Back to Pause Menu");
        btnBackToPause.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #444, #222);"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: white;"
                        + "-fx-border-width: 2;"
                        + "-fx-padding: 10 20 10 20;"
                        + "-fx-cursor: hand;"
        );

        btnBackToPause.setOnMouseEntered(e -> btnBackToPause.setScaleX(1.05));
        btnBackToPause.setOnMouseExited(e -> btnBackToPause.setScaleX(1.0));

        btnBackToPause.setOnAction(e -> {
            questPane.setVisible(false);
            pauseMenu.setVisible(true);
        });

        questCard.getChildren().addAll(questTitleLabel, questDescLabel, btnBackToPause);
        questPane.getChildren().add(questCard);
        questPane.setVisible(false);

// Initialize Quests
        initializeQuests();

        btnQuest.setOnAction(e -> {
            if (questList.size() == 0) {
                questTitleLabel.setText("No Quests Available");
                questDescLabel.setText("");
                return;
            }

            MyStringBuilder sb = new MyStringBuilder();
            for (int i = 0; i < questList.size(); i++) {
                Quest quest = questList.get(i);
                String statusIcon = quest.isCompleted() ? "✅" : "";

                sb.append("Quest ").append(i + 1).append(": ").append(quest.getTitle()).append(" ").append(statusIcon).append("\n")
                        .append("→ ").append(quest.getDescription()).append("\n\n");
            }

            questTitleLabel.setText("Your Quests");
            questDescLabel.setText(sb.toString());

            questPane.setVisible(true);
            pauseMenu.setVisible(false);
        });



        inventory.addItem("Health Potion", 1, "/heart_of_the_wildlands/assets/icon/potion.png");


        // path depends on your project




        btnExit.setOnAction(e -> exitToMainMenu());

// Add all components
        pauseMenu.getChildren().addAll(title, btnResume, btnQuest, btnExit);

        gameRoot.getChildren().add(pauseMenu); // Add on top of game UI
        gameRoot.getChildren().add(questPane);


        // Inventory should be hidden at start
        inventoryPane.setVisible(false);

        // Use a platform runLater to make sure the scene is available after loading
        javafx.application.Platform.runLater(() -> {
            Scene scene = inventoryPane.getScene(); // Get current scene from any UI element
            if (scene != null) {
                scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.I) {
                        boolean visible = inventoryPane.isVisible();
                        inventoryPane.setVisible(!visible);
                        if (!visible) showInventory(inventory); // refresh when opening
                        soundPlayer.play("/heart_of_the_wildlands/assets/sounds/inventory_open.mp3");
                    }
                });
            }
        });

        // Handle ESC key
        gameRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE && !pauseMenu.isVisible()) {
                pauseMenu.setVisible(true);
                slimeBossController.pause();
                isGamePaused = true;

                Platform.runLater(() -> gameRoot.requestFocus());
            }

            else if (questPane.isVisible()) {
                pauseMenu.setVisible(false);
                slimeBossController.pause();
                isGamePaused = true;

                Platform.runLater(() -> gameRoot.requestFocus());
            }

            else if (!isGamePaused && event.getCode() == KeyCode.SPACE) {
                characterController.playHitAnimation(); // bisa basic attack ketika tidak di pause
            }
        });



    }

    private void exitToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/heart_of_the_wildlands/views/main.fxml"));
            Parent pane = loader.load();

            // Get current stage from any node (like btnExit)
            Stage stage = (Stage) gameRoot.getScene().getWindow();
            stage.getScene().setRoot(pane);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load main.fxml: " + e.getMessage());
        }
    }

}
