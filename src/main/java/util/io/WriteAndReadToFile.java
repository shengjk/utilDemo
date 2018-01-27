package util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by shengjk1 on 2017/3/29 0029.
 */
public class WriteAndReadToFile {
	private static final Logger logger = LoggerFactory.getLogger(WriteAndReadToFile.class);
	
	/**
	 *
	 * @param pathtxt
	 * @return
	 */
	public static ArrayList<String> readFile(String pathtxt) throws Exception{
		BufferedReader br=null;
		InputStreamReader isr=null;
		FileInputStream fis=null;
		ArrayList<String> list =new ArrayList<String>();
		try {
			fis=new FileInputStream(pathtxt);
			isr=new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String line="";
			while ((line=br.readLine())!=null) {
				if (line.contains("\\\"status\\\":\\\"60\\\"")){
					list.add(line+"\n");
				}
			}
		}  catch (Exception e) {
			logger.error("文件读取失败 {}",e);
			throw new RuntimeException("文件读取失败",e);
		}finally{
			try {
				if (br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("流关闭失败 {}",e);
			}
		}
		return list;
	}
	
	/**
	 *
	 * @param path
	 * @param textValue
	 * @param FileName
	 * @param isAppent 当isappent为false时，自动对文件进行重写
	 *
	 */
	public static void outPut(String path,String textValue,String FileName,boolean isAppent) throws Exception{
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(path+System.getProperty("file.separator")+FileName),isAppent));
			byte[] data = textValue.getBytes();
			os.write(data, 0, data.length);
			os.flush();
			logger.info("写入成功");
		} catch (Exception e) {
			logger.error("文件写入失败 {}",e);
			throw new RuntimeException("文件写入失败 ",e);
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("流关闭失败 {}",e);
			}
		}
	}
	
	
	/**
	 *
	 * @param path
	 * @param text
	 * @param FileName
	 * @param isAppent
	 * @throws Exception
	 */
	public static void outPutList(String path,ArrayList<String> text,String FileName,boolean isAppent) throws Exception{
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(path+System.getProperty("file.separator")+FileName),isAppent));
			for (String textValue:text ) {
				byte[] data = textValue.getBytes();
				os.write(data, 0, data.length);
			}
			os.flush();
			logger.info("写入成功");
		} catch (Exception e) {
			logger.error("文件写入失败 {}",e);
			throw new RuntimeException("文件写入失败 ",e);
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("流关闭失败 {}",e);
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		outPutList("/Users/iss/Desktop",readFile("/Users/iss/Desktop/update.txt"),"update1.txt",true);
	}
}
