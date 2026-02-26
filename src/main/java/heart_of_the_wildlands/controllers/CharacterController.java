package heart_of_the_wildlands.controllers;

import heart_of_the_wildlands.inventory.*;
import heart_of_the_wildlands.skill.SkillTree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class CharacterController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView player;

    public ImageView getPlayer() {
        return player;
    }

    private SkillTree skillTree;

    private Image[] walkDownFrames;
    private Image[] walkUpFrames;
    private Image[] walkRightFrames;
    private Image[] walkLeftFrames;
    private Image[] hitsFrames;
    private int frameIndex = 0;

    private final Set<KeyCode> activeKeys = new HashSet<>();
    private Timeline gameLoop;
    private Bounds attackBounds;

    private int currentHP = 100;
    private int maxHP = 100;
    private int currentLevel = 1;
    private int currentLevelPoints = 0;
    private int maxLevelPoints = 100;
    private int basicAttack = 5;


    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private Inventory inventory = new Inventory();

    public Inventory getInventory() {
        return inventory;
    }

    public void receiveDamage(int damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            gameController.updateHP();
            die(); // Call this method when HP reaches 0
        } else {
            gameController.updateHP();
        }
    }

    private void die() {
        // You can disable movement, show death screen, etc.
        System.out.println("Player has died.");
        gameController.handlePlayerDeath(); // Call a method in GameController to manage death (optional)
    }


    public void updateStats() {
        if (gameController != null) {
            gameController.updateHP();
            gameController.updateLevel();
        }
    }

    public int getCurrentHP() { return currentHP; }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getMaxHP() { return maxHP; }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevelPoints() {
        return currentLevelPoints;
    }

    public void setCurrentLevelPoints(int currentLevelPoints) {
        this.currentLevelPoints = currentLevelPoints;
    }

    public int getMaxLevelPoints() {
        return maxLevelPoints;
    }

    public void setMaxLevelPoints(int maxLevelPoints) {
        this.maxLevelPoints = maxLevelPoints;
    }

    public int getBasicAttack() {
        return basicAttack;
    }

    public void setBasicAttack(int basicAttack) {
        this.basicAttack = basicAttack;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player.setFitWidth(100);   // or 32, or whatever size fits your game's scale
        player.setFitHeight(100);
        player.setPreserveRatio(true); // Optional

        URL down1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/down1.png");
        URL down2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/down2.png");
        URL down3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/down3.png");

        URL up1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/up1.png");
        URL up2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/up2.png");
        URL up3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/up3.png");
        URL up4 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/up4.png");

        URL right1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/right1.png");
        URL right2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/right2.png");
        URL right3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/right3.png");
        URL right4 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/right4.png");

        URL left1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/left1.png");
        URL left2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/left2.png");
        URL left3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/left3.png");
        URL left4 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/left4.png");

        URL hits1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/hits1.png");
        URL hits2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/hits2.png");
        URL hits3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/hits3.png");
        URL hits4 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/player/hits4.png");


        if (down1 == null || down2 == null || up1 == null || up2 == null || up3 == null || up4 == null || right1 == null || right2 == null || right3 == null || right4 == null || left1 == null || left2 == null || left3 == null || left4 == null) {
            System.out.println("❌ Image not found!");
            return;
        }

        walkDownFrames = new Image[]{
                new Image(down1.toExternalForm()),
                new Image(down2.toExternalForm()),
                new Image(down3.toExternalForm())
        };

        walkUpFrames = new Image[]{
                new Image(up1.toExternalForm()),
                new Image(up2.toExternalForm()),
                new Image(up3.toExternalForm()),
                new Image(up4.toExternalForm())
        };

        walkRightFrames = new Image[]{
                new Image(right1.toExternalForm()),
                new Image(right2.toExternalForm()),
                new Image(right3.toExternalForm()),
                new Image(right4.toExternalForm())
        };

        walkLeftFrames = new Image[]{
                new Image(left1.toExternalForm()),
                new Image(left2.toExternalForm()),
                new Image(left3.toExternalForm()),
                new Image(left4.toExternalForm())
        };

        hitsFrames = new Image[]{
                new Image(hits1.toExternalForm()),
                new Image(hits2.toExternalForm()),
                new Image(hits3.toExternalForm()),
                new Image(hits4.toExternalForm())
        };

        player.setImage(walkUpFrames[0]);
        player.setImage(walkRightFrames[0]);
        player.setImage(walkLeftFrames[0]);
        player.setImage(walkDownFrames[0]);
        player.setImage(hitsFrames[0]);

        gameLoop = new Timeline(new KeyFrame(Duration.millis(120), e -> {
//          ====================================================================================================================================================
//          =                                                               SIDE                                                                               =
//          ====================================================================================================================================================
            if (activeKeys.contains(KeyCode.A) || activeKeys.contains(KeyCode.LEFT)) {
                frameIndex = (frameIndex + 1) % walkLeftFrames.length;
                player.setImage(walkLeftFrames[frameIndex]);
                double nextX = player.getLayoutX() - 10;
                if (!isCollision(nextX, player.getLayoutY())) {
                    player.setLayoutX(nextX);
                }

            }
            if (activeKeys.contains(KeyCode.D) || activeKeys.contains(KeyCode.RIGHT)) {
                frameIndex = (frameIndex + 1) % walkRightFrames.length;
                player.setImage(walkRightFrames[frameIndex]);
                double nextX = player.getLayoutX() + 10;
                if (!isCollision(nextX, player.getLayoutY())) {
                    player.setLayoutX(nextX);
                }
            }
//          ====================================================================================================================================================


//          ====================================================================================================================================================
//          =                                                               DOWN                                                                               =
//          ====================================================================================================================================================
            if (activeKeys.contains(KeyCode.S) || activeKeys.contains(KeyCode.DOWN)) {
                frameIndex = (frameIndex + 1)  % walkDownFrames.length;
                player.setImage(walkDownFrames[frameIndex]);
                double nextY = player.getLayoutY() + 10;
                if (!isCollision(player.getLayoutX(), nextY)) {
                    player.setLayoutY(nextY);
                }
            }
//          ====================================================================================================================================================


//          ====================================================================================================================================================
//          =                                                               UP                                                                                 =
//          ====================================================================================================================================================
            if (activeKeys.contains(KeyCode.W) || activeKeys.contains(KeyCode.UP)) {
                frameIndex = (frameIndex + 1) % walkUpFrames.length;
                player.setImage(walkUpFrames[frameIndex]);
                double nextY = player.getLayoutY() - 10;
                if (!isCollision(player.getLayoutX(), nextY)) {
                    player.setLayoutY(nextY);
                }
            }
//          ====================================================================================================================================================
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();



        rootPane.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            // Block all movement inputs during attack
            if (isAttacking) {
                if (code == KeyCode.SPACE) {
                    playHitAnimation(); // still allow re-attacking if you want
                }
                return; // block movement keys like W, A, S, D
            }

            // If not attacking
            if (code == KeyCode.SPACE && activeKeys.isEmpty()) {
                playHitAnimation(); // only attack when character is not moving
            } else {
                activeKeys.add(code); // allow movement
            }
        });


        rootPane.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
        });

        Platform.runLater(() -> rootPane.requestFocus());
    }

    private boolean isCollision(double nextX, double nextY) {
        if (gameController == null || gameController.getObstacles() == null) {
            System.out.println("⚠️ GameController or obstacles not set!");
            return false; // Allow movement for now
        }

        double width = player.getFitWidth();
        double height = player.getFitHeight();
        BoundingBox predictedBounds = new BoundingBox(nextX, nextY, width, height);

        for (Rectangle wall : gameController.getObstacles()) {
            if (wall == null) continue;
            Bounds wallBounds = wall.localToScene(wall.getBoundsInLocal());
            if (predictedBounds.intersects(wallBounds)) return true;
        }

        return false;
    }

    private boolean isAttacking = false;

    public void playHitAnimation() {
        if (isAttacking || hitsFrames == null || hitsFrames.length == 0) return;
        isAttacking = true;

        Timeline hitTimeline = new Timeline();
        for (int i = 0; i < hitsFrames.length; i++) {
            int frameIndex = i;
            hitTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(150 * i), e -> player.setImage(hitsFrames[frameIndex]))
            );
        }

        // Determine direction of attack (for now assume facing right)
        double attackX = player.getLayoutX() + player.getFitWidth();
        double attackY = player.getLayoutY();
        double attackWidth = 20;  // sword length
        double attackHeight = player.getFitHeight();

            attackBounds = new BoundingBox(attackX, attackY, attackWidth, attackHeight);

