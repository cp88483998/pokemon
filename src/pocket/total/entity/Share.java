package pocket.total.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.smartfoxserver.v2.entities.User;

import pocket.chat.entity.Message;
import pocket.pvp.entity.Player;
import pocket.timeSpace.entity.Boss;
import pocket.timeSpace.entity.BossPlayer;


public class Share {
	public static List<Player> playerList = new ArrayList<Player>();
	
	//各个段位对应的匹配池
	public static Map<Integer, List<Player>> matchPool = new ConcurrentHashMap<Integer, List<Player>>();
	
	//匹配结果map
	public static Map<String, List<Player>> matchMap = new ConcurrentHashMap<String, List<Player>>();
	
	public static int roomId = 1;
	
	//离线公共消息
	public static Map<Integer, List<Message>> offline_public_msg = new ConcurrentHashMap<Integer, List<Message>>();
	
	//uid对应的服务器地址
	public static Map<String, String> serverURL = new HashMap<String, String>();
	
	//Boss哈希表,elveId:hp
	public static Map<String, Boss> bossMap = new LinkedHashMap<String, Boss>();
	
	//存储正在时空缝隙副本中的玩家集合
	public static List<User> inBossPlayer = new LinkedList<User>();
	public static long battleId = 0;
	public static Map<String, BossPlayer> inBossPlayer2 = new HashMap<String, BossPlayer>();
	
	
	//存储开始时间，用在自动发系统消息
	public static long startTime = System.currentTimeMillis()/1000;
	//存储时间间隔
	public static int intervalTime = 0;	
	//存储系统消息自动发送次数
	public static int sendCount = 0;
	
	public static Properties p = new Properties();	
	
	static{
		// 读取配置文件
		try {
			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	static{
		
//		//初始化boss哈希表
//		JSONArray ja = StaticClass.boss_data_js;
//		JSONObject jo = null;
//		int HP = 0;
//		double trValue = 0;
//		double btValue = 0;
//		int hpLevel = 0;
//		int lv = 0;
//		double HP_Ability = 0;
//		String eLvesId = null;
//		long hp = 0;
//		for(int i=0;i<ja.size();i++){
//			jo = ja.getJSONObject(i);
//			HP = jo.getInt("HP");
//			trValue = Ability.trainValue_Com(jo.getInt("trainedLevel"));
//			btValue = Ability.btVlaue_Com(jo.getInt("breakthroughLevel"));
//			hpLevel = jo.getInt("hpLevel");
//			lv = jo.getInt("level");
//			HP_Ability = 24*((HP*trValue+btValue+hpLevel/2+100)*lv/100+10);
//			eLvesId = jo.getInt("eLvesid")+"";
//			hp = Math.round(HP_Ability*1000);
//			Boss boss = new Boss();
//			boss.setElvesId(eLvesId);
//			boss.setMaxHp(hp);
//			boss.setNowHp(hp);;
//			bossMap.put(eLvesId, boss);
//		}
		
//	}
}
