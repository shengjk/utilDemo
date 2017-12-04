package util.http;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by 小省  on 2016/5/23.
 */
public class HttpClient {
	private static Logger logger = Logger.getLogger("HttpClient.class");
	public static String requestByPostMethod(List<NameValuePair> ipList) {
		
		CloseableHttpClient httpClient = getHttpClient();
		String result=null;
		try {
			HttpPost post = new HttpPost("http://192.168.0.230:8080/dashboard/getDimensionValues.do?datasetId=56&colmunName=date_year"); //这里用上本机的某个工程做测试
			
			//url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(ipList, "UTF-8");
			post.setEntity(uefEntity);
			System.out.println("POST 请求...." + post.getURI());
			//执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					System.out.println("-------------------------------------------------------");
					System.out.println(EntityUtils.toString(entity));
					System.out.println("-------------------------------------------------------");
					result= EntityUtils.toString(entity);
				}
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			logger.info("scan 请求接口失败 "+e);
			throw new RuntimeException("scan 请求接口失败 "+e);
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return  result;
	}
	
	private static CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}
	
	private static  void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}
	
	
	//添加要传递的参数
	public static void main(String[] args) {
		List<NameValuePair> list=new ArrayList<>();
		list.add(new BasicNameValuePair("colmunName","DATE_YEAR"));
		list.add(new BasicNameValuePair("datasetId","4"));
		requestByPostMethod(list);
	
	}
}
