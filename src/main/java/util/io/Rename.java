package util.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by shengjk1 on 2017/10/14
 */
public class Rename {
	
	/** */
	/**
	 * 文件重命名
	 *
	 * @param path 文件目录
	 */
	public static void renameFile(String path) throws Exception {
		File fs = new File(path);
		File[] files = fs.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".txt");
			}
		});
		
		
		System.out.println(files.length);
		
		ArrayList<String> arrayList = new ArrayList<>();
		
		for (File file : files) {
			String fileName = file.getName();
//			System.out.println("fileName "+fileName);
			String[] filenames = fileName.split("_", -1);
			
			String newname = filenames[0] + "-" + filenames[1] + "-" + filenames[2] + "-" + filenames[3] + "-" + filenames[4] + "_" + filenames[5] + "_" + filenames[6] + "_" + filenames[7].split("\\.")[0];
			System.out.println("newname: " + newname);
			String newPath = "/Users/iss/Desktop/html" + System.getProperty("file.separator") + "new" + System.getProperty("file.separator") + newname + ".txt";
//			System.out.println("newPath: "+newPath);
			boolean res = file.renameTo(new File(newPath));
			if (res) {
//				System.out.println("over");
			} else {
				System.out.println("error " + newPath);
			}
			
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		renameFile("/Users/iss/Desktop/test");
	}
}
