package util.io;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by shengjk1 on 2017/10/18
 */
public class FileArray {
	
	/**
	 *"/Users/iss/Desktop/order_info_topic", "153"
	 * @param pathStr
	 * @return
	 */
	public  static  File[]   getFileArray(String pathStr, final String filter){
		File file = new File(pathStr);
		
		return file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().contains(filter);
			}
		});
	}
}
