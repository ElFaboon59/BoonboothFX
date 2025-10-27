package org.boonbooth.webcam;

import org.boonbooth.utils.FrameListener;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.core.Mat;

import java.util.concurrent.*;

public class WebcamService {

    private VideoCapture capture;
    private ScheduledExecutorService executor;
    private FrameListener listener;
    private final int camIndex;
    private boolean running = false;

    public WebcamService(int cameraIndex) {
        this.camIndex = cameraIndex;
    }

    public void setFrameListener(FrameListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (running) return;

        capture = new VideoCapture(camIndex);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);

        if (!capture.isOpened()) {
            System.err.println("Erreur: impossible d’ouvrir la caméra " + camIndex);
            return;
        }

        running = true;

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::captureFrame, 0, 33, TimeUnit.MILLISECONDS);
    }

    private void captureFrame() {
        if (!running || listener == null) return;

        Mat frame = new Mat();
        if (capture.read(frame) && !frame.empty()) {
            listener.onFrame(frame);
        } else {
            System.err.println("Erreur lecture webcam");
        }
    }

    public void stop() {
        running = false;

        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }

        if (capture != null && capture.isOpened()) {
            capture.release();
        }
    }

    public boolean isOpened() {
        return capture != null && capture.isOpened();
    }
}
