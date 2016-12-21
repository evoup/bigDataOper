package org.beihang.bigData;

/**
 * Created by evoup on 16-12-11.
 * A cache of the dynamically loaded fonts found in the fonts directory.
 */


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts {

    public List<Font> getFont(String fName) {
        Font font;
        List<Font> fonts = new ArrayList<>();
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
                    font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                    fonts.add(font);
                }
            }
            //InputStream inputStream = hdfs.open(path);
            //font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            hdfs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(fName + " not loaded.  Using serif font.");
            //font = new Font("serif", Font.PLAIN, 24);
            //fonts.add(font);
        }
        return fonts;
    }
}
