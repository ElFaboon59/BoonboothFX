package org.boonbooth.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;

public class MatToImageConverter {

    private WritableImage image;

    public Image convert(Mat frame) {
        int width = frame.width();
        int height = frame.height();

        if (image == null || (int) image.getWidth() != width || (int) image.getHeight() != height) {
            image = new WritableImage(width, height);
        }

        PixelWriter pw = image.getPixelWriter();
        byte[] buffer = new byte[width * height * (int) frame.elemSize()];
        frame.get(0, 0, buffer);

        if (frame.channels() == 3) {
            // BGR → RGB
            byte tmp;
            for (int i = 0; i < buffer.length; i += 3) {
                tmp = buffer[i];
                buffer[i] = buffer[i + 2];
                buffer[i + 2] = tmp;
            }
            pw.setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), buffer, 0, width * 3);
        }
        else if (frame.channels() == 1) {
            pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width);
        }

        return image;
    }
}
