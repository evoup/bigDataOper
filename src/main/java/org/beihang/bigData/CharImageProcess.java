package org.beihang.bigData;

import org.beihang.bigData.domain.Pic;

import java.awt.*;

/**
 * Created by evoup on 16-12-11.
 */
public interface CharImageProcess {
    Pic getTextFromSpiderImage(Font font, String fontName, String character);

    String generateSampleImage();
}
