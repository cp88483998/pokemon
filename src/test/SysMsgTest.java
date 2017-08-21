package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.python.modules.thread;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.total.entity.Share;
import pocket.total.util.MongoDBUtil;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;

public class SysMsgTest {

	@Test
	public void test1(){
		String result = "RES: false";
		String[] rs = result.split(":");
		System.out.println(rs[1].trim().equals("false"));
	}
	
	@Test
	public void sysMsgTest1() throws UnsupportedEncodingException{
		String msg = "Servers will be closed 3 minutes later, please offline in advance to avoid unnecessary losses.";
		String[] rs ;
		for(int i=1;i<6;i++){
			sendSysMsg(msg, i);
		}
	}
	@Test
	public void sysMsgTest(){
		List<String> robotNames = StaticClass.robotNames;
		int i = MyUtil.random(robotNames.size());
		String robotName = robotNames.get(i);
		System.out.println(robotName);
	}
	
	@Test
	public void elveNameTest(){
		JSONArray ja = StaticClass.Elves_data_js;
		int i = MyUtil.random(ja.size());
		JSONObject jo = ja.getJSONObject(i);
		while(!jo.getString("Elves_rarity").equals("史诗")){
			i = MyUtil.random(ja.size());
			jo = ja.getJSONObject(i);
		}
		String elveName = jo.getString("Elves_name");
		System.out.println(elveName);
	}
	
	@Test
	public void sendTest(){
		/*
		 * 先获取当前时间
		 * 获取当前人数
		 * 根据人数随机出发送CD时间
		 * 计算发送次数
		 */
		long startTime = Share.startTime;
		int num = 0;
		long intervalTime;
		int sendCount = 1;
		if(num > 50){
			intervalTime = MyUtil.random(60, 180);
			if(MyUtil.random(100) > 50){
				sendCount = 3;
			}
		}
		if(num >20 && num <=50){
			intervalTime = MyUtil.random(120, 300);
		}
		if(num <= 20){
			intervalTime = MyUtil.random(300, 600);
		}
		
	}
	
	public static String sendSysMsg(String msg, int roomId) throws UnsupportedEncodingException{
		
		msg = URLEncoder.encode(msg, "UTF-8");
		System.out.println(msg);
		String path = "http://123.206.201.113:8080/systemMsg/send?message="+msg+"&room_id="+roomId;
		
		String result = null;
			
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
//			conn.setRequestProperty("charset", "UTF-8");
//			    System.out.println(conn);
		    
			strBuf = new StringBuffer();
            //读取URL的响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line =null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);
            }
            result =  strBuf.toString();
            System.out.println(result);
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
		return result;
	}
	
}
