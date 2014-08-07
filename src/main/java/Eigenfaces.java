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


import org.bytedeco.javacpp.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class Eigenfaces {


    static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    public static void readCsv(String csvfile, List<Mat> mats, List<Integer> labels) throws IOException {

        if (!Files.exists(Paths.get(csvfile))) {
            throw new FileNotFoundException(csvfile);
        }
        String line, path, classlabel;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvfile))))){
            while((line=br.readLine())!=null){
                String[] pathAndGroup = line.split(";");
                mats.add(imread(pathAndGroup[0], 0));
                labels.add(Integer.parseInt(pathAndGroup[1]));
            }
        }
    }

//    static Mat norm_0_255(CvMat src) {
//        // Create and return normalized image:
//        CvMat dst = null;
//        switch(src.channels()) {
//            case 1:
//                cvNormalize(src, dst, 0D, 255D, NORM_MINMAX, null);
//                break;
//            case 3:
//                cvNormalize(src, dst, 0d, 255d, NORM_MINMAX, null);
//                break;
//            default:
//                src.copyTo(dst);
//                break;
//        }
//        return dst;
//    }

    public static void main(String[] args) {
        // Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_core.class);
        Loader.load(opencv_photo.class);
        Loader.load(opencv_nonfree.class);
        Loader.load(opencv_contrib.class);
        Loader.load(opencv_highgui.class);
        System.out.println(System.getProperty("user.dir"));
        if (args.length < 2) {
            System.out.println("usage: java -jar 'the jar' <csv.ext> <output_folder> ");
            System.exit(1);
        }
        String outputFolder = args[1];
        String csv = args[0];

        csv = "src/test/resources/Cambridge_FaceDB/"+csv;


        //        // These vectors hold the images and corresponding labels.
        List<Mat> images = new ArrayList<Mat>();
        List<Integer> labels = new ArrayList<Integer>();
//        // Read in the data. This can fail if no valid
//        // input filename is given.
        try {
            Eigenfaces.readCsv(csv, images, labels);
        } catch (Exception e) {
            System.out.println("Failed to load images");
            System.exit(1);
        }
        // Quit if there are not enough images for this demo.
        if(images.size() <= 1) {
            throw new RuntimeException("This demo needs at least 2 images to work. Please add more images to your data set!");
        }

//        // Get the height from the first image. We'll need this
//        // later in code to reshape the images to their original
//        // size:
        int height = images.get(0).rows();
//        // The following lines simply get the last images from
//        // your dataset and remove it from the vector. This is
//        // done, so that the training data (which we learn the
//        // cv::FaceRecognizer on) and the test data we test
//        // the model with, do not overlap.

        Mat testSample = images.remove(images.size()-1);
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

        FaceRecognizer model = createEigenFaceRecognizer();


        //For some reason the
        Mat[] matArray = new Mat[images.size()];
        matArray = images.toArray(matArray);
        int[] array = toIntArray(labels);
        Mat matlabels = new Mat(new IntPointer(array));
        MatVector matVector = new MatVector(matArray);
        model.train(matVector, matlabels);

        // The following line predicts the label of a given
        // test image:
        int predictedLabel = model.predict(testSample);
        //
        // To get the confidence of a prediction call the model with:
        //
        //      int predictedLabel = -1;
        //      double confidence = 0.0;
        //      model->predict(testSample, predictedLabel, confidence);
        //
        System.out.println(String.format("Predicted class = %d / Actual class = %d.", predictedLabel, testLabel));

//        // Here is how to get the eigenvalues of this Eigenfaces model:
//        Mat eigenvalues = model.getMat("eigenvalues");
//        // And we can do the same to display the Eigenvectors (read Eigenfaces):
//        Mat W = model.getMat("eigenvectors");
//        // Get the sample mean from the training data
//        Mat mean = model.getMat("mean");
//        // Display or save:
//        if(args.length == 2) {
//            Mat reshape = mean.reshape(1, matArray[0].rows());
//            imshow("mean", norm_0_255(reshape));
//        } else {
//            imwrite(format("%s/mean.png", output_folder.c_str()), norm_0_255(mean.reshape(1, images[0].rows)));
        }

    }




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


