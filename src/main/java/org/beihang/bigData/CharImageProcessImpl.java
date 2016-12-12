package org.beihang.bigData;


import net.iharder.base64.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
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
        String text = "1";
        Fonts fonts = new Fonts();
        LOG.info("[new Fonts]");
        Font font = fonts.getFont(receiptImageFilePath);
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        try {
            LOG.info("[start write png]");
            //ImageIO.write(img, "png", new File("/sparkStream001/Text.png"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream b64 = new Base64.OutputStream(os);
            ImageIO.write(img, "png", b64);
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
