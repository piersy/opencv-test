import org.junit.Test;

/**
 * Created by piers on 07/08/14.
 */
public class FaceRecognitionTest {

    @Test
    public void test(){
        FaceRecognition faceRecognition = new FaceRecognition();
        faceRecognition.learn("all10.txt");
        faceRecognition.recognizeFileList("lower3.txt");
    }
}
