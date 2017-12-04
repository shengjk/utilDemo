package util.ip;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shengjk1 on 2017/4/21 0021
 */

public class RequestUtil {
	/**
	 * 获取客户端的真实ip
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
}
