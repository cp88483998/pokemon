package pocket.total.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
/**
 * 战斗结束数据结算类
 * <p>Title: PVPResult<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class PVPResult {
	
	/**
	 * 战斗结束数据结算
	 * @param jo
	 * @return 返回玩家的胜点、胜率、段位
	 */
	public static JSONObject sendResult(JSONObject jo){
		
		String url = jo.getString("url");
		String totalUrl = url+"pvp/update?user="+jo.toString();
		System.out.println("-----:total url:"+totalUrl+"-----");
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		StringBuffer strBuf = null;
		JSONObject json = null;
		try {
			URL myUrl = new URL(totalUrl);
			conn = (HttpURLConnection) myUrl.openConnection();
		    conn.setConnectTimeout(10000);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
			conn.setUseCaches(false);//post不能使用缓存  
			conn.setRequestProperty("charset", "UTF-8");
		    
			// 获取URLConnection对象对应的输出流
//            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            // 发送请求参数
//            out.write(paramsStr);
//            // flush输出流的缓冲
//            out.flush();
		    
			strBuf = new StringBuffer();
            //读取URL的响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line =null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);
            }
            json = JSONObject.fromObject(strBuf.toString());
		    
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("请求超时！");
			json = null;
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
		return json;
		
	}
}
