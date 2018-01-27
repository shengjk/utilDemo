package util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * Created by landscape.
 */
@SuppressWarnings("ALL")

/*
Bytes.toString( CellUtil.cloneValue( cell));
 */
public class HbaseTest{
	public static String table_name = "t_call_cdr";
	public static String cf="f";

	@Test
	public void create()throws Exception{
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "datanode1,datanode2,datanode3");
//		config.set("hbase.zookeeper.property.clientPort","2181");
//		config.set("hbase.master","10.16.30.54:600000");
//		HBaseAdmin.checkHBaseAvailable(config);

		Connection conn=ConnectionFactory.createConnection(config);

		Admin admin=conn.getAdmin();

//		admin.isTableEnabled(TableName.valueOf(table_name));
//		HBaseAdmin admin = new HBaseAdmin(config);
		System.out.println("11111111111");
		if(admin.tableExists(TableName.valueOf(table_name)) && admin.isTableEnabled(TableName.valueOf(table_name))){
			admin.disableTable(TableName.valueOf(table_name));
			admin.deleteTable(TableName.valueOf(table_name));
			System.out.println("22222222222222222222");
		}
		HTableDescriptor table =new HTableDescriptor(TableName.valueOf(table_name.getBytes()));
		System.out.println("33333333333333333333");
		HColumnDescriptor cf1= new HColumnDescriptor(cf.getBytes());
		
		System.out.println("44444444444444");
		cf1.setMaxVersions(1000);
		table.addFamily(cf1);

		System.out.println("4555555555555555555");
		admin.createTable(table);
		System.out.println("6666666666666666666");
		admin.close();
		conn.close();
	}
//
	@Test
	public void insert()throws Exception{
		long c = System.currentTimeMillis();
		//rowkey: phone_num_day
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "centos11,centos12,centos13");
		Connection conn=ConnectionFactory.createConnection(config);
//		Table table=conn.getTable(TableName.valueOf(table_name));
		Table table=conn.getTable(TableName.valueOf("test"));

//		HTable table =new HTable(config, table_name.getBytes());
		/*
		 table.setAutoFlush(false);

		 */

		for(int j=0;j<100;j++){
			String num =getPhone("139");
			String day =getDay();
//			List<Put> ps =new ArrayList<Put>();
			for(int i=0;i<10;i++){
				String rowkey =num+"_"+day;
				System.out.println(rowkey);
				Put put =new Put(rowkey.getBytes());
//				byte[] family, byte[] qualifier, byte[] value
				put.addColumn(cf.getBytes(), "myphone".getBytes(), num.getBytes());
				put.addColumn(cf.getBytes(), "type".getBytes(),getType().getBytes());
				put.addColumn(cf.getBytes(), "calltime".getBytes(), getCallTime(day).getBytes());
				put.addColumn(cf.getBytes(), "destphone".getBytes(), getPhone("139").getBytes());
//				ps.add(put);
				table.put(put);
			}
//				table.put(ps);
		}
		long b = System.currentTimeMillis();

		System.out.println("======== "+(b-c));
		table.close();
		conn.close();
	}


	@Test
	public void get()throws Exception{
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "centos11,centos12,centos13");
//		config.set("hbase.zookeeper.property.clientPort", "2181");
		Connection conn=ConnectionFactory.createConnection(config);
		Table table=conn.getTable(TableName.valueOf("test"));
//表名
		//byte[] row  rowkey
		Get get =new Get("20150928".getBytes());
//		get.addColumn(family, qualifier)
//		get.addFamily(family)
//		get.setFilter(new PrefixFilter("2000".getBytes()));
//		FilterList filterList=new FilterList();
		get.setMaxVersions();
		get.setMaxVersions(1);//will return last 3 versions of row
		Result res = table.get(get);
		

		System.out.println("==========================");

