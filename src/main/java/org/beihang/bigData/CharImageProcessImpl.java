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

    private static final Log LOG = LogFactory.getLog(CharImageProcessImpl.class);

    @Override
    public String getTextFromSpiderImage(String receiptImageFilePath, String s) {
        Fonts fonts = new Fonts();
        LOG.info("[new Fonts]");
        Font font = fonts.getFont(receiptImageFilePath).get(0).getFont(); // TODO 目前只要每种字体的第一个版本
        Font sizedFont = font.deriveFont(200f);
        AffineTransform stretch = new AffineTransform();
        int w = 200; // image width
        int h = 200; // image height
        int f = 100; // Font size in px
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
        drawCenteredString(g,s,w,h,sizedFont);
        g.dispose();
        String base64ImgContent;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream b64 = new Base64.OutputStream(os);
            ImageIO.write(bi, "png", b64);
            base64ImgContent = os.toString("UTF-8");
            System.out.println("[result:" + base64ImgContent + "]");
            return base64ImgContent;
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String generateSampleImage() {
        return null;
    }


    private void drawCenteredString(Graphics2D g2d, String text, int width, int height, Font font) {
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
