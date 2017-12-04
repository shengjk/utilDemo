package util.io;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shengjk1 on 2017/11/13
 */
public class MergeTxt {
	public static void main(String[] args) throws Exception {
		File[] files= FileArray.getFileArray("/Users/iss/pyProject/iss/screptile","mwm_flowers_spider_products");
		for (File file:files) {
			System.out.println(file.getAbsolutePath());
			ArrayList<String> arrayList=WriteAndReadToFile.readFile(file.getAbsolutePath());
			WriteAndReadToFile.outPutList("/Users/iss/Desktop/mwm",arrayList,"mwm.txt",true);
		}
	}
}
