package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shengjk1 on 2017/10/16
 */
public class Match {
	
	public static String  reularMatch(String s,String reular) {
		String rs="";
		Pattern p = Pattern.compile(reular);
		Matcher m = p.matcher(s);
		while (!m.hitEnd() && m.find()) {
			rs= m.group(1);
		}
		return rs;
	}
}
