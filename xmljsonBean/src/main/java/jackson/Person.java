package jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * @author shengjk1
 * @date 2019-05-10
 */
//用于类，指定属性在序列化时 json 中的顺序
@JsonPropertyOrder(alphabetic = true, value = {"p_name,name"})

//将Java对象序列化为Json时或者反序列化的时候，对属性进行过滤@JsonIgnore  @JsonIgnoreProperties
//@JsonIgnoreProperties(value = {"age","age1"})

public class Person {
	//	@JsonIgnore
	private String name;
	private int age;
	private int age1;
	
	/*
不同类型的日期类型，Jackson 的处理方式也不同。

	对于日期类型为 java.util.Calendar,java.util.GregorianCalendar,java.sql.Date,java.util.Date,java.sql.Timestamp，若不指定格式， 在 json 文件中将序列化 为 long 类型的数据。显然这种默认格式，可读性差，转换格式是必要的。Jackson 有 很多方式转换日期格式。
	注解方式，请参照" Jackson 的注解的使用"的@ JsonFormat 的示例。
	ObjectMapper 方式，调用 ObjectMapper 的方法 setDateFormat，将序列化为指定格式的 string 类型的数据。
	对于日期类型为 java.time.LocalDate，还需要添加代码 mapper.registerModule(new JavaTimeModule())，同时添加相应的依赖 jar 包
	清单 4 . JSR31 0 的配置信息
	<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-jsr310</artifactId>
	<version>2.9.1</version>
	</dependency>
	对于 Jackson 2.5 以下版本，需要添加代码 objectMapper.registerModule(new JSR310Module ())


	对于日期类型为 org.joda.time.DateTime，还需要添加代码 mapper.registerModule(new JodaModule())，同时添加相应的依赖 jar 包
	清单 5. joda 的 配置信息
	<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-joda</artifactId>
	<version>2.9.1</version>
	</dependency>
	
	 */
	@JsonProperty("bitrh_day")
	//字符串时，不需要加时区
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date bitrhDay;
	
	@JsonProperty("school_day")
	//用于属性或者方法，把属性的格式序列化时转换成指定的格式 毫秒的时候需要加时区
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	private Date schoolDay;
	
	
	//用于属性，把属性名称序列化时转化为jsonPropery
	//反序列化的时候用jsonPropery来赋值
	/*
	@JsonProperty
		这个注解提供了序列化和反序列化过程中该java属性所对应的名称
	@JsonAlias
		这个注解只只在反序列化时起作用，指定该java属性可以接受的更多名称
	 */
	//两个同时存在的情况下，JsonProperty起作用
	@JsonProperty("p_name")
	/*
	{
  "age" : 40,
  "age1" : 0,
  "bitrh_day" : "2019-02-03 12:32",
  "name" : "明",
  "p_name" : "小明",
  "school_day" : "2019-05-02 12:21"
}
	 */
//	@JsonAlias("p_name")
	/*
	{
  "age" : 40,
  "age1" : 0,
  "bitrh_day" : "2019-02-03 12:32",
  "name" : "明",
  "pName" : "小明",
  "school_day" : "2019-05-02 12:21"
}
	 */
	private String pName;
	
	
	public Person() {
	}
	
	
	public Person(String name, int age, int age1) {
		this.name = name;
		this.age = age;
		this.age1 = age1;
	}
	
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public Date getSchoolDay() {
		return schoolDay;
	}
	
	public void setSchoolDay(Date schoolDay) {
		this.schoolDay = schoolDay;
	}
	
	public Date getBitrhDay() {
		return bitrhDay;
	}
	
	public void setBitrhDay(Date bitrhDay) {
		this.bitrhDay = bitrhDay;
	}
	
	public String getpName() {
		return pName;
	}
	
	public void setpName(String pName) {
		this.pName = pName;
	}
	
	public int getAge1() {
		return age1;
	}
	
	public void setAge1(int age1) {
		this.age1 = age1;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}
