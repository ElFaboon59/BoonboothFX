package org.boonbooth;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.boonbooth.configuration.Config;
import org.boonbooth.video.VideoController;
import org.boonbooth.webcam.CameraController;

import java.io.InputStream;

public class BoonBoothApp extends Application {

    private VideoController videoController;
    private CameraController cameraController;

    @Override
    public void start(Stage stage) {

        OpenCV.loadLocally();

        // ImageView fond vidéo
        ImageView videoView = getBackgroundView();

        // ImageView webcam
        ImageView cameraView = getWebCamView();

        // Texte overlay
        Text overlay = getTextOverlay();

        StackPane root = new StackPane(videoView, cameraView, overlay);
        Scene scene = new Scene(root, Config.sceneWidth, Config.sceneHeight);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> {
                    Platform.exit();
                    System.exit(0);
                }
                default -> System.out.println("Autre touche pressée: " + event.getCode());
            }
        });

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

    private void stopAll() {
        if (videoController != null) videoController.stop();
        if (cameraController != null) cameraController.stop();
    }

    public static void main(String[] args) {
        launch();
    }

    private ImageView getBackgroundView(){
        ImageView videoView = new ImageView();
        videoView.setPreserveRatio(true);
        videoView.setFitWidth(Config.sceneWidth);
        videoView.setFitHeight(Config.sceneHeight);
        return videoView;
    }

    private ImageView getWebCamView(){
        ImageView cameraView = new ImageView();
        cameraView.setPreserveRatio(true);
        cameraView.setFitWidth(Config.sceneWidth * Config.webcamSize);
        cameraView.setFitHeight(Config.sceneHeight * Config.webcamSize);
        return cameraView;
    }

    private Text getTextOverlay(){
        Text overlay = new Text(Config.textValue);
        InputStream fontStream = getClass().getResourceAsStream(Config.textFont);
        Font customFont = Font.loadFont(fontStream, Config.textSize);
        overlay.setFont(customFont);
        overlay.setFill(Color.WHITE);

        // Glow + DropShadow
        Glow glow = new Glow(1.5);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(2);
        ds.setOffsetY(2);
        ds.setColor(Color.BLACK);
        overlay.setEffect(glow);
        glow.setInput(ds);

        return overlay;
    }
}




