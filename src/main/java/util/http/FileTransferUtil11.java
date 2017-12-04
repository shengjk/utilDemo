package util.http;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTransferUtil11 {
	private final static Logger logger = Logger.getLogger(FileTransferUtil11.class);

	public static void FileTransfer(String fileZipName, String ServiceAddr, String filename, String md5Hex) throws IOException {
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = null;

		HttpUriRequest httpUriRequest=null;
		try {
			long currentTime = System.currentTimeMillis();

			httpClient = HttpClientBuilder.create().setRetryHandler(new DefaultHttpRequestRetryHandler()).build();///默认失败后重发3次,也可以自定义


			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
			factory.setConnectTimeout(1000*60*3); //单位毫秒 2min
			factory.setReadTimeout(1000*60*5);

			File f = new File(fileZipName);
			FileBody fileBody = new FileBody(f);
			HttpEntity fileEntity = MultipartEntityBuilder.create().addPart(f.getName(), fileBody).build();

			httpUriRequest = RequestBuilder.post(ServiceAddr).addParameter("md5Hex", md5Hex).addParameter("filename", filename).setEntity(fileEntity).build();



			httpResponse = httpClient.execute(httpUriRequest);
			System.out.println("执行发送");


			long end = System.currentTimeMillis();
			long timeConsuming = (end - currentTime) / 1000;
			System.out.println("文件传输耗时：" + timeConsuming + "秒");

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {

				System.out.println("文件传输服务器正常响应！");

				HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity);

				System.out.println("返回结果：" + response);
				if (StringUtils.isNotEmpty(response)) {
					if ("ack".equals(response)) {
						System.out.println("数据发送成功！");
					} else if ("md5_error".equals(response)) {
						throw new RuntimeException("发送失败,md5_error!");
					} else {
						throw new RuntimeException("文件发送失败,返回结果为"+response);
					}
				}
				EntityUtils.consume(entity);
			} else {
				throw new RuntimeException("http异常状态码 ：" + statusCode);
			}

		} catch (Exception e) {
			throw new RuntimeException("发送zip时 "+e);
		} finally {

			if (httpClient != null) {
				httpClient.close();
			}
			if(httpResponse!=null){
				httpResponse.close();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		String fileZipName = "C:\\Users\\WUJUN\\Desktop\\2\\20160421.zip";
//		String fileZipName1 = "D:\\1\\20160329.zip";

//		String ServiceAddr = "http://jqb.jielema.com/fileupload/msjr/upload";
//		String ServiceAddr = "http://kn.feidee.net/loanmanage/loan/partner/msjr_batch_approval.do";
		String ServiceAddr = "http://localhost:8080/acctservlet";
//		String ServiceAddr = "http://opentest.daima360.com/msjr/push";
		//卡牛测试
//		String ServiceAddr = "http://kn.feidee.net/loanmanage/loan/partner/msjr_batch_approval.do";
		String parameter = "20160421.zip";

		FileInputStream fin=new FileInputStream(new File(fileZipName));
		String md5= DigestUtils.md5Hex(fin);
		if(fin!=null){
			fin.close();
		}

		System.out.println("md5Hex " + md5);

		//(String fileZipName, String ServiceAddr, String filename, String md5Hex)
		FileTransfer(fileZipName,ServiceAddr,parameter,md5);

	}
}

