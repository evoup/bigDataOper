package org.beihang.bigData;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordCount02 {
    private static final Log LOG = LogFactory.getLog(WordCount02.class);

    public static void main(String[] args) {

        getCharacters();


        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("wordCountSparkStream")
                .set("spark.testing.memory", "2147480000");
        JavaStreamingContext jssc=new JavaStreamingContext(conf,Durations.seconds(10));
        LOG.info("[创建javaStreamingContext成功：" + jssc + "]");
        CharImageProcess proc = new CharImageProcessImpl();
        proc.getTextFromSpiderImage("/project/full/", "c"); // 参数是爬虫下载下来的文件的存放路径
        LOG.info("[read hdfs font ok]");
        HbaseProcess hproc = new HbaseProcess();
        try {
            hproc.saveImg("12");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        JavaReceiverInputDStream<String> lines=jssc.socketTextStream("datanode01", 9999);


        JavaDStream<String> words=lines.flatMap(new FlatMapFunction<String,String>(){
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }});


        JavaPairDStream<String, Integer> pairs=words.mapToPair(new PairFunction<String,String,Integer>(){
            public Tuple2<String, Integer> call(String arg0) throws Exception {
                return new Tuple2<String,Integer>(arg0,1);
            }});


        JavaPairDStream<String,Integer> wordCounts=pairs.reduceByKey(new Function2<Integer,Integer,Integer>(){
            public Integer call(Integer arg0, Integer arg1) throws Exception {
                return arg0+arg1;
            }});


        wordCounts.print();


        wordCounts.dstream().saveAsTextFiles("hdfs://namenode:8020/sparkStream001/wordCount/", "spark");

        jssc.start();//开始计算
        jssc.awaitTermination();//等待计算结束


    }

    private static List<String> getCharacters() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String[] arr = str.split("");
        LOG.info("[---test---:" + new Gson().toJson(arr) + "]");
        return Arrays.asList(arr);
    }

}