package javasssss;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class SparkHBase {

	public static void main(String[] args) throws Exception {
		SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("spark://centos.host1:7077");  
        sparkConf.setAppName("Spark HBase");  
        JavaSparkContext context = new JavaSparkContext(sparkConf);  
  
        Configuration conf = HBaseConfiguration.create();  
        conf.set("hbase.zookeeper.property.clientPort", "2181");  
        conf.set("hbase.zookeeper.quorum", "centos.host1");  
        conf.set("hbase.master", "centos.host1:60000");  
        
        //Put操作
        HTable table = new HTable(conf, "user");  
        Put put = new Put(Bytes.toBytes("row6"));  
        put.add(Bytes.toBytes("basic"), Bytes.toBytes("name"), Bytes.toBytes("value6"));  
        table.put(put);  
        table.flushCommits();  
        
        //Delete操作
        Delete delete = new Delete(Bytes.toBytes("row1"));
        table.delete(delete);
        
        table.close();  
        
        //Scan操作
        Scan scan = new Scan();  
        scan.setStartRow(Bytes.toBytes("0120140722"));  
        scan.setStopRow(Bytes.toBytes("1620140728"));  
        scan.addFamily(Bytes.toBytes("basic"));  
        scan.addColumn(Bytes.toBytes("basic"), Bytes.toBytes("name"));  
  
        String tableName = "user";  
        conf.set(TableInputFormat.INPUT_TABLE, tableName);  
  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        DataOutputStream dos = new DataOutputStream(out);  
//        scan.write(dos);
        String scanStr = Base64.encodeBytes(out.toByteArray());  
        IOUtils.closeQuietly(dos);
        IOUtils.closeQuietly(out);  
        //高版本可以用如下方式：  
        //ClientProtos.Scan proto = ProtobufUtil.toScan(scan);  
        //String scanStr = Base64.encodeBytes(proto.toByteArray());  
        conf.set(TableInputFormat.SCAN, scanStr);  
  
        JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = context  
                .newAPIHadoopRDD(conf, TableInputFormat.class,  
                        ImmutableBytesWritable.class, Result.class);
            
//            hBaseRDD.saveAsHadoopDataset();
            
            Long count = hBaseRDD.count();
        System.out.println("count: " + count);  
  
        List<Tuple2<ImmutableBytesWritable, Result>> tuples = hBaseRDD  
                .take(count.intValue());  
        for (int i = 0, len = count.intValue(); i < len; i++) {  
            Result result = tuples.get(i)._2();  
            KeyValue[] kvs = result.raw();  
            for (KeyValue kv : kvs) {  
                System.out.println("rowkey:" + new String(kv.getRow()) + " cf:"  
                        + new String(kv.getFamily()) + " column:"  
                        + new String(kv.getQualifier()) + " value:"  
                        + new String(kv.getValue()));  
            }  
        }  
    }  
}