package jackson;

import java.io.IOException;

/**
 * @author shengjk1
 * @date 2019-04-18
 */
public class IsGoodJson {
	public static boolean isGoodJson(String value,Class<?> valueType ){
		boolean isGoodJson=true;
		try {
			Json2Others.json2Ob(value, valueType);
		} catch (IOException e) {
			isGoodJson=false;
		}
		return isGoodJson;
	}
	
}
