package util.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by shengjk1 on 2017/10/13
 */
public class Request {
	private static final Logger logger = LoggerFactory.getLogger(Request.class);
	
	/**
	 *
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient createHttpClient(){
		return HttpClients.createDefault();
	}
	
	
	public static void closeHttpResponse(CloseableHttpResponse httpResponse) throws IOException {
		if (httpResponse != null) {
			httpResponse.close();
		}
	}
	
	/**
	 *
	 * @param client
	 * @throws IOException
	 */
	public static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}
	
}
