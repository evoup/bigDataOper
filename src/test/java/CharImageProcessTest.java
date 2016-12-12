import junit.framework.TestCase;
import org.beihang.bigData.CharImageProcess;
import org.beihang.bigData.CharImageProcessImpl;
import org.beihang.bigData.Fonts;

/**
 * Created by evoup on 16-12-11.
 */
public class CharImageProcessTest extends TestCase {

    public void testGenerateSampleImage() {
        CharImageProcess cp = new CharImageProcessImpl("");
        cp.generateSampleImage();
        assertTrue(true);
    }

    public void testGetFont() {
        Fonts fonts = new Fonts();
        fonts.getFont("/project/1.ttf");
        assertTrue(true);
    }

}
