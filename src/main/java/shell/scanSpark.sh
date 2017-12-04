#!/usr/bin/env bash



FWDIR="$(cd `dirname $0`/..; pwd)"


export HADOOP_USER_NAME=hdfs



#`spark-submit --class bigdata.analysis.java.ScanMain  --master spark://centos14:7077 --executor-memory 512m --total-executor-cores 1 $FWDIR/lib/stream-statistics-1.0-SNAPSHOT-jar-with-dependencies.jar`
`spark-submit --class bigdata.analysis.java.ScanMain  --master yarn-cluster --driver-memory 2g  --driver-cores 2 --executor-memory 1g   --executor-cores 2 --num-executors 3 --queue default $FWDIR/lib/stream-statistics-1.0-SNAPSHOT-jar-with-dependencies.jar &`
#`spark-submit --class bigdata.analysis.java.ScanMain  --master yarn-client --driver-memory 1g --executor-memory 1g   --executor-cores 2 --num-executors 2  --queue default  $FWDIR/lib/stream-statistics-1.0-SNAPSHOT-jar-with-dependencies.jar`

logDir=$FWDIR/log/scanlog

n_date=`date  +"%Y%m%d %H:%M:%S"`
  echo "$n_date  +spark 程序启动" >> $logDir
