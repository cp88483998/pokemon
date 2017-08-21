package pocket.pvp.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import pocket.total.util.JsonRead;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;

public class JsonServiceImpl implements JsonService{
	private final static JsonServiceImpl instance = new JsonServiceImpl();
	public static JsonServiceImpl getInstance(){
		return instance;
	}
	/*
	 * 解析player
	 */
	public Player parsePlayer(JSONObject jsonObject){
		JSONObject playerObj = jsonObject.getJSONObject("player");
		Player player = new Player();
		player.setUid(playerObj.getLong("uid"));
		player.setUsername(playerObj.getString("username"));
		player.setNickname(playerObj.getString("nickname"));
		player.setGender(playerObj.getInt("gender"));
		player.setLevel(playerObj.getInt("level"));
		player.setTrainer_head_id(playerObj.getInt("trainer_head_id"));
		player.setElves_head_id(playerObj.getInt("elves_head_id"));
		player.setFight(playerObj.getInt("fight"));
		player.setWinPoint(playerObj.getInt("winPoint"));
		player.setWinRate(playerObj.getDouble("winRate"));
		player.setWinRank(playerObj.getInt("dan"));
		player.setServerName(playerObj.getString("serverName"));
		
		
		JSONArray jaElves = jsonObject.getJSONArray("elves");
		//解析elve
		Map<String, RoleElves> roleElves = parseElve(jaElves);
		
		player.setRoleElves(roleElves);
		
//		RoleElves nowRoleElve = null;
//		for(String elveId : roleElves.keySet()){
//			nowRoleElve = roleElves.get(Integer.parseInt(elveId));
//		}
//		player.setNowRoleElves(nowRoleElve);
		return player;
	}
	

	/*
	 * 解析elve
	 */
	public Map<String, RoleElves> parseElve(JSONArray jaElves) {
		Map<String, RoleElves> roleElves = new LinkedHashMap<String, RoleElves>();
		for (int i = 0; i < jaElves.size(); i++) {
			JSONObject jsonObject = jaElves.getJSONObject(i).getJSONObject("elvesDataInforVo");
			String eLvesID = jsonObject.getString("eLvesID");
			
		    Elve elve = new Elve();
		    elve.seteLvesID(Integer.parseInt(eLvesID));
		    elve.setRarity(jsonObject.getInt("rarity"));
		    elve.seteLvesname(jsonObject.getString("eLvesname"));
		    elve.setCharacter(jsonObject.getInt("character"));
			elve.setExp(jsonObject.getInt("exp"));
		    elve.setHp(jsonObject.getInt("hp"));
		    elve.setSameIndex(jsonObject.getInt("sameIndex"));
			elve.setHpLevel(jsonObject.getInt("hpLevel"));
		    elve.setWuAttackLevel(jsonObject.getInt("wuAttackLevel"));
		    elve.setWuDefenseLevel(jsonObject.getInt("wuDefenseLevel"));
		    elve.setTeAttackLevel(jsonObject.getInt("teAttackLevel"));
		    elve.setTeDefenseLevel(jsonObject.getInt("teDefenseLevel"));
		    elve.setSpeedLevel(jsonObject.getInt("speedLevel"));
			elve.setCarryitem(jsonObject.getString("carryitem"));
			if(!jsonObject.getString("carryequip").equals("")){
				elve.setCarryequip(jsonObject.getString("carryequip"));
			}
			elve.setFeatures(jsonObject.getString("features"));
			elve.setTrainedLevel(jsonObject.getInt("trainedLevel"));
			elve.setBreakthroughLevel(jsonObject.getInt("breakthroughLevel"));
			elve.setLevel(jsonObject.getInt("level"));
			elve.setUpgradeLv(jsonObject.getInt("upgradeLv"));
			elve.setStudySkillList(jsonObject.getString("studySkillList"));
			elve.setGender(jsonObject.getInt("gender"));
			elve.setMega(jsonObject.getString("mega"));
			
			JSONObject jo = null;
			for(int j=0;j<StaticClass.Elves_data_js.size();j++){
				jo = StaticClass.Elves_data_js.getJSONObject(j);
				if(jo.getString("Elves_ID").equals(eLvesID)){
					elve.setElves_type(jo.getString("Elves_type"));
					break;
				}
			}
			
			String[] strs = jsonObject.getString("studySkillList").split(";");
			List<Integer> list = new ArrayList<Integer>();
			for(String s:strs){
				String[] ms = s.split(",");
				list.add(Integer.parseInt(ms[0]));
			}
			
			//解析skill
			Map<String, SkillDataVo> studySkills = parseSkill(list); 
			
		    Elves_value elves_value = JsonRead.ElvesValueFind(Integer.parseInt(eLvesID), StaticClass.elves_value_js);
		    RoleElves roleElve = new RoleElves();
		    roleElve.setElves_value(elves_value);
			roleElve.setStudySkills(studySkills);
			roleElve.setElve(elve);
			roleElve.setMaxHp(jsonObject.getInt("hp"));
			
			JSONArray randomListJA = jaElves.getJSONObject(i).getJSONArray("randomList");
//			List<Double> randomList = new ArrayList<Double>();
//			for(int j=0;j<randomListJA.size();j++){
//				randomList.add((Double) randomListJA.get(j));
//			}
			roleElve.setRandomList(randomListJA);
			
			roleElves.put(MyUtil.addElveId(elve), roleElve);
		}
		return roleElves;
	}
	
	/*
	 * 解析skill
	 */
	public Map<String, SkillDataVo> parseSkill(List<Integer> list){
		Map<String, SkillDataVo> skills = new HashMap<String, SkillDataVo>();
		for(int j=0;j<list.size();j++){
			int skillId = list.get(j);
			SkillDataVo skill = JsonRead.skillFind(skillId, StaticClass.skill_data_js);
			skills.put(String.valueOf(skillId), skill);
		}
		return skills;
	}
	
	
}
