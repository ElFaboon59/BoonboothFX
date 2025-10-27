package org.boonbooth.utils;

import org.opencv.core.Mat;


public interface FrameListener {
    void onFrame(Mat frame);
}
