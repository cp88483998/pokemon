package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.relation.Role;

import org.junit.Test;
import org.python.modules.types;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Elves_value;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.RoundData;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.entity.SkillUseRecord;
import pocket.pvp.service.JsonService;
import pocket.pvp.service.JsonServiceImpl;
import pocket.pvp.service.PVPMatch;
import pocket.pvp.service.PVPMatchOld;
import pocket.pvp.service.RobotMatch;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;
import pocket.total.util.Ability;
import pocket.total.util.JsonRead;
import pocket.total.util.MyUtil;
import pocket.total.util.OfflinePrivateMsg;
import pocket.total.util.PVPResult;
import pocket.total.util.StaticClass;
import pocket.total.util.Verify;

public class TestCase {
	@Test
	public void skillAbility(){
		String skill_ability = "普通";
		String invalid = SkillService.invalidSkillAbility(skill_ability);
		String elves_type = "普通,鬼";
		String[] elves_types = elves_type.split(",");
		boolean result = true;
		for(int i=0;i<elves_types.length;i++){
			if(invalid.equals(elves_types[i])){
				result = false;
			}
		}
		System.out.println(result);
	}
	@Test 
	public void getTest(){
		JSONObject jo = new JSONObject();
		jo.put("url", "http://115.159.207.89/");
		jo.put("senderUid", "14884231051702");
		jo.put("recipientUid", "14884231051701");
		
		JSONArray jaResult = OfflinePrivateMsg.getOfflineMsg(jo);
		System.out.println(jaResult);
	}
	@Test
	public void judgeTest(){
		JSONObject jo = new JSONObject();
		jo.put("url", "http://115.159.207.89/");
		jo.put("recipientUid", "14884231051701");
		
		JSONObject joResult = OfflinePrivateMsg.hasOfflineMsg(jo);
		if(joResult != null){
			System.out.println(joResult);
		}
		
	}
	@Test
	public void sendTest(){
		JSONObject jo = new JSONObject();
		jo.put("url", "http://115.159.207.89/");
		jo.put("senderUid", "14884231051702");
		jo.put("recipientUid", "14884231051701");
		jo.put("context", "hello");
		jo.put("params", "1|F_G|test182,王路飞,70,15,2,0,2,2,24/4 05:39:12,58,14884231051702,1000315:2:68:girl;2000033:2:59:girl;1000282:2:53:girl;1000251:3:70:;2000032:2:49:girl;1000212:1:39:boy");
		
		JSONObject joResult = null;
		try {
			joResult = OfflinePrivateMsg.sendOfflineMsg(jo);
		} catch (Exception e) {
			System.out.println("请求超时！");
		}
		System.out.println(joResult);
	}
	@Test
	public void listTest(){
		List<String> list = new ArrayList<String>();
		list.add("123");
		list.add("456");
		list.add("123");
		list.add("789");
		list.add("901");
		System.out.println(list);
		JSONArray ja = JSONArray.fromObject(list);
		System.out.println(ja);
		List<String> list2 = new ArrayList<String>();
		for(int i=0;i<ja.size();i++){
			System.out.println(ja.get(i));;
			list2.add(ja.getString(i));
		}
		System.out.println("list2:"+list2);
		
	}
	
	@Test
	public void parseElve2(){
		JsonService jsonService = new JsonServiceImpl();
		Map<String, RoleElves> roleElves = jsonService.parseElve(StaticClass.elves_value_js);
		System.out.println(roleElves);
		
	}
	
	@Test
	public void parsePlayer(){
		JsonService jsonService = new JsonServiceImpl();
		Player player = jsonService.parsePlayer(StaticClass.player_data2_js);
		player.setStatus(1);
		System.out.println(player.getRoleElves());
		
//		JSONObject jo = JSONObject.fromObject(player);
//		System.out.println(jo.toString());
		
	}
	
	@Test
	public void parsePlayer2(){
		StaticClass.player_data2_js.put("roomId", 10001);
		System.out.println(StaticClass.player_data2_js.toString());
	}
	
