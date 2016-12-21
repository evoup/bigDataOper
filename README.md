##  文字识别训练样本制造程序1.0 ##

# 开发环境 #
java spark hbase opencv

# 介绍 #
在机器学习课程中任课老师曾经布置过一个纸牌识别的作业，其中有一步是识别文字A2345678910JQK，为了提供更多训练数据，考虑采用造样本的方法。
而本次一级工程实践正好提供了这样一个把各种大数据技术练手的项目，所以就有了这个项目。整个项目分为爬虫爬字体存hdfs,spark stream监控hdfs
目录进行处理，调用javacv生成基于文字的图片，对图片适当变形，这样就有了新训练数据图片，最后将训练数据图片存到hbase。

启动命令第一版：
sudo -u spark spark-submit --master yarn --deploy-mode client --executor-memory 1g /services/apps/sparkApps/bigDataOper-0.0.1-SNAPSHOT.jar --name wordCount --class=org.beihang.bigData.WordCount02