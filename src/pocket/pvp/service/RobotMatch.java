package pocket.pvp.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Elves_value;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.total.util.Ability;
import pocket.total.util.JsonRead;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;
import scala.util.Random;

/**
 * 匹配机器人服务类
 * <p>Title: RobotMatch<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月19日
 */
public class RobotMatch {
	public static Player getRobot(Player player){
		Player playerMatch = new Player();
		
		/*
		 * 根据pvp_robot_js设置机器人等级
		 */
		int level = player.getLevel();
		JSONObject jo = null;
		String randomType = null;
		for(int i=0;i<StaticClass.pvp_robot_js.size();i++){
			jo = StaticClass.pvp_robot_js.getJSONObject(i);
			if(level >= jo.getInt("Player_min") && level <= jo.getInt("Player_max")){
				playerMatch.setLevel(jo.getInt("Robot_level"));	
				randomType = jo.getString("Random_type");
				break;
			}
		}
		
		/*
		 * 确定random_type
		 */
		Random random = new Random();
		int n = random.nextInt(100);
		String[] randomTypes = randomType.split(";");
		String group = null;
		String[] groups = null;
		int type = 1;
		for(int i=0;i<randomTypes.length;i++){
			group = randomTypes[i];
			groups = group.split(",");
			if(Integer.parseInt(groups[1]) >= n){
				type = Integer.parseInt(groups[0]);
				break;
			}
		}
		
		/*
		 * 根据Random_type读取pvp_ai_js生成对应的elve，突破值等级，特训值等级，个体值等级
		 */
		String elveIdStr = null;
		String btLevelStr = null;
		String tLevelStr = null;
		String viLevel = null;
		
		for(int i=0;i<StaticClass.pvp_ai_js.size();i++){
			jo = StaticClass.pvp_ai_js.getJSONObject(i);
			if(jo.getInt("Random_type") == type){
				elveIdStr = jo.getString("Elves_id");
				btLevelStr = jo.getString("breakthroughLevel");
				tLevelStr = jo.getString("trainedLevel");
				viLevel = jo.getString("VI_Level");
				break;
			}
		}
		String[] elveIdsStr = elveIdStr.split(",");
		List<String> elveIds = new ArrayList<String>();
		//随机6个精灵id
		for(int i=0;i<6;i++){
			n = random.nextInt(elveIdsStr.length);
			elveIds.add(elveIdsStr[n]);
		}
		int breakthroughLevel = getLevel(btLevelStr, playerMatch.getLevel());//突破值等级
		int trainedLevel = getLevel(tLevelStr, playerMatch.getLevel());//特训值等级
		int VI_Level = getLevel(viLevel, playerMatch.getLevel());//个体值等级
		
		/*
		 * 根据精灵ID读取elves_value_ai_js生成精灵数据
		 */
		//携带的精灵
		Map<String, RoleElves> roleElveMap = new LinkedHashMap<String, RoleElves>();
		for(int i=0;i<elveIds.size();i++){
			
			RoleElves roleElves = ElvesValueFind(elveIds.get(i), StaticClass.elves_value_ai_js, playerMatch.getLevel());
			
			Elve elve = roleElves.getElve();
			elve.setBreakthroughLevel(breakthroughLevel);
			elve.setTrainedLevel(trainedLevel);
			elve.setHpLevel(VI_Level);
			elve.setWuAttackLevel(VI_Level);
			elve.setWuDefenseLevel(VI_Level);
			elve.setTeAttackLevel(VI_Level);
			elve.setTeDefenseLevel(VI_Level);
			elve.setSpeedLevel(VI_Level);
			elve.setAddSpeedLv(VI_Level);
			elve.setHp(Ability.hpComp(roleElves));
			roleElveMap.put(elveIds.get(i), roleElves);
		}
		playerMatch.setRoleElves(roleElveMap);
		
		/*
		 * 性别
		 */
		int gender = random.nextInt(2)+1;
		playerMatch.setGender(gender);
		/*
		 * trainer_head_id
		 */
		int trainer_head_id = random.nextInt(3)+1;
		playerMatch.setTrainer_head_id(trainer_head_id);
		/*
		 * Nickname
		 */
		String nickName = getRanName();
		playerMatch.setNickname(nickName);
		/*
		 * serverName
		 */
		int size = StaticClass.server_name_js.size();
		int serverRan = random.nextInt(size);
		String serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
		while(serverName.equals(player.getServerName())){
			serverRan = random.nextInt(size);
			serverName = StaticClass.server_name_js.getJSONObject(serverRan).getString("serverName");
		}
		playerMatch.setServerName(serverName);
		/*
		 * 离线状态
		 */
		playerMatch.setOffline(true);
		/*
		 * 设置是否为机器人
		 */
		playerMatch.setRobot(true);
		return playerMatch;
	}
	/*
	 * 随机名字
	 */
	public static String getRanName(){
		String name = "";
		String[] surnames = StaticClass.player_namedate_js.getString("surname").split(",");
		String[] names = StaticClass.player_namedate_js.getString("name").split(",");
		Random random = new Random();
		int ran = random.nextInt(surnames.length);
		name += surnames[ran];
		ran = random.nextInt(names.length);
		name += names[ran];
		return name;
	}
	
