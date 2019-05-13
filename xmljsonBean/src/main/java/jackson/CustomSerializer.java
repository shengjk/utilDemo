package jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * 当 Jackson 默认序列化和反序列化的类不能满足实际需要，可以自定义新的序列化和反序列化的类。
 *
 * 自定义序列化类。自定义的序列化类需要直接或间接继承 StdSerializer 或 JsonSerializer，
 * 同时需要利用 JsonGenerator 生成 json，重写方法 serialize，示例如下：
 */
public class CustomSerializer extends StdSerializer<Person> {
	
	protected CustomSerializer(Class<Person> t) {
		super(t);
	}
	
	@Override
	public void serialize(Person person, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("age", person.getAge());
		gen.writeStringField("name", person.getName());
		gen.writeEndObject();
	}
	/*
	JsonGenerator 有多种 write 方法以支持生成复杂的类型的 json，比如 writeArray，writeTree 等 。
	若想单独创建 JsonGenerator，可以通过 JsonFactory() 的 createGenerator
	 */
	
}