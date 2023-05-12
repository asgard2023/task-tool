package cn.org.opendfl.tasktool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class InetAddressUtils {

	final static Logger logger = LoggerFactory.getLogger(InetAddressUtils.class);

	private static String localhostIp=null;
	private static String localhostName=null;
	
	/**
	 * 获取机器IP
	 * @return
	 */
	public static synchronized String getLocalHostIp() {
		Long time=System.currentTimeMillis();
		if(localhostIp==null) {
			if (isWindowsOS()) {
				try {
					localhostIp = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					logger.info("Error when getting windows host ip address: <{}>.", e.getMessage());
				}
			} else {
				InetAddress inetAddress = getLinuxLocalHost();
				if (null != inetAddress) {
					localhostIp=inetAddress.getHostAddress();
				}
			}
			logger.debug("-----getLocalHostIp--time={}", System.currentTimeMillis()-time);
		}
		return localhostIp;
	}
	
	/**
	 * 判断操作系统是否是windows
	 * @return
	 */
	 public static boolean isWindowsOS() {
	        boolean isWindowsOS = false;
	        String osName = System.getProperty("os.name");
	        if (osName.toLowerCase().contains("windows")) {
	            isWindowsOS = true;
	        }
	        return isWindowsOS;
	    }
	 
	 
	 /**
	  * 获取linux系统的InetAddress对象
	  * @return
	  */
	 public static InetAddress getLinuxLocalHost() {
		 try {
	            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
	                NetworkInterface networkInterface = interfaces.nextElement();
	                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
	                    continue;
	                }
	                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
	                if (addresses.hasMoreElements()) {
	                    return addresses.nextElement();
	                }
	            }
	        } catch (SocketException e) {
	            logger.info("Error when getting linux host ip address: <{}>.", e.getMessage());
	        }
	        return null;
	 }

	 public static String getLocalHostName(){
	 	if(localhostName==null){
	 		localhostName=getHostName();
		}
	 	return localhostName;
	 }

	private static String getHostName(){
		try {
			Process p = Runtime.getRuntime().exec("hostname");
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line=null;
			//只需要取出第一行
			if((line=br.readLine())!=null)
			{
				return line;
			}


			br.close();
			p.destroy();
			return line;


		} catch (IOException e) {
			logger.error("IOException", e);
			return e.getMessage();
		}
	}
}
