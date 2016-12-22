package org.beihang.bigData;

import org.beihang.bigData.domain.Pic;

/**
 * Created by evoup on 16-12-11.
 */
public interface CharImageProcess {
    Pic getTextFromSpiderImage(final String receiptImageFilePath, String character);

    String generateSampleImage();
}
