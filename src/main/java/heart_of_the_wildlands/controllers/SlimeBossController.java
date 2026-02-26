package heart_of_the_wildlands.controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SlimeBossController implements Initializable {
    @FXML private ImageView slime_bossView;
    @FXML private ImageView projectileView;
    @FXML
    private AnchorPane slimeBossRoot;
    @FXML
    private ProgressBar hpBar;
    private Timeline slimeBallAnimation;
//    SoundPlayer soundPlayer = new SoundPlayer();


    public void pause() {
        if (shootTimer != null) {
            shootTimer.pause();
        }
        if (projectileAnim != null) {
            projectileAnim.pause();
        }
        if (idleAnim != null) {
            idleAnim.pause();
        }

        // Menjeda setiap move dari Slime Boss
    }

    public void resume() {
        if (shootTimer != null) {
            shootTimer.play();
        }
        if (projectileAnim != null) {
            projectileAnim.play();
        }
        if (idleAnim != null) {
            idleAnim.play();
        }
    }


    private Image[] idleFrames;
    private Image shootFrame;
    private Image[] slimeBallFrames;

    private int idleIndex = 0;
    private int slimeBallIndex = 0;

    private Timeline idleAnim;
    private Timeline projectileAnim;
    private Timeline shootTimer;
    private Timeline shot;
    private TranslateTransition projectileMove;
    private boolean isPaused = false;
    private boolean isDefeated = false;

    public boolean isDefeated() {
        return isDefeated;
    }


    private Node playerNode;

    public void setPlayerNode(Node playerNode) {
        this.playerNode = playerNode;
    }

    private CharacterController characterController;

    public void setCharacterController(CharacterController characterController) {
        this.characterController = characterController;
    }

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    private boolean hasHit = false; // Boolean hit dari player ke slime

    // Total hp dari slime
    private int maxHP = 250;
    private int currentHP = 250;

    public void takeDamage(int damage) {
        if (isDefeated) return;
        currentHP -= damage;
        if (currentHP <= 0) currentHP = 0;

        // Memperbaharui tampilan hp bar
//        double percent = (double) currentHP / maxHP;
        double percent = (double) currentHP / maxHP;
        hpBar.setProgress(percent);

        if (currentHP <= 0 && !isDefeated) {
            isDefeated = true;
            gameController.completeCurrentQuest();

            die(); // Call your death animation
            if (projectileAnim != null) {
                projectileAnim.stop();
            }

            if (shootTimer != null) {
                shootTimer.stop();
            }
            gameController.inventory.addItem("Slime Ball", 5, "/heart_of_the_wildlands/assets/icon/slimeBall.png");
            gameController.soundPlayer.play("/heart_of_the_wildlands/assets/sounds/item_received.mp3");
            characterController.gainExperience(150);
            characterController.gainHP(100);

        }
    }

    private void die() {
        isDefeated = true;

        // Menghentikan animasi tembakan slime ball
        if (projectileAnim != null) {
            projectileAnim.stop();
        }

        if (shootTimer != null) {
            shootTimer.stop();
        }


        // Memudar
        FadeTransition fade = new FadeTransition(Duration.seconds(1), slime_bossView);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        // Mengurangi skala object ketika mati
        ScaleTransition scale = new ScaleTransition(Duration.seconds(1), slime_bossView);
        scale.setToX(0);
        scale.setToY(0);

        ParallelTransition deathAnimation = new ParallelTransition(fade, scale);
        deathAnimation.setOnFinished(e -> {
            slime_bossView.setVisible(false);
            hpBar.setVisible(false);
            projectileView.setVisible(false); // hide slime ball
            if (shootTimer != null) {
                shootTimer.pause(); // or stop()
            }
            if (projectileAnim != null) {
                projectileAnim.pause(); // optional
            }
        });
        deathAnimation.play();
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //sprite dari slime boss
        URL boss1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_boss/slime_boss_1.png");
        URL boss2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_boss/slime_boss_2.png");
        URL boss3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_boss/slime_boss_3.png");
        URL boss_shot = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_boss/slime_boss_shot.png");

        //sprite dari slime ball
        URL ball1 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_ball/slime_ball_1.png");
        URL ball2 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_ball/slime_ball_2.png");
        URL ball3 = getClass().getResource("/heart_of_the_wildlands/assets/sprites/slime/slime_ball/slime_ball_3.png");

        //idle dari slime boss
        idleFrames = new Image[]{
                new Image(boss1.toExternalForm()),
                new Image(boss2.toExternalForm()),
                new Image(boss3.toExternalForm())
        };

        shootFrame = new Image(boss_shot.toExternalForm());

        slimeBallFrames = new Image[]{
                new Image(ball1.toExternalForm()),
                new Image(ball2.toExternalForm()),
                new Image(ball3.toExternalForm())
        };

        slime_bossView.setImage(idleFrames[0]);
        projectileView.setImage(slimeBallFrames[0]);

        projectileView.setVisible(false);


        // Keyframe animasi tembakan slime boss
        Timeline proximityChecker = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            if (playerNode != null) {
                double dx = playerNode.getLayoutX() - slime_bossView.getLayoutX();
                double dy = playerNode.getLayoutY() - slime_bossView.getLayoutY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < 100) {
                    shootAtPlayer(dx, dy);
                    projectileView.setVisible(true);
                }
            }
        }));

        Timeline collisionCheckTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> checkProjectileCollision())
        );
        collisionCheckTimeline.setCycleCount(Animation.INDEFINITE);
        collisionCheckTimeline.play();

        proximityChecker.setCycleCount(Animation.INDEFINITE);
        proximityChecker.play();

    }

    public void start() {
        startIdleAnimation();
        startShootLoop();
    }

    private void startIdleAnimation() {
        if (idleAnim != null) idleAnim.stop();

        idleAnim = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            slime_bossView.setImage(idleFrames[idleIndex]);
            idleIndex = (idleIndex + 1) % idleFrames.length;
        }));
        idleAnim.setCycleCount(Animation.INDEFINITE);
        idleAnim.play();
    }

    private void startShootLoop() {
        if (shootTimer != null) {
            shootTimer.stop(); // Prevent duplicate loops
        }

        shootTimer = new Timeline(new KeyFrame(Duration.seconds(3), e -> shoot()));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    private void shoot() {
        slime_bossView.setImage(shootFrame);
        launchProjectile();

        PauseTransition backToIdle = new PauseTransition(Duration.seconds(0.5));
        backToIdle.setOnFinished(e -> startIdleAnimation());
        backToIdle.play();
    }

    private void launchProjectile() {
        projectileView.setLayoutX(slime_bossView.getLayoutX());
        projectileView.setLayoutY(slime_bossView.getLayoutY() + 20);
        projectileView.setVisible(true);

        projectileAnim = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            projectileView.setImage(slimeBallFrames[slimeBallIndex]);
            slimeBallIndex = (slimeBallIndex + 1) % slimeBallFrames.length;
        }));
        projectileAnim.setCycleCount(Animation.INDEFINITE);
        projectileAnim.play();

        TranslateTransition move = new TranslateTransition(Duration.seconds(2), projectileView);
        move.setByX(-800);
        move.setOnFinished(e -> {
            projectileAnim.stop();
            projectileView.setVisible(false);
            projectileView.setTranslateX(0);
        });
        move.play();
    }

    public ImageView getProjectileView() {
        return projectileView;
    }

    private void shootAtPlayer(double dx, double dy) {
        // Normalize direction
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        double vx = dx / magnitude * 3;
        double vy = dy / magnitude * 3;

        // Tembakan dari arah Slime Boss
        projectileView.setLayoutX(slime_bossView.getLayoutX());
        projectileView.setLayoutY(slime_bossView.getLayoutY());
        projectileView.setVisible(true);

        hasHit = false;

       // Timeline untuk pelurunya
        shot = new Timeline(new KeyFrame(Duration.millis(20), evt -> {
            projectileView.setLayoutX(projectileView.getLayoutX() + vx);
            projectileView.setLayoutY(projectileView.getLayoutY() + vy);

            checkProjectileCollision(); // Mengecek collision/tabrakan framenya
        }));

        shot.setCycleCount(50);
        shot.play();
    }


    // cek collision dari peluru slime ballnya
    private void checkProjectileCollision() {
        if (characterController.getPlayer() == null || !projectileView.isVisible()) return;

        Bounds projectileBounds = projectileView.getBoundsInParent();
        Bounds playerBounds = characterController.getPlayer().getBoundsInParent();


        if (projectileBounds.intersects(playerBounds)) {
            hasHit = true;

            // Peluru akan hilang ketika berhasil mengenai player
            projectileView.setVisible(false);
            projectileView.setLayoutX(-999);
            projectileView.setLayoutY(-999);


            // Damage yang diberikan Slime Boss dari pelurunya terhadap player
            if (characterController != null) {
                characterController.receiveDamage(20); // Ini angka damagenya
            }
        }
    }


    public Bounds getBossBounds() {
        return slime_bossView.getBoundsInParent();
    }


}
