package org.boonbooth.video;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.boonbooth.utils.FrameListener;
import org.boonbooth.utils.MatToImageConverter;
import org.opencv.core.Mat;

public class VideoController implements FrameListener {

    private final VideoPlayerService videoService;
    private final ImageView imageView;
    private final MatToImageConverter converter = new MatToImageConverter();

    public VideoController(ImageView imageView, String videoPath) {
        this.imageView = imageView;
        this.videoService = new VideoPlayerService(videoPath, this);
    }

    public void start() {
        videoService.start();
    }

    public void stop() {
        videoService.stop();
    }

    @Override
    public void onFrame(Mat frame) {
        Platform.runLater(() -> imageView.setImage(converter.convert(frame)));
    }
}
