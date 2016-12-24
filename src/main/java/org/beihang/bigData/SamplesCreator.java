package org.beihang.bigData;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.beihang.bigData.domain.FontModel;
import org.beihang.bigData.domain.Pic;
import scala.Tuple2;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SamplesCreator {
    private static final Log LOG = LogFactory.getLog(SamplesCreator.class);

    public static void main(String[] args) {


        final SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("samplesCreatorSparkStream")
                .set("spark.testing.memory", "2147480000");
        JavaStreamingContext jssc=new JavaStreamingContext(conf,Durations.seconds(10));
        LOG.info("[创建javaStreamingContext成功：" + jssc + "]");
        JavaReceiverInputDStream<String> lines=jssc.socketTextStream("datanode01", 9999);


        JavaDStream<String> words=lines.flatMap(new FlatMapFunction<String,String>(){
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }});


        JavaPairDStream<String, Integer> pairs=words.mapToPair(new PairFunction<String,String,Integer>(){
            public Tuple2<String, Integer> call(String arg0) throws Exception {
                // arg0是下载的文件名（注：没有路径名的）
                ///////////////////////
                CharImageProcess proc = new CharImageProcessImpl();
                List<String> willRemoveHdfsURIs = new ArrayList<>();
                List<String> proceedFontNames = new ArrayList<>();
                for (String charactor : getCharacters()) {
                    if (StringUtils.isEmpty(charactor)) {
                        continue; // 第一个会出现一个空格要去掉
                    }
                    Fonts fonts = new Fonts();
                    String receiptImageFilePath = "/tmp/downloadFiles/" + arg0;
                    LOG.info("[receiptImageFilePath:" + receiptImageFilePath + "]");
                    List<FontModel> fontModels = fonts.getFont(receiptImageFilePath);
                    for (FontModel fontModel : fontModels) {
                        Font font = fontModel.getFont();
                        String fontName = fontModel.getName();
                        if (!proceedFontNames.contains(fontName)) proceedFontNames.add(fontName);
                        Pic pic = proc.getTextFromSpiderImage(font, fontName, charactor); // 参数是爬虫下载下来的文件的存放路径
                        LOG.info("[read hdfs font ok]");
                        HbaseProcess hproc = new HbaseProcess();
                        try {
                            hproc.saveImg(pic, charactor);
                            if (!willRemoveHdfsURIs.contains(fontModel.getHdfsPath()))
                                willRemoveHdfsURIs.add(fontModel.getHdfsPath());
                        } catch (IOException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
                // delete download file from hdfs
                LOG.info("[will delete these premitive files:" + new Gson().toJson(willRemoveHdfsURIs) + "]");
                for (String hdfsOldFileURI : willRemoveHdfsURIs) {
                    try {
                        deleteOldFile(hdfsOldFileURI);
                    } catch (URISyntaxException|IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
                ///////////////////////
                String retKey = StringUtils.join(proceedFontNames, "|");
                //return new Tuple2<String,Integer>(arg0,1);
                return new Tuple2<String,Integer>(retKey, 1);
            }});


        JavaPairDStream<String,Integer> wordCounts=pairs.reduceByKey(new Function2<Integer,Integer,Integer>(){
            public Integer call(Integer arg0, Integer arg1) throws Exception {
                return arg0+arg1;
            }});


        wordCounts.print();


        wordCounts.dstream().saveAsTextFiles("hdfs://namenode:8020/sparkStream001/samplesCreator/", "spark");

        jssc.start();//开始计算
        jssc.awaitTermination();//等待计算结束


    }

    private static List<String> getCharacters() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String[] arr = str.split("");
        LOG.info("[read sample characters:" + new Gson().toJson(arr) + "]");
        return Arrays.asList(arr);
    }

    private static  void deleteOldFile(String fName) throws URISyntaxException, IOException {
        Configuration configuration = new Configuration();
        URI uri = new URI(fName);
        Path path = new Path(uri);
        FileSystem hdfs = FileSystem.get(URI.create("hdfs://namenode:8020"), configuration);
        LOG.info("[delete work download file:" + fName + "]");
        hdfs.delete(path, true);
    }

}