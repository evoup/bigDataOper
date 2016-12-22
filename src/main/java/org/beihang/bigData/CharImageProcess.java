package org.beihang.bigData;

/**
 * Created by evoup on 16-12-11.
 */
public interface CharImageProcess {
    String getTextFromSpiderImage(final String receiptImageFilePath, String character);

    String generateSampleImage();
}
