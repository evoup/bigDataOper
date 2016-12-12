package org.beihang.bigData;


import net.iharder.base64.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by evoup on 16-12-11.
 */
public class CharImageProcessImpl implements CharImageProcess {

    String timeStampDefined = null;

    private static final Log LOG = LogFactory.getLog(CharImageProcessImpl.class);

    public CharImageProcessImpl(String timeStampDefined) {
        this.timeStampDefined = timeStampDefined;
    }

    @Override
    public String getTextFromSpiderImage(String receiptImageFilePath) {
        String text = "hello world";
        Fonts fonts = new Fonts();
        LOG.info("[new Fonts]");
        Font font = fonts.getFont(receiptImageFilePath);
        //BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        //g2d.setFont(font);


        AffineTransform stretch = new AffineTransform();
        int w = 640; // image width
        int h = 200; // image height
        int f = 21; // Font size in px
        String s = "The quick brown fox jumps over the lazy dog.";

        final BufferedImage bi = new BufferedImage(
                w,h,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font("Serif",Font.PLAIN,f));
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // paint BG
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);

        for (int i=0; (i*f)+f<=h; i++) {
            g.drawString(s, 0, (i*f)+f);
            // stretch
            stretch.concatenate(
                    AffineTransform.getScaleInstance(1.18, 1d));
            g.setTransform(stretch);

            // fade
            Color c = g.getColor();
            g.setColor(new Color (
                    c.getRed(),
                    c.getGreen(),
                    c.getBlue(),
                    (int)(c.getAlpha()*.75)));
        }

        g.dispose();





/*        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();*/
/*        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);*/
/*        g2d.setFont(font);
        g2d.translate(100,100);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();*/
        try {
            LOG.info("[start write png]");
            //ImageIO.write(img, "png", new File("/sparkStream001/Text.png"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream b64 = new Base64.OutputStream(os);
            ImageIO.write(bi, "png", b64);
            String result = os.toString("UTF-8");
            LOG.info("[result:" + result + "]");
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String generateSampleImage() {
        return null;
    }

}
