package org.beihang.bigData;

/**
 * Created by evoup on 16-12-11.
 * A cache of the dynamically loaded fonts found in the fonts directory.
 */


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.awt.Font;
import java.io.InputStream;
import java.net.URI;

public class Fonts {

    public Font getFont(String fName) {
        Font font;
        try {
            Configuration configuration = new Configuration();
            URI uri = new URI(fName);
            FileSystem hdfs = FileSystem.get(uri, configuration);
            Path path = new Path(uri);
            InputStream inputStream = hdfs.open(path);
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(fName + " not loaded.  Using serif font.");
            font = new Font("serif", Font.PLAIN, 24);
        }
        return font;
    }
}