	/*
	 * 获取等级
	 */
	private static int getLevel(String btLevelStr, int playerLv){
		String[] btLevels = btLevelStr.split(";");
		int level = 0;
		for(int i=0;i<btLevels.length;i++){
			String[] group = btLevels[i].split(",");
			if(Integer.parseInt(group[0]) == playerLv){
				level = Integer.parseInt(group[1]);
				break;
			}
		}
		return level;
	}
	
	/*
	 * 读取配置文件elves_value_ai_js,生成RoleElves
	 */
	private static RoleElves ElvesValueFind(String elvesID, JSONArray jsonArray, int level){
		Elves_value elves_value = new Elves_value();
		Elve elve = new Elve();
		String studySkillStr = null;
		Map<String, SkillDataVo> studySkills = null;
		for(int  i = 0; i < jsonArray.size(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(elvesID.equals(jsonObject.getString("Elves_id"))){
				elves_value.setValueID(Integer.parseInt(elvesID));
				elves_value.setHP(jsonObject.getInt("HP"));
				elves_value.setATK(jsonObject.getInt("ATK"));
				elves_value.setDEF(jsonObject.getInt("DEF"));
				elves_value.setINT(jsonObject.getInt("S-ATK"));
				elves_value.setMDF(jsonObject.getInt("S-DEF"));
				elves_value.setACT(jsonObject.getInt("Speed"));
				elves_value.setTotal_value(jsonObject.getInt("value"));
				elve.seteLvesID(Integer.parseInt(elvesID));
				elve.seteLvesname(jsonObject.getString("elves_name"));
				elve.setGender(jsonObject.getInt("gender"));
				elve.setRarity(Integer.parseInt(jsonObject.getString("rarity")));
				elve.setCharacter(getCharacter());
				studySkillStr = jsonObject.getString("Study_skill");
				studySkills = getStudySkills(level, studySkillStr);
				break;
			}
		}
		for(int i=0;i<StaticClass.Elves_data_js.size();i++){
			JSONObject jo = StaticClass.Elves_data_js.getJSONObject(i);
			if(jo.getString("Elves_ID").equals(elvesID)){
				elve.setElves_type(jo.getString("Elves_type"));
				break;
			}
		}
		RoleElves roleElves = new RoleElves();
		float[] randomList = MyUtil.ranFloatArr1(30);
		JSONArray randomListJA = JSONArray.fromObject(randomList);
		roleElves.setRandomList(randomListJA);
		roleElves.setElve(elve);
		roleElves.setElves_value(elves_value);
		roleElves.setStudySkills(studySkills);
		elve.setStudySkillList(getStudySkillList(roleElves));
		elve.setLevel(level);
		return roleElves;
	}
	/*
	 * 合成studySkillList
	 * 格式：skillId,pp;skillId,pp;
	 */
	public static String getStudySkillList(RoleElves roleElves){
		String studySkillList = "";
		Map<String ,SkillDataVo> map = roleElves.getStudySkills();
		SkillDataVo skill = null;
		for(String key : map.keySet()){
			skill = map.get(key);
			studySkillList += skill.getSkill_id()+","+skill.getPP()+";";
		}
		return studySkillList;
	}
	/*
	 * 随机性格 1~25
	 */
	public static int getCharacter(){
		Random random = new Random();
		int character = random.nextInt(25)+1;
		return character;
	}
	/*
	 * 根据玩家等级获取相应的技能
	 */
	public static Map<String, SkillDataVo> getStudySkills(int level, String studySkillStr){
//		System.out.println(studySkillStr);
		Map<String, SkillDataVo> studySkills = new LinkedHashMap<String, SkillDataVo>();
		String[] studySkillStrs = studySkillStr.split(":");
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<studySkillStrs.length;i++){
//			System.out.println("studySkillStrs[i] :"+studySkillStrs[i]);
			String[] group = studySkillStrs[i].split(",");
//			System.out.println("group :"+group);
			if(level >= Integer.parseInt(group[0])){
//				System.out.println("level >= group[0]");
				for(int j=1;j<group.length;j++){
//					System.out.println("list add");
					list.add(Integer.parseInt(group[j]));
				}
			}
		}
		List<Integer> list2 = new ArrayList<Integer>();
		if(list.size()>0){
//			System.out.println("list size :"+list.size());
			//限制技能只能有四个
			if(list.size() > 4 ){
				int startIndex = list.size() - 5;
				for(int i=list.size()-1;i>startIndex;i--){
					list2.add(list.get(i));
				}
			}else{
				list2 = list;
			}
			SkillDataVo skillDataVo = null;
			for(int i=0;i<list2.size();i++){
				skillDataVo = JsonRead.skillFind(list.get(i), StaticClass.skill_data_js);
				studySkills.put(skillDataVo.getSkill_id()+"", skillDataVo);
			}
		}
		return studySkills;
	}
}
