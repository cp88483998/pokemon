package pocket.total.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * 时空缝隙工具类
 * <p>Title: BossUtil<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年8月16日
 */
public class BossUtil {
	
	/*
	 * 验证boss战斗数据
	 */
	public static String verifyBossData(String data, String url){
		if(StringUtils.isBlank(data)){
			return null;
		}
		//获取配置文件中url
//		String url = MongoDBUtil.getSystemParamKeyValue("bossServerUrl");
		url += "gap/update/boss";
		String result = null;
		if(StringUtils.isNotBlank(data)){
			
			BufferedReader reader = null;
			HttpURLConnection conn = null;
			OutputStreamWriter out = null;
			StringBuffer strBuf = null;
			try {
				URL myUrl = new URL(url);
				conn = (HttpURLConnection) myUrl.openConnection();
			    conn.setConnectTimeout(10000);
			    conn.setRequestMethod("POST");
			    conn.setDoInput(true);
			    conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestProperty("charset", "UTF-8");
				strBuf = new StringBuffer();
				out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				// 发送请求参数
				out.write(data);
				// flush输出流的缓冲
				out.flush();
				//读取URL的响应
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
	            String line =null;
	            while ((line = reader.readLine()) != null) {
	                strBuf.append(line);
	            }
	            result = strBuf.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(out!=null){
	                    out.close();
	                }
	                if(reader!=null){
	                	reader.close();
	                }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
