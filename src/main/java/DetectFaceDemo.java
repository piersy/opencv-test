import org.bytedeco.javacpp.opencv_objdetect;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
public class DetectFaceDemo {
    public void run() {
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        opencv_objdetect.CascadeClassifier faceDetector = new opencv_objdetect.CascadeClassifier(getClass().getResource("/lbpcascade_frontalface.xml").getPath());
        Mat image = imread(getClass().getResource("/lena.png").getPath());

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        Rect faceDetections = new Rect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections));

        // Draw a bounding box around each face.

        rectangle(image, new Point(faceDetections.x(), faceDetections.y()), new Point(faceDetections.x() + faceDetections.width(), faceDetections.y() + faceDetections.height()), new Scalar(0, 255, 0, 0));

        // Save the visualized detection.
        String filename = "build/faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        imwrite(filename, image);
    }
}



