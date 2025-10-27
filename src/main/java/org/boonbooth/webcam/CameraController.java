package org.boonbooth.webcam;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.boonbooth.utils.FrameListener;
import org.boonbooth.utils.MatToImageConverter;
import org.opencv.core.Core;
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

        // Flip vertical (effet miroir horizontal)
        Mat flipped = new Mat();
        Core.flip(frame, flipped, +1); // +1 = flip horizontal (effet miroir)
        // 0 = flip vertical, -1 = flip horizontal + vertical

        Platform.runLater(() -> imageView.setImage(converter.convert(flipped)));
    }
}
