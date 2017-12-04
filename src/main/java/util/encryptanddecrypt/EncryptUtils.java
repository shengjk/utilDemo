package util.encryptanddecrypt;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class EncryptUtils
{
  private static Logger logger = Logger.getLogger(EncryptUtils.class);
  private static final String Algorithm = "DESede";
  
  public static String decrypt3DES(String value, String key)
    throws Exception
  {
    byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value));
    return new String(b, "utf-8");
  }
  
  public static String encrypt3DES(String value, String key)
    throws Exception
  {
    String str = byte2Base64(encryptMode(GetKeyBytes(key), value.getBytes("utf-8")));
    return str;
  }

  public static byte[] GetKeyBytes(String strKey)
    throws Exception
  {
    if ((null == strKey) || (strKey.length() < 1)) {
      throw new Exception("key is null or empty!");
    }
    //SHA(Secure Hash Algorithm，安全散列算法）
    MessageDigest alg = MessageDigest.getInstance("MD5");
    alg.update(strKey.getBytes());
    byte[] bkey = alg.digest();

    int start = bkey.length;
    byte[] bkey24 = new byte[24];
    for (int i = 0; i < start; i++) {
      bkey24[i] = bkey[i];
    }
    for (int i = start; i < 24; i++) {
      bkey24[i] = bkey[(i - start)];
    }
    return bkey24;
  }

  public static byte[] encryptMode(byte[] keybyte, byte[] src) throws Exception{
      SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
      Cipher c1 = Cipher.getInstance("DESede");
      c1.init(1, deskey);
      return c1.doFinal(src);

  }

  public static byte[] decryptMode(byte[] keybyte, byte[] src) throws Exception{

      SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
      
      Cipher c1 = Cipher.getInstance("DESede");
      c1.init(2, deskey);
      return c1.doFinal(src);

  }
  
  public static String byte2Base64(byte[] b)
  {
    return Base64.encode(b);
  }
}