//		byte[] family, byte[] qualifier
		//列是严格区分大小写的
		List<Cell> cs = res.getColumnCells("f".getBytes(), "type".getBytes());
		for (Iterator iterator = cs.iterator(); iterator.hasNext();) {
			Cell cell = (Cell) iterator.next();
//			System.out.println("cell.getValueArray() "+new String(cell.getValueArray()));
			System.out.println("+++++++++++++++++++ ");
			System.out.println("cell.getValue() "+new String(cell.getValue()));

		}

		
		
		if(table!=null){

			table.close();
		}
		if(conn!=null){

			conn.close();
		}
	}

	@Test
	public void find()throws Exception {
		cf="f";
		table_name="kylin_metadata";
		System.getProperty("file.encoding");
		Configuration config = new Configuration();
		config.set("hbase.zookeeper.quorum", "cdh02,cdh03,cdh04");
		HTable table = new HTable(config, table_name.getBytes());
		Scan scan = new Scan();

//		RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
//		new RegexStringComparator("20150928"));
//		scan.setFilter(filter);
		
		
//		Filter
		
//		scan.setMaxVersions(10);
//		String startrowkey = "13948179148_" + "20150801";
//		String endowkey = "13948179148_" + "20150831";
////		byte[] startRow, byte[] stopRow
//		scan.setStartRow(startrowkey.getBytes());
//		scan.setStopRow(endowkey.getBytes());
//		scan.setBatch(100);
		scan.setFilter(new  PageFilter(1));
		ResultScanner rs = table.getScanner(scan);
//		for (Result res : rs) {
//			List<Cell> cs = res.getColumnCells(cf.getBytes(), "M".getBytes());
//			for (Iterator iterator = cs.iterator(); iterator.hasNext(); ) {
//				Cell cell = (Cell) iterator.next();
//				System.out.println(new String(cell.getValue()));
//			}
//		}
		for (Result res : rs) {
			printRecoder(res);
		}
		table.close();
		System.out.println(System.getProperty("file.encoding"));
	}
	
	
	
	public  static void printRecoder(Result result)throws Exception{
		for(Cell cell:result.rawCells()){
			System.out.print("行健: "+new String(CellUtil.cloneRow(cell)));
			System.out.print("列簇: "+new String(CellUtil.cloneFamily(cell)));
			System.out.print(" 列: "+new String(CellUtil.cloneQualifier(cell)));
			System.out.print(" 值: "+new String(CellUtil.cloneValue(cell)));
			System.out.println("时间戳: "+cell.getTimestamp());
		}
	}
	
	
	
	/*
	* Configuration conf = HBaseConfiguration. create ();

        HTable table =  new  HTable(conf,  "rd_ns:leetable" );

        Get get =  new  Get(Bytes. toBytes ( "100003" ));

        Result r = table.get(get);

         for  (Cell cell : r.rawCells()) {

            System. out .println(

                     "Rowkey : " +Bytes. toString (r.getRow())+

                     "   Familiy:Quilifier : " +Bytes. toString (CellUtil. cloneQualifier (cell))+

                     "   Value : " +Bytes. toString (CellUtil. cloneValue (cell))

                    );

        }
	* */
	
	
//	}
//
	public String getPhone(String prefix){
		return  new Formatter().format(prefix+"%08d", new Random().nextInt(100000000)).toString();

	}

	public String getDay(){
		return   new Formatter().format("2015%02d%02d", new Random().nextInt(12)+1,new Random().nextInt(31)+1).toString();
	}


	public String getCallTime(String prefix){
		return  new Formatter().format(prefix+"%02d%02d%02d", new Random().nextInt(25),new Random().nextInt(60),new Random().nextInt(60)).toString();
	}



	public String getType(){
		return  new Formatter().format("%1d", new Random().nextInt(3)).toString();

	}

	@Test
	public void  scanfilter() throws Exception {
		Configuration conf =HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "centos2");
		Connection conn =ConnectionFactory.createConnection(conf);
		Table table=conn.getTable(TableName.valueOf("test"));

		Scan scan =new Scan();
		scan.setFilter(new PrefixFilter("id".getBytes()));

		ResultScanner results=table.getScanner(scan);
		int count=0;
		for (Result res:results) {
			for( Cell ce:res.rawCells()){
				count++;
//				System.out.println(new String(ce.getValue()));
				System.out.println(Bytes.toString(ce.getFamilyArray()));
				System.out.println(count+" ==========");
			}
		}


	}


	@Test
	public void insert1()throws Exception{
		long c = System.currentTimeMillis();
		//rowkey: phone_num_day
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "192.168.2.27,192.168.2.26,192.168.2.28");
		Connection conn=ConnectionFactory.createConnection(config);
		String table_name="peng_yuan";
		Table table=conn.getTable(TableName.valueOf("msxf_datalogic:"+table_name));

		String rowkey="c4930c4d32e9499386c2ac974fc7705352d639e2b4634387a277674ac14710bf1";
		String qualifier="peng_yuan_balck";

		List<String> listStr=Files.readAllLines(new File("C:\\Users\\WUJUN\\Desktop\\文件迁移\\样例数据.txt").toPath());

		StringBuilder sb =new StringBuilder();
		for (String str:listStr) {
			sb.append(str+System.getProperty("line.separator"));
			System.out.println(str);
		}
		Put put =new Put(rowkey.getBytes());
		put.addColumn("content".getBytes(), qualifier.getBytes(), sb.toString().getBytes());


		table.put(put);

		long b = System.currentTimeMillis();

		System.out.println("======== "+(b-c));
		table.close();
		conn.close();
	}

	//过滤器,通过过滤器可以很精确的定为到某一个cell
	@Test
	public void scan()throws Exception{
		long c = System.currentTimeMillis();
		//rowkey: phone_num_day
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "192.168.2.27,192.168.2.26,192.168.2.28");
		Connection conn=ConnectionFactory.createConnection(config);
		String table_name="msxf_blaze:pos_hard_soft_triggers";
		Table table=conn.getTable(TableName.valueOf(table_name));

		Scan scan =new Scan();
		//过滤
		Filter filter=new FamilyFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("main")));
