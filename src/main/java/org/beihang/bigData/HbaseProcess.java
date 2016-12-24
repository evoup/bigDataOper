package org.beihang.bigData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.beihang.bigData.domain.Pic;

import java.io.IOException;

/**
 * Created by evoup on 16-12-16.
 */
public class HbaseProcess {

    private static final Log LOG = LogFactory.getLog(HbaseProcess.class);

    public void saveImg(Pic pic, String charactor) throws IOException {
        LOG.info("[saveImg][hbase conf]");
        Configuration hBaseConfig =  HBaseConfiguration.create();
        hBaseConfig.setInt("timeout", 120000);
        hBaseConfig.set("hbase.master", "zoo3:60010");
        hBaseConfig.set("hbase.zookeeper.quorum", "zoo3");
        hBaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        LOG.info("[saveImg][hbase ok]");
        HTable table = new HTable(hBaseConfig, "ocr_table");
        LOG.info("[saveImg][HTable new]");
        // 字体名字空格用下划线替换，全部转换成小写后当rowkey
        Put p = new Put(Bytes.toBytes(pic.getFontName().replaceAll(" ", "_").toLowerCase()));
        LOG.info("[saveImg][Put p]");
        p.add(Bytes.toBytes("info"), Bytes.toBytes("font"), Bytes.toBytes(pic.getFontName()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("type"), Bytes.toBytes("png"));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("content_" + charactor), Bytes.toBytes(pic.getBase64Content())); // info:content_0 info:content_a ...
        LOG.info("[saveImg][Add p]");
        table.put(p);
        LOG.info("[saveImg][Put table]");
        Get g = new Get(Bytes.toBytes("rowkey1"));
        LOG.info("[saveImg][Get g]");
        Result r = table.get(g);
        LOG.info("[saveImg][Result r]");
        byte[] value = r.getValue(Bytes.toBytes("info"), Bytes
                .toBytes("type"));
        String valueStr = Bytes.toString(value);
        System.out.println("[GET: " + valueStr + "]");
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes("info"), Bytes
                .toBytes("type"));
        LOG.info("[saveImg][Scan s]");
        try (ResultScanner scanner = table.getScanner(s)) {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println("Found row: " + rr);
                LOG.info("[saveImg][Found row: " + rr + "]");
            }
        } catch (Exception e) {
            LOG.error("[saveImg][error:" + e.getMessage() + "]");
            throw e;
        }
        System.exit(0);
    }


}
