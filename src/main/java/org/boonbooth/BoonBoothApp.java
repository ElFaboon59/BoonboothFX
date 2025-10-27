package org.boonbooth;

import javafx.application.Application;
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
        ImageView videoView = new ImageView();
        videoView.setPreserveRatio(true);
        videoView.setFitWidth(1280);
        videoView.setFitHeight(720);

        // ImageView webcam
        ImageView cameraView = new ImageView();
        cameraView.setPreserveRatio(true);
        cameraView.setFitWidth(1280 * 0.66);
        cameraView.setFitHeight(720 * 0.66);

        // Texte overlay
        Text overlay = new Text("BoonBooth");
        InputStream fontStream = getClass().getResourceAsStream("/TheFrightHouseDEMO.otf");
        Font customFont = Font.loadFont(fontStream, 160);
        overlay.setFont(customFont);
        overlay.setFill(Color.WHITE);

        // Glow + DropShadow
        Glow glow = new Glow(0.8);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(2);
        ds.setOffsetY(2);
        ds.setColor(Color.BLACK);
        overlay.setEffect(glow);
        glow.setInput(ds);

        StackPane root = new StackPane(videoView, cameraView, overlay);
        Scene scene = new Scene(root, 1280, 720);

        // set webcam pane
        StackPane.setAlignment(cameraView, Pos.TOP_RIGHT);
        StackPane.setMargin(cameraView, new Insets(20, 20, 20, 20));

        // Position texte en bas à gauche avec marge
        StackPane.setAlignment(overlay, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setMargin(overlay, new javafx.geometry.Insets(20));

        stage.setTitle("Superposition Video + Webcam");
        stage.setScene(scene);
        stage.show();

        // Chemin de ta vidéo locale
        String videoPath = Config.backgroundFile;

        videoController = new VideoController(videoView, videoPath);
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
}




