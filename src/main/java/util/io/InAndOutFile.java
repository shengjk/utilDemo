package util.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengjk1 on 2017/3/29 0029.
 */
public class InAndOutFile {
	public static List<String> readFile(String pathtxt) {
		BufferedReader br=null;
		InputStreamReader isr=null;  //C:\Users\WUJUN\Desktop
		FileInputStream fis=null;
		List<String> list =new ArrayList<String>();
		try {
			//br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\WUJUN\\Desktope\\key和value都不相同.txt"), "UTF-8"));
			fis=new FileInputStream(pathtxt);
			isr=new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String line="";
			String[] arrs=null;
			int i=0;
			while ((line=br.readLine())!=null) {
				arrs=line.split(" ");
				list.add(arrs[0]);
				i++;
//				System.out.println(arrs.length);
//				System.out.println(arrs[0]/* + " : " + arrs[1]*/);
			}
//			System.out.println("总共读取"+i+"个");
			return list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件不存在！");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件读取失败！");
			return null;
		}finally{
			try {
				if (br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *
	 * @param path
	 * @param textValue
	 * @param FileName
	 */
	public static void outPut(String path,String textValue,String FileName) {
		OutputStream os = null;
		try {
//			os = new BufferedOutputStream(new FileOutputStream(new File(path+System.getProperty("file.separator")+FileName),true));
			os = new BufferedOutputStream(new FileOutputStream(new File(path+System.getProperty("file.separator")+FileName),false));
			byte[] data = textValue.getBytes();
			os.write(data, 0, data.length);
			os.flush();
//			System.out.println("写入成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件不存在");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件写入失败");
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
