package util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengjk1 on 2017/11/29
 */
public class JsonAndOb {
	
	/**
	 *
	 * @param json
	 * @param valueType
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public static <T> T json2Ob(String json,Class<T> valueType) throws IOException {
		ObjectMapper ob =SingletonObjectMap.getObjectMapper();
		return ob.readValue(json,valueType);
	}
	
	
	/**
	 * ob2json/map2json
	 * @param value
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String Ob2Json(Object value) throws JsonProcessingException {
		ObjectMapper objectMapper = SingletonObjectMap.getObjectMapper();
		return objectMapper.writeValueAsString(value);
	}
	
	/**
	 * json2map
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static Map json2map(String json) throws IOException {
		ObjectMapper objectMapper = SingletonObjectMap.getObjectMapper();
		return objectMapper.readValue(json,HashMap.class);
	}
	
	
	public static void main(String[] args) throws IOException {
//
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("id","14");
		hashMap.put("ip","http://11111:8111");
		hashMap.put("sign","1");
		hashMap.put("is_pay","2");
		
		System.out.println(Ob2Json(hashMap));
		
		String json="{\"ip\":\"http://11111:8111\",\"sign\":\"1\",\"id\":\"14\",\"is_pay\":\"2\"}";
		
		Map map=json2map(json);
		System.out.println(map.get("ip"));
	}
}