//		new QualifierFilter()
//		new TimestampsFilter()
		scan.setStartRow("20000014".getBytes());
		scan.setStopRow("c".getBytes());
		scan.setFilter(filter);
		ResultScanner rs =table.getScanner(scan);
		System.out.println(rs);

		/*
		 @@Description: SingleColumnValueFilter用于对指定列的数据值进行比较，将匹配上的值表进行返回。如，如果字段包含
     *                ”zhang“，则该列返回。
     * @param table

	 SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("INFO"), Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL, new SubstringComparator("zhang"));
        filter.setFilterIfMissing(true);
		 */


		table.close();
		conn.close();


	}

	/**
	 *
	 * @@Description: FilterList 过滤器列表，用于对数据进行组合过滤查询,可实现多个过滤条件的OR和AND关系
	 * @param table
	 * @throws IOException
	 */
	public static void scanMultColumnValueFilter(Table table) throws IOException {
		Scan scan = new Scan();
		// 查找创建时间为2010-10-01的记录
		Filter filter1 = new SingleColumnValueFilter(Bytes.toBytes("main"), Bytes.toBytes("parentdept"),
				CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("总行")));
		Filter filter2 = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("createdate"),
				CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("2011-10-01")));

		// MUST_PASS_ONE 表示只要任一条件通过则返回 相关于OR关系
		// MUST_PASS_ALL 表示必须所有条件都满足才返回 相当于AND关系
		FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		list.addFilter(filter1);
		list.addFilter(filter2);
		scan.setFilter(list);

		ResultScanner scanner = table.getScanner(scan);


	}