// Clear attack hitbox after short delay
        Timeline clearHitbox = new Timeline(new KeyFrame(Duration.millis(400), e2 -> attackBounds = null));
        clearHitbox.setCycleCount(1);
        clearHitbox.play();


        hitTimeline.setOnFinished(e -> isAttacking = false); // allow movement again
        hitTimeline.setCycleCount(1);
        hitTimeline.play();
    }

    public void levelUp() {
        currentLevel++;
        gameController.soundPlayer.play("/heart_of_the_wildlands/assets/sounds/level_up.mp3");
        System.out.println("Level Up! You are now level " + currentLevel);

        if (skillTree == null) skillTree = new SkillTree(); // safety check

        // Save old skills before checking new ones
        List<String> oldSkills = new ArrayList<>(skillTree.getAllUnlockedSkills());

        // Trigger the unlocking process
        List<String> newlyUnlocked = skillTree.getAvailableSkills(currentLevel);

        // Show only the new ones
        System.out.println("New Skills unlocked:");
        if (newlyUnlocked.isEmpty()) {
            System.out.println("- (No new skills)");
        } else {
            newlyUnlocked.forEach(skill -> System.out.println("- " + skill));
        }

        // Show all current unlocked skills
        System.out.println("All Skills you have:");
        skillTree.getAllUnlockedSkills().forEach(skill -> System.out.println("* " + skill));

        if (gameController != null) {
            gameController.showLevelUpOverlay(currentLevel);// <<==== CALL IT HERE
        }

        updateStats(); // optional: UI update
    }

    public Bounds getAttackBounds() {
        return attackBounds;
    }


    public void gainExperience(int amount) {
        currentLevelPoints += amount;
        while (currentLevelPoints >= maxLevelPoints) {
            currentLevelPoints -= maxLevelPoints;
            levelUp();
        }
    }

    public void gainHP(int amount) {
        currentHP += amount;

        if (currentHP > maxHP) {
            currentHP = maxHP;
        }

        gameController.updateHP(); // Optional: update UI if you have one
    }



}
