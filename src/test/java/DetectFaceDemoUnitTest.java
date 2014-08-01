import org.junit.Test;
import org.opencv.core.Core;

/**
 * Created by piers on 01/08/14.
 */
public class DetectFaceDemoUnitTest {
    @Test
    public void testName() throws Exception {
        // Load the native library.
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new DetectFaceDemo().run();
    }
}
