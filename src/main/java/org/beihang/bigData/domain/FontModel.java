package org.beihang.bigData.domain;

import java.awt.*;

/**
 * Created by evoup on 2016/12/22.
 */
public class FontModel {
    String name;
    Font font;
    String hdfsPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }
}
