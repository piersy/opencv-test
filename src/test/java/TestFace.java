import org.junit.Test;

/**
 * Created by piers on 07/08/14.
 */
public class TestFace {

    @Test
    public void test(){
        Eigenfaces.main(new String[]{"at.txt", "output"});
    }
}
