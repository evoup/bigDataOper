package org.beihang.bigData;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by evoup on 16-12-16.
 */
public class HbaseProcess {

    public void saveImg(String fontName) throws IOException {
        Configuration hBaseConfig =  HBaseConfiguration.create();
        hBaseConfig.setInt("timeout", 120000);
        hBaseConfig.set("hbase.master", "zoo3:60010");
        hBaseConfig.set("hbase.zookeeper.quorum", "zoo3");
        hBaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
/*        HTable table = new HTable(hBaseConfig, "ocr_table");
        Put p = new Put(Bytes.toBytes(fontName));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("type"),
                Bytes.toBytes("png"));
        table.put(p);*/
        HTable table = new HTable(hBaseConfig, "ocr_table");
        Put p = new Put(Bytes.toBytes("myLittleRow"));
        p.add(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"),
                Bytes.toBytes("Some Value"));
        table.put(p);
        Get g = new Get(Bytes.toBytes("myLittleRow"));
        Result r = table.get(g);
        byte[] value = r.getValue(Bytes.toBytes("myLittleFamily"), Bytes
                .toBytes("someQualifier"));
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes("myLittleFamily"), Bytes
                .toBytes("someQualifier"));

        try (ResultScanner scanner = table.getScanner(s)) {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println("Found row: " + rr);
            }
        } catch (Exception e) {
            throw e;
        }
    }


}
