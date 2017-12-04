package util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by shengjk1 on 2017/10/18
 */
public class SingletonObjectMap {
	private static class SingletonFactory{
		private static ObjectMapper objectMapper=new ObjectMapper();
	}
	
	public static ObjectMapper getObjectMapper(){
		return SingletonFactory.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
}
