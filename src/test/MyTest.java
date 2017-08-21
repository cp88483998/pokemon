package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.TreeMap;

import org.bson.Document;
import org.junit.Test;
import org.python.core.SysPackageManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.chat.dao.MongoAddMsgDao;
import pocket.chat.dao.MongoAddMsgDaoImpl;
import pocket.chat.service.PublicService;
import pocket.pvp.dao.MongoPVPDaoImpl;
import pocket.pvp.entity.Player;
import pocket.pvp.service.RobotMatch;
import pocket.total.entity.Share;
import pocket.total.util.MongoDBUtil;
import pocket.total.util.MyUtil;
import pocket.total.util.PVPResult;
import pocket.total.util.StaticClass;

public class MyTest {
	private static Properties p = Share.p;
//	MongoPVPDaoImpl dao = MongoPVPDaoImpl.getInstance();
	MongoDBUtil utilInstance = MongoDBUtil.getInstance();
	MongoAddMsgDao dao = MongoAddMsgDaoImpl.getInstance();
	
	@Test
	public void mapTest2(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("123", "123");
		map.remove("asdad");
	}
	@Test
	public void mapTest1(){
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(3, 5);
		map.put(2, 4);
		map.put(1, 3);
		
		for(Integer key : map.keySet()){
			System.out.println(map.get(key));
		}
		
		A://设置一个标记 使用带此标记的break语句跳出多重循环体  
		for(int i=0;i<100;i++){
			for(int j=0;j<=i;j++){
				if(i==10){
					break A;
				}
			}
		}
	}
	@Test
	public void regularTest(){
		String count = "123456a";
		boolean isMatch =  count.matches("[0-9]{1,}");
		System.out.println(isMatch);
	}
	@Test
	public void mapTest(){
		for(int i=0;i<10;i++){
			Share.serverURL.put("14884231051702", i+"");
		}
		System.out.println(Share.serverURL.size());
	}
	@Test
	public void PVPTest(){
		JSONObject playerJO1 = new JSONObject();
		JSONObject playerJO2 = new JSONObject();
		JSONArray playerJA = new JSONArray();
		
		playerJO1.put("uid", "14884231051702");
		playerJO1.put("isWin", false);
		playerJO1.put("url", "http://115.159.207.89/");
		playerJA.add(playerJO1);
		playerJO2.put("uid", "14987025272902");
		playerJO2.put("isWin", true);
		playerJO2.put("url", "http://115.159.207.89/");
		playerJA.add(playerJO2);
		
//		for(int n=0;n<10;n++){
			System.out.println(PVPResult.sendResult(playerJO2));
//		}
		
//		JSONObject playerJO = null;
//		JSONObject resultJO = null;
//		for(int n=0;n<10;n++){
//			for(int i=0;i<playerJA.size();i++){
//				playerJO = playerJA.getJSONObject(i);
//				resultJO = PVPResult.sendResult(playerJO);
//				if(resultJO == null){
//					for(int j=0;j<2;j++){
//						resultJO = PVPResult.sendResult(playerJO);
//						if(resultJO != null){
//							break;
//						}
//					}
//				}
//				if(resultJO != null){
//					System.out.println("resultJO :"+resultJO);
//				}	
//			}	
//		}
	}
	@Test
	public void ranNameTest(){
		Random random = new Random();
		int size = StaticClass.server_name_js.size();
		int serverRan = random.nextInt(size);
		String serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
		while(serverName.equals("S1 Pallet Town")){
			serverRan = random.nextInt(size);
			serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
		}
		System.out.println(serverName);
		
		
		String ranName = RobotMatch.getRanName();
		System.out.println(ranName);
	}
	
	@Test
	public void pvpTest(){
		int a = 123;
		long b = (long) a;
		System.out.println(b);
	}
	@Test
	public void offMsgTest(){
//		14991694638846
//		14992246407242
		Document doc = dao.findOneUserInfo(14743423114157l, 5);
		long level = Long.parseLong(doc.get("level").toString());
		System.out.println(level);
	}
	@Test
	public void additionalMsgTest(){
		String additionalMsg = "1|F_G|test182,王路飞,20,10,2,0,2,2,24/4 05:39:12,58,14884231051702,1000315:2:68:girl;2000033:2:59:girl;1000282:2:53:girl;1000251:3:70:;2000032:2:49:girl;1000212:1:39:boy";
		long uid = 14884231051702l;
		String info = PublicService.findUserInfo(1, uid, additionalMsg);
		System.out.println(info);
	}
	@Test
	public void stringTest(){
		List<MongoDatabase> mongoDBs = utilInstance.getGameDB();
		for(MongoDatabase mongoDB : mongoDBs){
			MongoIterable<String> iterable = mongoDB.listCollectionNames();
			System.out.println("name :"+mongoDB.getName());
			for(String str : iterable){
				System.out.print(str+",");
			}
			System.out.println();
		}
	}
//	@Test
//	public void serverAdminTest(){
//		MongoDatabase mongoDB = MongoDBUtil_new.getServerAdminDB();
//		MongoCollection collection = MongoDBUtil_new.getOffCollection();
//		System.out.println(collection.count());
//	}
	@Test
	public void robotTest(){
		
		for(int i=1;i<=24;i++){	
			System.out.print(i);
			System.out.print(" : ");
			if(i == 0){
				break;
			}
			else if((i>=12 && i<=14) || (i>=19 && i<=21)){
				System.out.println(true);
			}
			else{
				System.out.println(false);
			}
		}
		System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
	}
	@Test
	public void sysMsgReadTest(){
		//玩家名
		List<String> robotNames = StaticClass.robotNames;
		int i = MyUtil.random(robotNames.size());
		String robotName = robotNames.get(i);
		//精灵名
		JSONArray ja = StaticClass.Elves_data_js;
		i = MyUtil.random(ja.size());
		JSONObject jo = ja.getJSONObject(i);
		while(!jo.getString("Elves_rarity").equals("史诗")){
			i = MyUtil.random(ja.size());
			jo = ja.getJSONObject(i);
		}
		String elveName = jo.getString("Elves_name");
		Properties p = new Properties();
		try {
			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/sysMsg.properties"));
			String param1 = p.getProperty("param1");
			String param2 = p.getProperty("param2");
			String param3 = p.getProperty("param3");
			String message = param1
					+robotName
					+param2
					+elveName
					+param3;
			System.out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test1(){
		int[] arr = new int[5];
		System.out.println(arr[1]);
		
	}

	public void NineXNineTest(int m){
		if(m == 1){
			System.out.println("1*1=1");
		}else{
			NineXNineTest(m-1);
			for(int i=1;i<=m;i++){
				System.out.print(i+"*"+m+"="+i*m+" ");
			}
			System.out.println();
		}
	}
	
	@Test
	public void test(){
		NineXNineTest(9);
	}
	@Test
	public void test2(){
		System.out.println(MyEnum.NumberTwo);;
	}
	
	@Test
	public void test3(){
		System.out.println('1' - '0');
	}
}