	@Test
	public void elve2_type(){
		String elve2_type = "草";
		String[] types = elve2_type.split(",");
		System.out.println(types.length);
		for(int i=0;i<types.length;i++){
			System.out.println(types[i]);
		}
	}
	@Test
	public void random(){
		Random random = new Random();
		int a = random.nextInt(100);
		System.out.println(a);
		double b = (random.nextInt(17)+92)*0.01;
		System.out.println(b);
	}
	@Test
	public void number(){
		double a = 305.18320000000006;
		int b = 1;
		System.out.println(a*b);
	}
	@Test
	public void array(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(10);
		System.out.println(list.get(0));
		list.set(0, 100);
		System.out.println(list.get(0));
	}
	@Test
	public void jo(){
		
		JSONObject jo = new JSONObject();
		jo.put("skillId", 1);
		String joStr = jo.toString();
		System.out.println(joStr);
	}
	@Test
	public void concurrentHashMap(){
		Map<Integer, String> map = new ConcurrentHashMap<>();
		map.put(2, "1");
		map.put(1, "2");
		map.put(2, "3");
		System.out.println(map.toString());
	}
	@Test
	public void paeseInt(){
		String strCarryequip1 = "";
		String strCarryequip2 = "";
		if(!strCarryequip1.equals("")){
			int carryequip1= Integer.parseInt(strCarryequip1);
			System.out.println(1);
		}
		if(!strCarryequip2.equals("")){
			int carryequip2= Integer.parseInt(strCarryequip2);
			System.out.println(2);
		}
		
	}
	@Test
	public void elves_type(){
		System.out.println(Share.roomId);
		Share.roomId++;
		System.out.println(Share.roomId);
	}
	@Test
	public void baseSkill(){
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "1111");
		jsonObject.put("skillId", 2222);
		System.out.println(jsonObject.toString());
		JSONObject jo = new JSONObject();
		jo.put("BaseSkill", jsonObject);
		System.out.println(jo.toString());
	}
	@Test
	public void isHit(){
		SkillDataVo skill1 = new SkillDataVo();
		skill1.setHit(100);
	}
	@Test
	public void json(){
		JSONObject jo1 = new JSONObject();
		jo1.put("jo1", 111);
		JSONObject jo2 = new JSONObject();
		jo1.put("jo2", 222);
		JSONObject joAll = new JSONObject();
		joAll.putAll(jo1);
		joAll.putAll(jo2);
		joAll.put("new1", 3333);
		System.out.println(joAll);
		
		JSONObject newJo = new JSONObject();
		newJo.put("new", joAll);
		System.out.println(newJo);
	}
	@Test
	public void roundData(){
		RoundData roundData = new RoundData();
		roundData.setHit(false);
		roundData.setHitValue(1);
		roundData.setXkMultiple(1);
		roundData.setVital(false);
		roundData.setRivalDie(false);
		roundData.setOwnDie(false);
//		System.out.println(roundData.toString());
//		JSONObject jo = JSONObject.fromObject(roundData);
//		System.out.println(jo);
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.add(roundData);
//		System.out.println(jsonArray.toString());
		
		List<RoundData> list = new ArrayList<RoundData>();
		list.add(roundData);
		Player player = new Player();
		Map<Integer, List<RoundData>> map = player.getRoundDatas();
		map.put(1, list);
		map.put(2, list);
		map.put(3, list);
		map.put(4, list);
		System.out.println(map);
		System.out.println(player.getRoundDatas());
		JSONArray array = JSONArray.fromObject(list);
		System.out.println(array);
		
		int round = player.getRound();
		System.out.println(round);
		player.setRound(round+1);
		System.out.println(round);
	}
	@Test
	public void child_effect(){
		int a = 983;
		System.out.println(Math.round(1.122222*10000)/10000.0);
		int b = (int)Math.round(a);
		System.out.println(b);
		float c = (float)b / 100;
		System.out.println(Math.round(c));
	}
	@Test
	public void Str(){
		String str = "1,2";
		String[] arr = str.split(",");
		System.out.println(arr[0]);
		System.out.println(arr.length);
	}
	@Test
	public void jsonArray(){
		JSONArray jsonArray = new JSONArray();
		ISFSObject params = new SFSObject();
		params.putUtfString("Infor", jsonArray.toString());
		System.out.println(params.getUtfString("Infor"));
	}
	@Test
	public void jsonSkill(){
		JSONArray jo = StaticClass.skill_data_js;
		Object obj = null;
		for(int  i = 0; i < StaticClass.skill_data_js.size(); i++){
			JSONObject jsonObject = jo.getJSONObject(i);
			if(jsonObject.getInt("Skill_id") == 10182){
				obj = jsonObject.get("Vital");
				System.out.println("obj :"+obj);
			}
		}
		int i;
		if(obj instanceof Integer){
			i = Integer.parseInt(obj.toString());
		}else{
			i = 0;
		}
		System.out.println(i);
	}
	@Test
	public void skill_effect(){
		for(int i=0;i<StaticClass.skill_effect_js.size();i++){
			JSONObject jo = StaticClass.skill_effect_js.getJSONObject(i);
			if(Integer.parseInt(jo.getString("Skill_id")) == 10001){
				System.out.println(jo.getString("buff_rate").isEmpty());
				System.out.println(jo.getString("buff_rate")==null);
			}
		}
	}
	@Test
	public void str(){
		String str = "";
		String[] arr = str.split(";");
		System.out.println(str.isEmpty());
		System.out.println(str == null);
		System.out.println(arr == null);
		System.out.println(String.valueOf(112).equals(arr[0]));
	}
	@Test
	public void ranFloat(){
		
		float[] attackRandomArr = MyUtil.ranFloatArr1(10000);
//		float[] beAttackRandomArr = MyUtil.ranFloatArr(10);
		for(int i=0;i<attackRandomArr.length;i++){
			System.out.println(attackRandomArr[i]);
//			System.out.println(beAttackRandomArr[i]);
		}
		int n = 0;
		for(int i=0;i<attackRandomArr.length;i++){
			if(attackRandomArr[i] == 1){
				n++;
			}
		}
		System.out.println(n);
		int m = 0;
		for(int i=0;i<attackRandomArr.length;i++){
			if(attackRandomArr[i] == 0){
				m++;
			}
		}
		System.out.println(m);
	}
	@Test
	public void json1(){
		JSONObject jo1 = new JSONObject();
		jo1.put("1", 1);
		jo1.put("2", 2);
		JSONObject jo2 = new JSONObject();
		jo2.put("3", 3);
		jo2.put("4", 4);
		JSONArray ja = new JSONArray();
		ja.add(jo1);
		ja.add(jo2);
		System.out.println(ja.size());
		
		System.out.println(ja.get(0));
		
	}
	@Test
	public void addEffect(){
		RoleElves roleElves = new RoleElves();
		Child_Effect child_Effect = new Child_Effect();
		child_Effect.setTarget(112);
		System.out.println(roleElves.getChildEffectList() == null);
		if(roleElves.getChildEffectList() == null){
        	List<Child_Effect> list = new ArrayList<Child_Effect>();
        	list.add(child_Effect);
        }else{
        	roleElves.getChildEffectList().add(child_Effect);
        }
//		List<Child_Effect> list = new ArrayList<Child_Effect>();
//		roleElves.setChildEffectList(list);
//		roleElves.getChildEffectList().add(child_Effect);
//		roleElves.AddChildEffect(child_Effect, "11");
	}
	@Test
	public void equal(){
		String str1 = "";
		String str2 = "";
		System.out.println(str1.equals(str2));
	}
	@Test
	public void str1(){
		String params = "0sadas";
		System.out.println(params.charAt(0)-'0' == 0);
	}
