import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfByte;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class CameraStreamApp extends Application {

    private VideoCapture capture;
    private ImageView imageView;

    @Override
    public void start(Stage stage) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        OpenCV.loadShared();
        imageView = new ImageView();
        imageView.setFitWidth(640);
        imageView.setFitHeight(480);

        Text overlay = new Text("📸 Caméra en direct");
        overlay.setFill(Color.WHITE);
        overlay.setFont(Font.font(20));
        overlay.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 4, 0.5, 1, 1);");

        StackPane root = new StackPane(imageView, overlay);
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Streaming Caméra JavaFX");
        stage.setScene(scene);
        stage.show();

        startCamera();
        stage.setOnCloseRequest(e -> stopCamera());
    }

    private void startCamera() {
        capture = new VideoCapture(0); // 0 = webcam par défaut
        if (!capture.isOpened()) {
            System.err.println("Impossible d'ouvrir la caméra !");
            return;
        }

        AnimationTimer timer = new AnimationTimer() {
            private final Mat frame = new Mat();
            private final MatOfByte buffer = new MatOfByte();

            @Override
            public void handle(long now) {
                if (capture.read(frame)) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
                    Imgcodecs.imencode(".bmp", frame, buffer);
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(buffer.toArray()));
                        Image fxImage = SwingFXUtils.toFXImage(img, null);
                        imageView.setImage(fxImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.start();
    }

    private void stopCamera() {
        if (capture != null && capture.isOpened()) {
            capture.release();
        }
        Platform.exit();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
