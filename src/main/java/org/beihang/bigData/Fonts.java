package org.beihang.bigData;

/**
 * Created by evoup on 16-12-11.
 * A cache of the dynamically loaded fonts found in the fonts directory.
 */


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.beihang.bigData.domain.FontModel;

import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts {

    public List<FontModel> getFont(String fName) {
        Font font;
        List<FontModel> fonts = new ArrayList<>();
        try {
            Configuration configuration = new Configuration();
            URI uri = new URI(fName);
            FileSystem hdfs = FileSystem.get(uri, configuration);
            Path path = new Path(uri);
            FileStatus[] status = hdfs.listStatus(path);  // you need to pass in your hdfs path
            for (FileStatus st : status) {
                String fname = st.getPath().toString();
                System.out.println("[found a download file:" + fname + "]");
                InputStream inputStream = hdfs.open(st.getPath());
                ZipInputStream zipStream = new ZipInputStream(inputStream);
                ZipEntry entry;
                while((entry = zipStream.getNextEntry())!=null) {
                    System.out.println("[zip file content:" + entry.getName() + "]");
                    try {
                        font = Font.createFont(Font.TRUETYPE_FONT, zipStream);
                        FontModel fontModel = new FontModel();
                        fontModel.setFont(font);
                        fontModel.setName(entry.getName());
                        fonts.add(fontModel);
                    } catch (Exception e) {
                        System.err.println(fName + " is not a font,so can`t create font");
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(fName + " not loaded.  Using serif font.");
        }
        return fonts;
    }
}
