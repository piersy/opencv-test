/**
 * Created by piers on 05/08/14.
 */
/*
 * Copyright (c) 2011. Philipp Wagner <bytefish[at]gmx[dot]de>.
 * Released to public domain under terms of the BSD Simplified license.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the organization nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 *   See <http://www.opensource.org/licenses/bsd-license>
 */


import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.contrib.Contrib;
import org.opencv.core.Core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


//Mat image = Highgui.imread(getClass().getResource("/lena.png").getPath());
//static Mat norm_0_255(InputArray _src) {
//        Mat src = _src.getMat();
//        // Create and return normalized image:
//        Mat dst;
//        switch(src.channels()) {
//        case 1:
//        cv::normalize(_src, dst, 0, 255, NORM_MINMAX, CV_8UC1);
//        break;
//        case 3:
//        cv::normalize(_src, dst, 0, 255, NORM_MINMAX, CV_8UC3);
//        break;
//default:
//        src.copyTo(dst);
//        break;
//        }
//        return dst;
//        }


//static void read_csv(const string& filename, vector<Mat>& images, vector<int>& labels, char separator = ';') {
//        std::ifstream file(filename.c_str(), ifstream::in);
//        if (!file) {
//        string error_message = "No valid input file was given, please check the given filename.";
//        CV_Error(CV_StsBadArg, error_message);
//        }
//        string line, path, classlabel;
//        while (getline(file, line)) {
//        stringstream liness(line);
//        getline(liness, path, separator);
//        getline(liness, classlabel);
//        if(!path.empty() && !classlabel.empty()) {
//        images.push_back(imread(path, 0));
//        labels.push_back(atoi(classlabel.c_str()));
//        }
//        }
//        }

public class Eigenfaces {

    public static void readCsv(String csvfile, List<Mat> mats, List<Integer> labels) throws IOException {

        if (!Files.exists(Paths.get(csvfile))) {
            throw new FileNotFoundException(csvfile);
        }
        String line, path, classlabel;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvfile))))){
            while((line=br.readLine())!=null){
                String[] pathAndGroup = line.split(";");
                mats.add(Highgui.imread(pathAndGroup[0], 0));
                labels.add(Integer.parseInt(pathAndGroup[1]));
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: java -jar 'the jar' <csv.ext> <output_folder> ");
            System.exit(1);
        }
        String outputFolder = args[1];
        String csv = args[0];


        //        // These vectors hold the images and corresponding labels.
        List<Mat> mats = new ArrayList<Mat>();
        List<Integer> labels = new ArrayList<Integer>();
//        // Read in the data. This can fail if no valid
//        // input filename is given.
        try {
            Eigenfaces.readCsv(csv, mats, labels);
        } catch (Exception e) {
            System.out.println("Failed to load images");
            System.exit(1);
        }
        // Quit if there are not enough images for this demo.
        if(mats.size() <= 1) {
            throw new RuntimeException("This demo needs at least 2 images to work. Please add more images to your data set!");
        }

//        // Get the height from the first image. We'll need this
//        // later in code to reshape the images to their original
//        // size:
          int height = mats.get(0).rows();
//        // The following lines simply get the last images from
//        // your dataset and remove it from the vector. This is
//        // done, so that the training data (which we learn the
//        // cv::FaceRecognizer on) and the test data we test
//        // the model with, do not overlap.

        Mat testSample = mats.remove(mats.size()-1);
        int testLabel = labels.remove(labels.size() - 1);
        //        // The following lines create an Eigenfaces model for
//        // face recognition and train it with the images and
//        // labels read from the given CSV file.
//        // This here is a full PCA, if you just want to keep
//        // 10 principal components (read Eigenfaces), then call
//        // the factory method like this:
//        //
//        //      cv::createEigenFaceRecognizer(10);
//        //
//        // If you want to create a FaceRecognizer with a
//        // confidence threshold (e.g. 123.0), call it with:
//        //
//        //      cv::createEigenFaceRecognizer(10, 123.0);
//        //
//        // If you want to use _all_ Eigenfaces and have a threshold,
//        // then call the method like this:
//        //
//        //      cv::createEigenFaceRecognizer(0, 123.0);
//        //
//        Ptr<FaceRecognizer> model = createEigenFaceRecognizer();
//        model->train(images, labels);


    }



//        // The following line predicts the label of a given
//        // test image:
//        int predictedLabel = model->predict(testSample);
//        //
//        // To get the confidence of a prediction call the model with:
//        //
//        //      int predictedLabel = -1;
//        //      double confidence = 0.0;
//        //      model->predict(testSample, predictedLabel, confidence);
//        //
//        string result_message = format("Predicted class = %d / Actual class = %d.", predictedLabel, testLabel);
//        cout << result_message << endl;
//        // Here is how to get the eigenvalues of this Eigenfaces model:
//        Mat eigenvalues = model->getMat("eigenvalues");
//        // And we can do the same to display the Eigenvectors (read Eigenfaces):
//        Mat W = model->getMat("eigenvectors");
//        // Get the sample mean from the training data
//        Mat mean = model->getMat("mean");
//        // Display or save:
//        if(argc == 2) {
//        imshow("mean", norm_0_255(mean.reshape(1, images[0].rows)));
//        } else {
//        imwrite(format("%s/mean.png", output_folder.c_str()), norm_0_255(mean.reshape(1, images[0].rows)));
//        }
//        // Display or save the Eigenfaces:
//        for (int i = 0; i < min(10, W.cols); i++) {
//        string msg = format("Eigenvalue #%d = %.5f", i, eigenvalues.at<double>(i));
//        cout << msg << endl;
//        // get eigenvector #i
//        Mat ev = W.col(i).clone();
//        // Reshape to original size & normalize to [0...255] for imshow.
//        Mat grayscale = norm_0_255(ev.reshape(1, height));
//        // Show the image & apply a Jet colormap for better sensing.
//        Mat cgrayscale;
//        applyColorMap(grayscale, cgrayscale, COLORMAP_JET);
//        // Display or save:
//        if(argc == 2) {
//        imshow(format("eigenface_%d", i), cgrayscale);
//        } else {
//        imwrite(format("%s/eigenface_%d.png", output_folder.c_str(), i), norm_0_255(cgrayscale));
//        }
//        }
//
//        // Display or save the image reconstruction at some predefined steps:
//        for(int num_components = min(W.cols, 10); num_components < min(W.cols, 300); num_components+=15) {
//        // slice the eigenvectors from the model
//        Mat evs = Mat(W, Range::all(), Range(0, num_components));
//        Mat projection = subspaceProject(evs, mean, images[0].reshape(1,1));
//        Mat reconstruction = subspaceReconstruct(evs, mean, projection);
//        // Normalize the result:
//        reconstruction = norm_0_255(reconstruction.reshape(1, images[0].rows));
//        // Display or save:
//        if(argc == 2) {
//        imshow(format("eigenface_reconstruction_%d", num_components), reconstruction);
//        } else {
//        imwrite(format("%s/eigenface_reconstruction_%d.png", output_folder.c_str(), num_components), reconstruction);
//        }
//        }
//        // Display if we are not writing to an output folder:
//        if(argc == 2) {
//        waitKey(0);
//        }
//        return 0;
//        }


}