//
//
//
//	private final static int COLUMN_MAX_VERSION = 1;
//	private static Connection conn = null;
//	private static final Object GET_CONN_LOCK = new Object();
//	private static HBaseAdmin hbaseAdmin = null;
//	private static final Object GET_HBASE_ADMIN_LOCK = new Object();
//	private static Configuration conf = null;
//
//	public static Configuration getConf() {
//		if (conf == null) {
//			synchronized (HbaseTest.class) {
//				if (conf == null) {
//					conf = HBaseConfiguration.create();
//				}
//			}
//		}
//
//		return conf;
//	}
//
//	public static Connection getConn() {
//		if (conn == null) {
//			synchronized (GET_CONN_LOCK) {
//				if (conn == null) {
//					try {
//						conn = ConnectionFactory.createConnection(getConf());
//					} catch (Exception e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}
//		}
//
//		return conn;
//	}
//
//	public static HBaseAdmin getHBaseAdmin() {
//		if (hbaseAdmin == null) {
//			synchronized (GET_HBASE_ADMIN_LOCK) {
//				if (hbaseAdmin == null) {
//					try {
//						hbaseAdmin = new HBaseAdmin(getConf());
//					} catch (Exception e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}
//		}
//
//		return hbaseAdmin;
//	}
//
//	public static HTableInterface getHTable(String tableName) {
//		HTableInterface t = null;
//		try {
//			t = getConn().getTable(tableName);
//		} catch (IOException e) {
//			try {
//				t = getConn().getTable(tableName);
//			} catch (IOException e1) {
//				throw new RuntimeException("getHTable error, tableName:"+tableName, e1);
//			}
//		}
//
//		return t;
//	}

	/*
	hbase 0.94.0版本中，对于region的split方式引入了一个非常方便的SplitPolicy，通过这个SplitPolicy，可以主动的干预控制region split的方式。在org.apache.hadoop.hbase.regionserver包中，可以找到这么几个自带的splitPolicy： ConstantSizeRegionSplitPolicy, IncreasingToUpperBoundRegionSplitPolicy, and KeyPrefixRegionSplitPolicy。

从名字上就可以分辨出这三种split策略的适用场景：
ConstantSizeRegionSplitPolicy：按固定长度分割region，固定长度取值优先获取table的”MAX_FILESIZE” 值，若没有设定该属性，则采用在hbase-site.xml中配置的hbase.hregion.max.filesize值，在0.94版本中这个值的缺省值已经被调整为：10 * 1024 * 1024 * 1024L 也就是10G，网上很多关于 hbase.hregion.max.filesize 默认值 1G的文章应该都是基于0.92的hbase的。这个在使用中需要明确具体的hbase版本号。这个策略是0.94版本之前默认使用的，采用该策略后，当table的某一region中的某一store大小超过了预定的最大固定长度时，对该region进行split。splitPoint算法的选择还是依据“数据对半”原则，找到该region的最大store的中间长度的rowkey进行split。



IncreasingToUpperBoundRegionSplitPolicy：按照region数量累增划分region，该策略为Hbase 0.94默认使用的策略，采用该策略分割的region大小是不相等的，每次新region的大小随着region数量的增多而增大。具体增长方法为：Min (R^2 *  ”MEMSTORE_FLUSHSIZE”||”hbase.hregion.memstore.flush.size”, “hbase.hregion.max.filesize”)；其中R 为当前这个region所在regionserver中对应此table的region数，MEMSTORE_FLUSHSIZE 为table创建时指定大小，若table指定了此属性则忽略下面的hbase.hregion.memstore.flush.size 。

hbase.hregion.memstore.flush.size 为hbase-site中设定大小 默认128M

hbase.hregion.max.filesize 为hbase-site中设定的单个region大小，默认10G

每次region大小是取上述两个size中较小的那个。

假设使用hbase.hregion.memstore.flush.size 128M, hregion.max.filesize为10G， 那么每次region增长情况为：512M,1152M,2G,3,2G,4,6G,6,2G,etc。当region增长到9个时，9*9*128M/1024=10.125G >10G,至此以后region split大小都固定为10G。



KeyPrefixRegionSplitPolicy:指定rowkey前缀位数划分region，通过读取table的prefix_split_key_policy.prefix_length属性，该属性为数字类型，表示前缀长度，

在进行split时，按此长度对splitPoint进行截取。个人理解是rowkey前缀不相等，则划分region。此种策略比较适合固定前缀的rowkey。当table中没有设置prefix_split_key_policy.prefix_length属性，或prefix_split_key_policy.prefix_length属性不为Integer类型时，指定此策略效果等同与使用IncreasingToUpperBoundRegionSplitPolicy。



	   // 更新现有表的split策略
            HBaseAdmin admin = new HBaseAdmin( conf);
            HTable hTable = new HTable( conf, ”test” );
            HTableDescriptor htd = hTable.getTableDescriptor();
            HTableDescriptor newHtd = new HTableDescriptor(htd);
             newHtd.setValue(HTableDescriptor. SPLIT_POLICY, KeyPrefixRegionSplitPolicy.class .getName());// 指定策略
             newHtd.setValue(“prefix_split_key_policy.prefix_length”, ”2″);
             newHtd.setValue(“MEMSTORE_FLUSHSIZE”, ”5242880″); // 5M
            admin.disableTable( ”test”);
            admin.modifyTable(Bytes. toBytes(“test”), newHtd);
            admin.enableTable( ”test”);
	 */
	
	
	/*
	
	　　　 HTableDescriptor tableDesc = new HTableDescriptor("test");
  //日志flush的时候是同步写，还是异步写
  tableDesc.setDurability(Durability.SYNC_WAL);
  //MemStore大小
  tableDesc.setMemStoreFlushSize(256*1024*1024);
  
  HColumnDescriptor re = new HColumnDescriptor("f");
  //块缓存，保存着每个HFile数据块的startKey
  colDesc.setBlockCacheEnabled(true);
  //块的大小，默认值是65536
  //加载到内存当中的数据块越小，随机查找性能更好,越大，连续读性能更好
  colDesc.setBlocksize(64*1024);
  //bloom过滤器，有ROW和ROWCOL，ROWCOL除了过滤ROW还要过滤列族
  colDesc.setBloomFilterType(BloomType.ROW);
  //写的时候缓存bloom
  colDesc.setCacheBloomsOnWrite(true);
  //写的时候缓存索引
  colDesc.setCacheIndexesOnWrite(true);
　　　　　//存储的时候使用压缩算法
  　　　 colDesc.setCompressionType(Algorithm.SNAPPY);
  //进行compaction的时候使用压缩算法
  colDesc.setCompactionCompressionType(Algorithm.SNAPPY);
  //压缩内存和存储的数据，区别于Snappy
  colDesc.setDataBlockEncoding(DataBlockEncoding.PREFIX);
  //写入硬盘的时候是否进行编码
  colDesc.setEncodeOnDisk(true);
  //关闭的时候，是否剔除缓存的块
  colDesc.setEvictBlocksOnClose(true);
  //是否保存那些已经删除掉的kv
  colDesc.setKeepDeletedCells(false);
  //让数据块缓存在LRU缓存里面有更高的优先级
  colDesc.setInMemory(true);
  //最大最小版本
  colDesc.setMaxVersions(3);
  colDesc.setMinVersions(1);
  //集群间复制的时候，如果被设置成REPLICATION_SCOPE_LOCAL就不能被复制了
  colDesc.setScope(HConstants.REPLICATION_SCOPE_GLOBAL);
  //生存时间
  colDesc.setTimeToLive(18000);
  
  tableDesc.addFamily(colDesc);
	
	 */
}

