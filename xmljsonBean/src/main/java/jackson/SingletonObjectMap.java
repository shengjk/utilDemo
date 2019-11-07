package jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengjk1 on 2017/10/18
 */
public class SingletonObjectMap {
	private static class SingletonFactory {
		private static ObjectMapper objectMapper = new ObjectMapper();
	}
	
	public static ObjectMapper getObjectMapper() {
		return SingletonFactory.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
				.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
				.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true)
				.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
				.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
				.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
				.configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
				.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
//				.enable(SerializationFeature.INDENT_OUTPUT)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//		return SingletonFactory.objectMapper;
	}
	
	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		//格式化输出
		mapper.enable(SerializationFeature.INDENT_OUTPUT)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		mapper.writerWithDefaultPrettyPrinter().writeValueAsString("");
		
		Person person = new Person("", 40);
		person.setAge(40);
		String jsonString = mapper
				.writeValueAsString(person);
		System.out.println(jsonString);
		
		//基于default constructor
		Person deserializedPerson = mapper.readValue(jsonString, Person.class);
		Map map = mapper.readValue(jsonString, Map.class);
		//类型擦除并不影响真正的类型
		Object o = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
		});
		
		Map<String, Object> map1 = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
		});
		//类型擦除并不影响真正方法的调用
		System.out.println(String.valueOf(((Map) o).get("name")));
		System.out.println(String.valueOf((map1.get("name"))));
		//
		//如果结果可能是Object或者是Array，那可以使用JsonNode;
//如果你知道是Object，你可以直接强转成ObjectNode;如果你知道是Array，你可以直接强转成ArrayNode;
		jsonString = "{\"@timestamp\":\"\",\"beat\":{\"hostname\":\"iss-bigdata-ali-f-dsp-002-12-101\",\"name\":\"iss-bigdata-ali-f-dsp-002-12-101\",\"version\":\"5.6.4\"},\"input_type\":\"log\",\"message\":\"{\\\"Time\\\":\\\"2019-05-01T14:04:02.918\\\",\\\"Type\\\":\\\"cnv_node_record\\\",\\\"ActivityID\\\":\\\"1001\\\",\\\"CnvNodeID\\\":\\\"1200\\\",\\\"UserID\\\":\\\"-1\\\",\\\"DeviceIDType\\\":\\\"1\\\",\\\"DeviceID\\\":\\\"55ef8057868ad2f965e8691b0f7ff821\\\",\\\"ClientIP\\\":\\\"\\\",\\\"UniqID\\\":\\\"1_55ef8057868ad2f965e8691b0f7ff821\\\",\\\"ActionTime\\\":\\\"2019-05-01T14:04:02.918\\\",\\\"ReportFrom\\\":\\\"third_dsp\\\"}\",\"offset\":1510,\"serverName\":\"iss-bigdata-ali-f-dsp-002-12-101\",\"source\":\"/mnt/iss/go/recmd-dsp-log/stats.log.2019050114\",\"type\":\"bigdata-dsp\",\n" +
				"\"testArray\":[\"a\",\"b\",\"c\"]}";
		System.out.println(jsonString);
		JsonNode baseRoot = mapper.readTree(jsonString);
		//一级嵌套
		String source = baseRoot.get("source").asText();
		System.out.println("source======== " + source);
		//一级嵌套 int类型可以作为string
		String offset = baseRoot.get("offset").asText();
		System.out.println("offset====== " + offset);
		//二级嵌套
		JsonNode beat = baseRoot.get("beat");
		System.out.println("beat type========" + beat.getNodeType());
		String name = beat.get("hostname").asText();
		System.out.println("hostname=========" + name);
		
		String message = baseRoot.get("message").asText();
		System.out.println("hostname=========" + message);
		
		
		//增删改查都是基于value
		//添加字段 ArrayNode
		JsonNode testArray = baseRoot.get("testArray");
		System.out.println("testArray======= " + testArray.getNodeType());
		ArrayNode arrayNode = (ArrayNode) testArray;
		//从 0 开始计数
		//index > biggest 取 biggest index<  smallest 取  smallest
		arrayNode.insert(-1, "ccca");
		//默认添加到最后一位
		arrayNode.add("add");
		arrayNode.remove(3);
		arrayNode.removeAll();
		
		
		//添加字段 ObjectNode
		ObjectNode objectNode = (ObjectNode) baseRoot;
		objectNode.putArray("a").add("ac").add("ccc");
		objectNode.put("baseRoot", "baseRoot");
		objectNode.with("objectNode").put("objectNode1", "objectNode1").put("objectNode2", "objectNode2");
		//更新
		objectNode.put("source", "source");
		//删除其他所有的属性，除了 source
//		objectNode.retain("source");
		//删除单个属性
//		objectNode.remove("source");
		JsonNodeType nodeType = baseRoot.getNodeType();
		System.out.println("nodeType======== " + nodeType);
		
		
		ObjectNode beat1 = (ObjectNode) beat;
		beat1.put("beat1", "beat1");
//		beat1.removeAll();
		
		
		System.out.println("============= " + mapper.writeValueAsString(baseRoot));
		
		
		String json = "{\n" +
				"  \"name\" : \"明\",\n" +
				"  \"age\" : 40,\n" +
				"  \"age1\" : 0,\n" +
				"  \"bitrh_day\" : \"2019-02-03 12:32:23.222\",\n" +
				"  \"school_day\" : 1556770883000,\n" +
				"  \"p_name\" : \"小明\"\n" +
				"}";
		
		Person person1 = mapper.readValue(json, Person.class);
		System.out.println(person1.getpName());
		System.out.println(mapper.writeValueAsString(person1));
		
		MapType javaType =
				mapper.getTypeFactory().constructMapType(HashMap.class, String.class,
						String.class);
//		Map<String, String> personMap = mapper.readValue(json,
//				javaType);
		
		Map<String, String> personMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
		});
		System.out.println(personMap.get("name"));
		
		/*
	属性可视化
		是 java 对象的所有的属性都被序列化和反序列化，换言之，不是所有属性都可视化，默认的属性可视化的规则如下：
		
		若该属性修饰符是 public，该属性可序列化和反序列化。
		若属性的修饰符不是 public，但是它的 getter 方法和 setter 方法是 public，该属性可序列化和反序列化。因为 getter 方法用于序列化， 而 setter 方法用于反序列化。
		若属性只有 public 的 setter 方法，而无 public 的 getter 方 法，该属性只能用于反序列化。
		若想更改默认的属性可视化的规则，需要调用 ObjectMapper 的方法 setVisibility。

		PropertyAccessor 支持的类型有 ALL,CREATOR,FIELD,GETTER,IS_GETTER,NONE,SETTER
 Visibility 支持的类型有 A
    NY,DEFAULT,NON_PRIVATE,NONE,PROTECTED_AND_PUBLIC,PUBLIC_ONLY
		 */
//		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		
		Person person2 = mapper.readValue(json, Person.class);
		System.out.println("person2========"+person2.getName());
		System.out.println("getAge=========="+person2.getAge());
		System.out.println(mapper.writeValueAsString(person2));
		
		//bean to jsonNode
		JsonNode personNode = mapper.valueToTree(person2);
		String name1 = personNode.get("age").asText();
		System.out.println(name1);
		System.out.println(name1);
		
		
	}
}
