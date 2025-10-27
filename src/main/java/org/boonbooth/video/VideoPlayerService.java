package org.boonbooth.video;

import org.boonbooth.utils.FrameListener;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VideoPlayerService {

    private final VideoCapture capture;
    private ScheduledExecutorService executor;
    private final FrameListener listener;

    public VideoPlayerService(String filePath, FrameListener listener) {
        this.listener = listener;
        this.capture = new VideoCapture(filePath);
    }

    public void start() {
        if (!capture.isOpened()) {
            System.err.println("Impossible d’ouvrir la vidéo !");
            return;
        }

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::grabFrame, 0, 33, TimeUnit.MILLISECONDS);
    }

    private void grabFrame() {
        Mat frame = new Mat();
        if (capture.read(frame) && !frame.empty()) {
            listener.onFrame(frame);
        } else {
            // On remet la lecture au début
            capture.set(Videoio.CAP_PROP_POS_FRAMES, 0);
        }
    }

    public void stop() {
        if (executor != null) executor.shutdown();
        if (capture != null && capture.isOpened()) capture.release();
    }
}