//	@Test
//	public void nowRole(){
//		Elve elve = new Elve();
//		elve.seteLvesID(100001);
//		elve.setHp(100);
//		RoleElves roleElves = new RoleElves();
//		roleElves.setElve(elve);
//		Map<String,RoleElves> map = new HashMap<String,RoleElves>();
//		map.put("100001", roleElves);
//		Player player = new Player();
//		player.setRoleElves(map);
//		player.setNowRoleElves(roleElves);
//		
//		RoleElves nowRoleElves = player.getNowRoleElves();
//		nowRoleElves.getElve().setHp(0);
//		
//		System.out.println(player.getRoleElves().get("100001").getElve().getHp());
//		System.out.println(nowRoleElves.getElve().isDie());
//	}
	@Test
	public void hp(){
		Elve elve = new Elve();
		elve.setHp(16);
		System.out.println(elve.getIsDie());
	}
	@Test
	public void jsonFind(){
		for(int i=0;i<StaticClass.skill_data_js.size();i++){
			JSONObject jo = StaticClass.skill_data_js.getJSONObject(i);
			if(jo.getInt("Skill_id") == 10186){
				System.out.println(jo.getString("Vital"));
			}
		}
	}
	@Test
	public void curRound(){
		Elve elve1 = new Elve();
		elve1.seteLvesID(100001);
		elve1.setHp(100);
		Elve elve2 = new Elve();
		elve2.seteLvesID(100001);
		elve2.setHp(100);
		RoleElves roleElves1 = new RoleElves();
		RoleElves roleElves2 = new RoleElves();
		roleElves1.setElve(elve1);
		roleElves2.setElve(elve2);
		CurRound curRound = new CurRound();
		curRound.setNowRoleElves(roleElves1);
		System.out.println(curRound.getNowRoleElves());
		curRound.setBeChangeRoleElves(curRound.getNowRoleElves());
		System.out.println(curRound.getBeChangeRoleElves());
		curRound.setNowRoleElves(roleElves2);
		System.out.println(curRound.getNowRoleElves());
		System.out.println(curRound.getBeChangeRoleElves());
		
		System.out.println(curRound.getResultType());
		CurRound curRound2 = new CurRound();
		System.out.println(curRound2.getResultType());
		curRound2.setResultType(1);
		System.out.println(curRound2.getResultType());
	}
	
	@Test
	public void timeTest(){
		long time = System.currentTimeMillis()/1000;
		System.out.println(time);
	}
	
	@Test
	public void random2(){
		Random ran = new Random();
		System.out.println(ran.nextFloat()*11/10);
		double d = Math.random()*11;
		System.out.println(d);
		float e = (ran.nextInt(1000001))/1000000f;
		System.out.println(e);
	}
	@Test
	public void isDie(){
		Elve elve = new Elve();
		elve.setHp(71);
		System.out.println(elve.getIsDie());
	}
	@Test
	public void random1(){
		Random ran = new Random();
		for(int i=0;i<9;i++){
			System.out.println(ran.nextInt(2)+2);
		}
	}
	@Test
	public void mapTest(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "1");
		System.out.println(map.get(2));
		
		map.remove(3);
	}
	@Test
	public void jsonArray1(){
		JSONObject jo = new JSONObject();;
		jo.put("123", "");
		System.out.println(jo.get("123").equals(""));
		System.out.println(jo.size());
		System.out.println(jo.get("123"));
		
		JSONObject jo1 = new JSONObject();;
		jo1.put("jo", jo);
		
		System.out.println(jo1.get("jo"));
	}
	
	@Test
	public void jsonTest(){
		JSONObject jo1 = new JSONObject();
		jo1.put("1", 1);
		jo1.put("2", 2);
		jo1.put("3", 3);
		
		JSONObject jo2 = new JSONObject();
		jo2.put("4", 1);
		jo2.put("5", 2);
		jo2.put("6", 3);
		
		JSONObject joAll1 = new JSONObject();
		joAll1.put("jo1", jo1);
		joAll1.put("jo2", jo2);
		
		JSONObject joAll2 = new JSONObject();
		joAll2.put("jo1", jo1);
		joAll2.put("jo2", jo2);
		
		Iterator it1 = joAll1.keys();
		
		while(it1.hasNext()){
			String key1 = (String) it1.next(); 
			JSONObject j1 = joAll1.getJSONObject(key1);
			System.out.println("j1"+j1);
			Iterator it2 = joAll2.keys();
			while(it2.hasNext()){
				String key2 = (String) it2.next(); 
				JSONObject j2 = joAll2.getJSONObject(key2);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("j2"+j2);
			}
			
		}
	}
	@Test
	public void jsonArray2(){
		float[] randomList = MyUtil.ranFloatArr1(100);
		String infor = randomList.toString();
		System.out.println("infor " +infor);
		
		JSONArray randomListJA = JSONArray.fromObject(infor);
		System.out.println("randomListJA " +randomListJA);
	}
	@Test
	public void linkedHashMapTest(){
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(int i=0;i<10;i++){
			map.put(""+i+1, ""+i+10);
		}
		for(String key : map.keySet()){
			System.out.println(map.get(key));
		}
	}
	@Test
	public void jsonTest1(){
		SkillDataVo skillDataVo = new SkillDataVo();
		skillDataVo.setSkill_id(1000);
		skillDataVo.setSkill_name("a");
		
		List<SkillUseRecord> skillUseRecord = null;
		System.out.println(skillUseRecord.size());
		
		JSONObject jo = JSONObject.fromObject(skillDataVo);
		System.out.println(jo);
	}
	@Test
	public void elveTest(){
		Elve elve = new Elve();
		JSONObject jo = JSONObject.fromObject(elve);
		System.out.println(jo);
	}
	@Test
	public void string(){
		String params = "10";
		System.out.println(params.charAt(0));
		System.out.println(params.charAt(0)-'0');
		System.out.println(params.charAt(0)-'1' == 0);
		System.out.println(Math.abs((double)2/-4));
	}
	@Test
	public void result(){
		JSONObject playerJO1 = new JSONObject();
		JSONObject playerJO2 = new JSONObject();
		JSONArray playerJA = new JSONArray();
		
		playerJO1.put("uid", "14884231051702");
		playerJO1.put("isWin", false);
		playerJO1.put("url", "http://115.159.207.89/pvp/update");
		playerJA.add(playerJO1);
		playerJO2.put("uid", "14883590927084");
		playerJO2.put("isWin", true);
		playerJO2.put("url", "http://115.159.207.89/pvp/update");
		playerJA.add(playerJO2);
		
		JSONObject playerJO = null;
		JSONObject resultJO = null;
		JSONObject sendJO = null;
		for(int i=0;i<playerJA.size();i++){
			playerJO = playerJA.getJSONObject(i);
			resultJO = PVPResult.sendResult(playerJO);
			if(resultJO == null){
				for(int j=0;j<2;j++){
					resultJO = PVPResult.sendResult(playerJO);
					if(resultJO != null){
						break;
					}
				}
			}
			sendJO = new JSONObject();
			if(resultJO != null){
//				trace("resultJO :"+resultJO);
				JSONObject userJO = resultJO.getJSONObject("user");
				
				//战斗总场数
				int PVP_season = userJO.getInt("PVP_season");
				//胜利场数
				int PVP_season_win = userJO.getInt("PVP_season_win");
				//胜率
				double winRate = 0;
				if(PVP_season != 0){
					winRate = (double)PVP_season_win / PVP_season;
				}
				//胜点
				int winPoint = userJO.getInt("PVP_season_point");
				//段位
//				int winRank = winsRankCom(winPoint);
				int winRank = userJO.getInt("PVP_level");
				//段位是否提升
				boolean isWinRankUp = userJO.getBoolean("isUpgraded");
				sendJO.put("status", 0);//0：获取成功
				sendJO.put("PVPTimes", PVP_season);
				sendJO.put("winTimes", PVP_season_win);
				sendJO.put("winRate", Math.round(winRate*100)/100.0);
				sendJO.put("winPoint", winPoint);
				sendJO.put("winRank", winRank);
				sendJO.put("isWinRankUp", isWinRankUp);
				
			}else{
//				trace("not get and status : 1");
				sendJO.put("status", 1);//1：获取失败
				sendJO.put("PVPTimes", 0);
				sendJO.put("winTimes", 0);
				sendJO.put("winRate", 0);
				sendJO.put("winPoint", 0);
				sendJO.put("winRank", 0);
				sendJO.put("isWinRankUp", false);
			}
			if(playerJO.getString("uid").equals("14884231051702")){
				System.out.println(resultJO);
			}
			else if(playerJO.getString("uid").equals("14883590927084")){
				System.out.println(resultJO);
			}
		}
	}
	@Test
	public void minByValue(){
		Map<String, Double> map = new HashMap<String, Double>();
		
		map.put("1", 1.0);
		map.put("2", 2.0);
		map.put("3", 3.0);
		map.put("4", 4.0);
		map.put("5", 5.0);
		map.put("0.1", 0.1);
		map.put("6", 6.0);
		
		System.out.println(MinByValue(map));;
	}
	@Test
	public void doubleFor(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		for(int i=0;i<list.size();i++){
			int a = list.get(i);
			list.remove(i);
			if(a > 3){
				System.out.println("OK");
			}else{
				list.add(a);
				System.out.println("NO");
			}
		}
	}
	@Test
	public void matchTest(){
		List<Player> list = new ArrayList<Player>();
		Player player = new Player();
		player.setUid(1000000001L);
		list.add(player);
		
		Player player2 = list.get(0);
		System.out.println(player2);
		
		list.remove(0);
		System.out.println(player2);
		int levelDiffer = Math.abs(71 - 70);
		if(levelDiffer <= 5){
			System.out.println(levelDiffer);
		}
		Share.matchPool.put(1, list);
		Share.matchPool.remove(1);
		System.out.println(Share.matchPool.get(1) == null);
		
	}
	@Test
	public void randomType(){
		
		Random random = new Random();
		int n = random.nextInt(100);
		System.out.println(n);
		
		String randomType = "1,100;2,0;3,0";
		String[] randomTypes = randomType.split(";");
		System.out.println(randomTypes);
		String group = null;
		String[] groups = null;
		int type = 1;
		for(int i=0;i<randomTypes.length;i++){
			group = randomTypes[i];
			groups = group.split(",");
			System.out.println(groups[1]);
			if(Integer.parseInt(groups[1]) >= n){
				type = Integer.parseInt(groups[0]);
			}
		}
		System.out.println("type :"+type);
	}
	@Test
	public void robotTest(){
		Player player = new Player();
		player.setLevel(40);
		Player playerMatch = RobotMatch.getRobot(player);
		Map<String ,RoleElves> roleElves = playerMatch.getRoleElves();
		for(String key : roleElves.keySet()){
			Elve elve = roleElves.get(key).getElve();
			System.out.println(elve.toString());
			Map<String, SkillDataVo> skills = roleElves.get(key).getStudySkills();
			for(String key2 : skills.keySet()){
				SkillDataVo skill = skills.get(key2);
				System.out.println(skill.toString());
			}
		}
	}
	@Test
	public void trainValueCom(){
		JSONArray ja = new JSONArray();
		System.out.println(ja.isEmpty());
		RoleElves roleElves = new RoleElves();
		List<Child_Effect> list = ja;
		Child_Effect child_Effect;
		for(int i=0;i<list.size();i++){
			child_Effect = list.get(i);
			if(child_Effect.getId()==107){//速度%+-
				System.out.println(107);
			}
			if(child_Effect.getId()==116){//速度等级+-
				System.out.println(106);
			}
		}
	}
	@Test
	public void randomTest(){
		Random random = new Random();
		int serverRan = random.nextInt(3);
		String serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
		while(serverName.equals("S1 Pikachu")){
			serverRan = random.nextInt(3);
			serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
			System.out.println(serverName);
		}
		System.out.println("final :"+serverName);
		
		float[] randomList = MyUtil.ranFloatArr1(100);
		JSONArray randomListJA = JSONArray.fromObject(randomList);
		System.out.println(randomListJA);
	}
	@Test
	public void listSizeTest(){
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		for(int i=0;i<100000;i++){
			list.add(i);
		}
		//限制技能只能有四个
		if(list.size() > 4 ){
			System.out.println(list.size());
			int startIndex = list.size() - 5;
			System.out.println("startIndex :"+startIndex);
			for(int i=list.size()-1;i>startIndex;i--){
				list2.add(list.get(i));
			}
		}
		System.out.println(list2.size());
	}
	@Test
	public void mapTest1(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, null);
		for(int key : map.keySet()){
			System.out.println(map.get(key));
		}
	}
	@Test
	public void getSkillTest(){
		Map<String, SkillDataVo> map = RobotMatch.getStudySkills(40, "0,10010:18,10093:28,10039:31,10050:48,10154:59,10056");
		System.out.println("size :"+map.size());
	}
	@Test
	public void urlTest(){
		System.out.println(Share.serverURL.get(0+""));
	}
	
	public static String MinByValue(Map<String, Double> map){
		if (map == null || map.isEmpty()) {  
            return null;  
        }  
		double a = 0;
		double b = 0;
		String player = null;
        for(String key : map.keySet()){
        	player = key;
        	a = map.get(key);
        }
    	for(String key : map.keySet()){
    		b = map.get(key);
    		if(a > b){
    			a = b;
    			player = key;
    		}
    	}
        return player;
	}
}

