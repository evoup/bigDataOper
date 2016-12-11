package org.beihang.bigData;

/**
 * Created by evoup on 16-12-11.
 * A cache of the dynamically loaded fonts found in the fonts directory.
 */


import java.awt.Font;
        import java.io.InputStream;
        import java.util.Map;
        import java.util.concurrent.ConcurrentHashMap;

public class Fonts {

    // Prepare a static "cache" mapping font names to Font objects.
    private static String[] names = { "A.ttf" };

    private static Map<String, Font> cache = new ConcurrentHashMap<>(names.length);
    static {
        for (String name : names) {
            cache.put(name, getFont(name));
        }
    }

    public static Font getFont(String name) {
        Font font = null;
        if (cache != null) {
            if ((font = cache.get(name)) != null) {
                return font;
            }
        }
        String fName = "/fonts/" + name;
        try {
            InputStream is = Fonts.class.getResourceAsStream(fName);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(fName + " not loaded.  Using serif font.");
            font = new Font("serif", Font.PLAIN, 24);
        }
        return font;
    }
}
