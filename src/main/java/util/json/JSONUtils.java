package util.json;


import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * 处理json数据格式的工具类
 *
 * @version 1.0
 */
public class JSONUtils {
	public static Properties json2properties(Properties pro, JSONObject obj){
		Iterator iter = obj.keys();
		try {
			while (iter.hasNext()) {
				String key = iter.next().toString();
				pro.setProperty(key, obj.get(key).toString());
			}
		}catch (Exception e){
			e.getMessage();
		}
		return pro;
	}
	
	/**
	 *
	 * @param json
	 * @return
	 */
	public static boolean isGoodJson(String json) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readValue(json,Map.class);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
