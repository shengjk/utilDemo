package jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;

/**
 * 自定义反序列化类。自定义的反序列化类需要直接或间接继承 StdDeserializer 或 StdDeserializer，
 * 同时需要利用 JsonParser 读取 json，重写方法 deserialize，示例如下：
 */
public class CustomDeserializer extends StdDeserializer<Person> {
	public CustomDeserializer(Class<Person> vc) {
		super(vc);
	}
	
	@Override
	public Person deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		Person person = new Person();
		int age = (Integer) ((IntNode) node.get("age")).numberValue();
		String name = node.get("name").asText();
		person.setAge(age);
		person.setName(name);
		return person;
	}
	/*
	JsonParser 提供很多方法来读取 json 信息， 如 isClosed(), nextToken(), getValueAsString()等。
	若想单独创建 JsonParser，可以通过 JsonFactory() 的 createParser。
	 */
	
	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		//格式化输出
		mapper.enable(SerializationFeature.INDENT_OUTPUT)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		SimpleModule module = new SimpleModule("myModule");
		module.addSerializer(new CustomSerializer(Person.class));
		module.addDeserializer(Person.class, new CustomDeserializer(Person.class));
		mapper.registerModule(module);
		
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
		
	}
}