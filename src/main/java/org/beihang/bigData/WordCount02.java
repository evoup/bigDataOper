package org.beihang.bigData;
import java.util.Arrays;
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
public class WordCount02 {
    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("wordCountSparkStream")
                .set("spark.testing.memory", "2147480000");
        JavaStreamingContext jssc=new JavaStreamingContext(conf,Durations.seconds(10));
        System.out.println("创建javaStreamingContext成功："+jssc);
        Fonts fonts = new Fonts();
        fonts.getFont("hdfs://namenode:8020/project/2");
        System.out.println("read hdfs font ok");

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

}