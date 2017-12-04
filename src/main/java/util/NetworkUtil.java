package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * donghao.liu 柳东浩</br>
 * 2016年3月3日 上午11:49:36</br> 
 * @description
 * 
 */
public final class NetworkUtil {
	private static String ip;

	/**
	 * donghao.liu 柳东浩</br>
	 * 2016年3月3日 上午11:49:38</br>
	 * @return 
	 * @description
	 */
	private final static List<String> getAllLocalIpV4(){
		List<String> rtn=new ArrayList<String>();
		Pattern pat=Pattern.compile("\\d+\\.+\\d+\\.+\\d+\\.+\\d+", Pattern.DOTALL);
		try{
			Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
			for (; n.hasMoreElements();) {
				NetworkInterface e = n.nextElement();
				Enumeration<InetAddress> a = e.getInetAddresses();
				for (; a.hasMoreElements();) {
					InetAddress addr = a.nextElement();
					if(addr.isLinkLocalAddress() || addr.isLoopbackAddress())continue;
					Matcher m=pat.matcher(addr.getHostAddress());
					if(m.find()){
						String tmp=m.group();
						if(!"127.0.0.1".equals(tmp)){
							rtn.add(tmp);
						}
					}
				}
			}
		}catch(Exception e){
			
		}
		return rtn;
	}
	
	public static String getLocalIp(){
		if(ip==null){
			ip=getAllLocalIpV4().get(0);
		}
		return ip;
	}

}