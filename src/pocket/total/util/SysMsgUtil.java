package pocket.total.util;

import java.io.IOException;
import java.util.Properties;

public class SysMsgUtil {
	private static SysMsgUtil instance = null;
	public static SysMsgUtil getInstance(){
		if(instance == null){
			instance = new SysMsgUtil();
		}
		return instance;
	}
	// 读取配置文件
	private static Properties p = new Properties();
	public String getPreperty(String param){
		return p.getProperty(param);
	}
	public SysMsgUtil() {
//		System.out.println("ClassLoader.getSystemResource() :"+ClassLoader.getSystemResource(""));
//		System.out.println("ClassLoader.getSystemClassLoader() :"+ClassLoader.getSystemClassLoader().getResource("resource/sysMsg.properties"));
		try {
			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/sysMsg.properties"));
//			System.out.println("p :"+p.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
}
