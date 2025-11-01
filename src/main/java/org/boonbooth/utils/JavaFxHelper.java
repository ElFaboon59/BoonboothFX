package org.boonbooth.utils;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.boonbooth.configuration.Config;

import java.io.InputStream;

public class JavaFxHelper {

    public static ImageView getBackgroundView(){
        ImageView videoView = new ImageView();
        videoView.setPreserveRatio(true);
        videoView.setFitWidth(Config.sceneWidth);
        videoView.setFitHeight(Config.sceneHeight);
        return videoView;
    }

    public static ImageView getWebCamView(){
        ImageView cameraView = new ImageView();
        cameraView.setPreserveRatio(true);
        cameraView.setFitWidth(Config.sceneWidth * Config.webcamSize);
        cameraView.setFitHeight(Config.sceneHeight * Config.webcamSize);
        return cameraView;
    }

    public static Text getTextOverlay(){
        Text overlay = new Text(Config.textValue);
        InputStream fontStream = JavaFxHelper.class.getResourceAsStream(Config.textFont);
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
