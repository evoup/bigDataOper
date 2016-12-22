package org.beihang.bigData.domain;

/**
 * Created by evoup on 2016/12/22.
 */
public class Pic {
    String fontName;
    String base64Content;

    public Pic(String fontName, String base64Content) {
        this.fontName = fontName;
        this.base64Content = base64Content;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }
}
