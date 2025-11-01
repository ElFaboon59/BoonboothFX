package org.boonbooth;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nu.pattern.OpenCV;
import org.boonbooth.configuration.Config;
import org.boonbooth.utils.JavaFxHelper;
import org.boonbooth.video.VideoController;
import org.boonbooth.webcam.CameraController;

public class BoonBoothApp extends Application {

    private VideoController videoController;
    private CameraController cameraController;

    @Override
    public void start(Stage stage) {

        OpenCV.loadLocally();

        // ImageView fond vidéo
        ImageView videoView = JavaFxHelper.getBackgroundView();

        // ImageView webcam
        ImageView cameraView = JavaFxHelper.getWebCamView();
        cameraView.setOpacity(0);

        // Texte overlay
        Text overlay = JavaFxHelper.getTextOverlay();

        Scene scene = getScene(videoView, cameraView, overlay);

        // set webcam pane
        StackPane.setAlignment(cameraView, Pos.TOP_RIGHT);
        StackPane.setMargin(cameraView, new Insets(20, 20, 20, 20));

        // Position texte en bas à gauche avec marge
        StackPane.setAlignment(overlay, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setMargin(overlay, new javafx.geometry.Insets(20));

        stage.setTitle(Config.title);
        stage.setScene(scene);
        stage.show();

        videoController = new VideoController(videoView, Config.backgroundFile);
        cameraController = new CameraController(cameraView, 0);
        videoController.start();
        cameraController.start();

        stage.setOnCloseRequest(e -> stopAll());
    }

    private static Scene getScene(ImageView videoView, ImageView cameraView, Text overlay) {
        StackPane root = new StackPane(videoView, cameraView, overlay);
        Scene scene = new Scene(root, Config.sceneWidth, Config.sceneHeight);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), cameraView);
        fade.setFromValue(0);   // Départ: transparent
        fade.setToValue(1);     // Arrivée: opaque

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> {
                    Platform.exit();
                    System.exit(0);
                }
                case SPACE -> { fade.stop();  fade.playFromStart(); }
                default -> System.out.println("Autre touche pressée: " + event.getCode());
            }
        });
        return scene;
    }

    private void stopAll() {
        if (videoController != null) videoController.stop();
        if (cameraController != null) cameraController.stop();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}




