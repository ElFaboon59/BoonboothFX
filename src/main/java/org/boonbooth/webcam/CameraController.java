package org.boonbooth.webcam;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.boonbooth.utils.FrameListener;
import org.boonbooth.utils.MatToImageConverter;
import org.opencv.core.Mat;

public class CameraController implements FrameListener {

    private final WebcamService service;
    private final ImageView imageView;
    private final MatToImageConverter converter = new MatToImageConverter();

    public CameraController(ImageView imageView, int camIndex) {
        this.imageView = imageView;
        this.service = new WebcamService(camIndex);
        this.service.setFrameListener(this);
    }

    public void start() {
        service.start();
    }

    public void stop() {
        service.stop();
    }

    @Override
    public void onFrame(Mat frame) {
        Platform.runLater(() -> imageView.setImage(converter.convert(frame)));
    }
}
