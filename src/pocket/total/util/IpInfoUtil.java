package pocket.total.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

public class IpInfoUtil {
	/*
	 * 根据IP查询归属地信息
	 */
	public static String getIpInfo(String ip){
		if(StringUtils.isBlank(ip)){
			return null;
		}
		String url = MongoDBUtil.getSystemParamKeyValue("ipSearchUrl");
		JSONObject jo = null;
		String country = null;
		if(StringUtils.isNotBlank(ip)){
			
			String path = url+ip;
			
			BufferedReader reader = null;
			HttpURLConnection conn = null;
			OutputStreamWriter out = null;
			StringBuffer strBuf = null;
			try {
				URL myUrl = new URL(path);
				conn = (HttpURLConnection) myUrl.openConnection();
			    conn.setConnectTimeout(5000);
//			    conn.setRequestMethod("POST");
			    conn.setDoInput(true);
			    conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestProperty("charset", "UTF-8");
//			    System.out.println(conn);
			    
				strBuf = new StringBuffer();
	            //读取URL的响应
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
	            String line =null;
	            while ((line = reader.readLine()) != null) {
	                strBuf.append(line);
	            }
			    jo = JSONObject.fromObject(strBuf.toString());
//			    System.out.println(jo);
			    //淘宝查询IP接口
//			    country = jo.getJSONObject("data").getString("country");
			    //新浪查询IP接口
			    country = jo.getString("country");
//			    System.out.println(country);
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
		return country;
	}
}
