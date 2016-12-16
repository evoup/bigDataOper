package org.beihang.bigData;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
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
        hBaseConfig.set("hbase.zookeeper.quorum","zoo3");
        hBaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        HTable table = new HTable(hBaseConfig, "ocr_table");
        Put p = new Put(Bytes.toBytes(fontName));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("type"),
                Bytes.toBytes("png"));
        table.put(p);
    }


}