package pack.java.midea.dao;  
  
import java.io.UnsupportedEncodingException;  
/** 
 * 测试; 
 * @author zhouhaitao 
 * 2012-5-17 
 */  
public class Test {  
  
    /** 
     * @param args 
     * @throws UnsupportedEncodingException  
     */  
    public static void main(String[] args) throws UnsupportedEncodingException {  
        // TODO Auto-generated method stub  
        Test test = new Test();  
        String a = "在";  
        test.getStringByteLength(a);  
        System.out.println("--------------------------------------");  
        String b = "A";  
        test.getStringByteLength(b);  
        String c = "1";  
        test.getStringByteLength(c);  
      
    }  
      
    /** 
     * 获取字符的所占字节长度; 
     * @param str 
     * @throws UnsupportedEncodingException 
     */  
    private void getStringByteLength(String str) throws UnsupportedEncodingException{  
        System.out.println("\""+str+"\"字符所占的字节长度如下:");  
        System.out.println("ISO-8859-1:"+str.getBytes("ISO-8859-1").length);  
        System.out.println("UTF-8:"+str.getBytes("UTF-8").length);  
        System.out.println("GBK:"+str.getBytes("GBK").length);  
        System.out.println("GB2312:"+str.getBytes("GB2312").length);  
        System.out.println("GB18030:"+str.getBytes("GB18030").length);  
        System.out.println("UTF-16:"+str.getBytes("UTF-16").length);  
    }  
}  