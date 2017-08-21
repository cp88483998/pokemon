package pocket.total.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 离线私有消息存取类
 * <p>Title: OfflinePrivateMsg<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月24日
 */
public class OfflinePrivateMsg {
	/**
	 * 请求保存离线消息
	 * @param jo
	 * @return
	 */
	public static JSONObject sendOfflineMsg(JSONObject jo){
		String url = jo.getString("url");
		String totalUrl = url+"offlinePriMsg/add?infor="+jo.toString();
		
        JSONObject json = JSONObject.fromObject(sendGet(jo, totalUrl));
		
		return json;
	}
	/**
	 * 请求判断是否有离线消息
	 * @param jo
	 * @return
	 */
	public static JSONObject hasOfflineMsg(JSONObject jo){
		String url = jo.getString("url");
		String totalUrl = url+"offlinePriMsg/judge?infor="+jo.toString();
		
		JSONObject json = JSONObject.fromObject(sendGet(jo, totalUrl));
		
		return json;
	}
	/**
	 * 请求获得离线消息
	 * @param jo
	 * @return
	 */
	public static JSONArray getOfflineMsg(JSONObject jo){
		String url = jo.getString("url");
		String totalUrl = url+"offlinePriMsg/get?infor="+jo.toString();
		
		JSONArray ja = JSONArray.fromObject(sendGet(jo, totalUrl));
		
		return ja;
	}
	
	/**
	 * 发送请求
	 * @param jo
	 * @param totalUrl
	 * @return
	 */
	public static String sendGet(JSONObject jo, String totalUrl){
		BufferedReader reader = null;
		HttpURLConnection conn = null;
//		OutputStreamWriter out = null;
		StringBuffer strBuf = null;
		String result = null;
		try {
			URL myUrl = new URL(totalUrl);
			conn = (HttpURLConnection) myUrl.openConnection();
		    conn.setConnectTimeout(10000);
		    conn.setRequestMethod("POST");
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
			conn.setUseCaches(false);//post不能使用缓存  
			conn.setRequestProperty("charset", "UTF-8");
		    
//			// 获取URLConnection对象对应的输出流
//            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            /*
//             * 发送请求参数
//             */
//            out.write(jo.toString());
//            // flush输出流的缓冲
//            out.flush();
		    
			strBuf = new StringBuffer();
            //读取URL的响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line =null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);
            }
            result = strBuf.toString();
		    
		} catch (Exception e) {
			System.out.println("请求超时！");
//			e.printStackTrace();
		}finally{
			try {
//				if(out!=null){
//                    out.close();
//                }
                if(reader!=null){
                	reader.close();
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
