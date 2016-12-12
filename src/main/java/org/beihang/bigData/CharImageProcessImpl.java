package org.beihang.bigData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        Fonts fonts = new Fonts();
        LOG.info("[new Fonts]");
        fonts.getFont(receiptImageFilePath);
        return null;
    }

    @Override
    public String generateSampleImage() {
        return null;
    }

}
