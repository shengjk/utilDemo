package javasssss.df;

import javasssss.Student;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

public class RDD2DataFrameReflection {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("RDD2DataFrameReflection").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		
		JavaRDD<String> lines = sc.textFile("students.txt");
		JavaRDD<Student> studentRDD = lines.map(new Function<String, Student>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Student call(String line) throws Exception {
				String[] lineSplited = line.split(",");
				Student stu = new Student();
				stu.setId(Integer.valueOf(lineSplited[0]));
				stu.setName(lineSplited[1]);
				stu.setAge(Integer.valueOf(lineSplited[2]));
				return stu;
			}
		});
		
		// 使用反射方式将RDD转换为DataFrame
		DataFrame studentDF = sqlContext.createDataFrame(studentRDD, Student.class);
		studentDF.printSchema();
		// 有了DataFrame后就可以注册一个临时表，SQL语句还是查询年龄小于18的人
		studentDF.registerTempTable("student");
		DataFrame teenagerDF = sqlContext.sql("select * from student where age <= 18");
		JavaRDD<Row> teenagerRDD = teenagerDF.toJavaRDD();
		JavaRDD<Student> teenagerStudentRDD = teenagerRDD.map(new Function<Row, Student>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Student call(Row row) throws Exception {
				// 通过反射来生成这个DataFrame的方式如果使用get(index)，大家要注意这个列的顺序是字典顺序
//				int id = row.getInt(1);
//				String name = row.getString(2);
//				int age = row.getInt(0);
				
				// 第二种可以直接通过列明来从Row里面来获取数据，这样的好处就是不用担心上面放上的顺序了
				int id = row.getAs("id");
				int age = row.getAs("age");
				String name = row.getAs("name");
				
				Student stu = new Student();
				stu.setId(id);
				stu.setName(name);
				stu.setAge(age);
				return stu;
			}
		});
		
		List<Student> studentList = teenagerStudentRDD.collect();
		for(Student stu : studentList){
			System.out.println(stu);
		}
	}
}
