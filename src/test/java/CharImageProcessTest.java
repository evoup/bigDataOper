import junit.framework.TestCase;
import org.beihang.bigData.CharImageProcess;
import org.beihang.bigData.CharImageProcessImpl;

/**
 * Created by evoup on 16-12-11.
 */
public class CharImageProcessTest extends TestCase {

    public void testGenerateSampleImage() {
        CharImageProcess cp = new CharImageProcessImpl("");
        cp.generateSampleImage();
        assertTrue( true );
    }

}
