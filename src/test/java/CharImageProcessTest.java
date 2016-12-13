import junit.framework.TestCase;
import net.iharder.base64.Base64;
import org.beihang.bigData.CharImageProcess;
import org.beihang.bigData.CharImageProcessImpl;
import org.beihang.bigData.Fonts;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by evoup on 16-12-11.
 * 测试代码
 */
public class CharImageProcessTest extends TestCase {

    public void testGenerateSampleImage() {
        CharImageProcess cp = new CharImageProcessImpl("");
        cp.generateSampleImage();
        assertTrue(true);
    }

    public void testGetFont() {
        Fonts fonts = new Fonts();
        Font font = fonts.getFont("/project/Pacifico.ttf");
        assertTrue(true);
    }

    public void testCreateFont() {
        Font font = null;
        //String fontName = "ostrich-regular.ttf";
        //String fontName = "duma.ttf";
        //String fontName = "Pacifico.ttf";
        //String fontName = "Ubuntu-C.ttf";
        //String fontName = "Palo_Alto_Regular.ttf";
        //String fontName = "whitrabt.ttf";
        String fontName = "peace_sans.otf";
        InputStream is = this.getClass().getResourceAsStream(fontName);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font sizedFont = font.deriveFont(200f);
            AffineTransform stretch = new AffineTransform();
            int w = 200; // image width
            int h = 200; // image height
            int f = 100; // Font size in px
            //String s = "The quick brown fox jumps over the lazy dog.";
            String s = "z";
            final BufferedImage bi = new BufferedImage(
                    w,h,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            g.setFont(sizedFont);
            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // paint BG
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            g.setColor(Color.BLACK);

            //for (int i=0; (i*f)+f<=h; i++) {
            //    g.drawString(s, 0, (i*f)+f);
                // stretch
/*                stretch.concatenate(
                        AffineTransform.getScaleInstance(1.18, 1d));
                g.setTransform(stretch);*/

                // fade
/*                Color c = g.getColor();
                g.setColor(new Color (
                        c.getRed(),
                        c.getGreen(),
                        c.getBlue(),
                        (int)(c.getAlpha()*.75)));*/
            //}
            //g.drawString(s, 0, f);
            drawCenteredString(g,s,w,h,sizedFont);
            //printSimpleString(g,s,w,h,0,f);
            g.dispose();

            try {
                //ImageIO.write(img, "png", new File("/sparkStream001/Text.png"));
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                OutputStream b64 = new Base64.OutputStream(os);
                ImageIO.write(bi, "png", b64);
                String result = os.toString("UTF-8");
                System.out.println(result);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void printSimpleString(Graphics2D g2d, String s, int width, int height, int XPos, int YPos){
        int stringLen = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int stringLen2 = (int)g2d.getFontMetrics().getStringBounds(s, g2d).getHeight();
        int start = width/2 - stringLen/2;
        int start2 = height/2 - stringLen2/2;
        g2d.drawString(s, start + XPos, 0);
        }

    public void drawCenteredString(Graphics2D g2d, String text, int width, int height, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g2d.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g2d.setFont(font);
        // Draw the String
        g2d.drawString(text, x, y);
        // Dispose the Graphics
        g2d.dispose();
    }
}
