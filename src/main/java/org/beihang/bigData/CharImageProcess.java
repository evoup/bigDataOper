package org.beihang.bigData;

import org.beihang.bigData.domain.PicModel;

/**
 * Created by evoup on 16-12-11.
 */
public interface CharImageProcess {
    PicModel getTextFromSpiderImage(final String receiptImageFilePath, String character);

    String generateSampleImage();
}
