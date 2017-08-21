package pocket.pvp.service;

import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.entity.SkillUseRecord;
import pocket.total.entity.Share;

public class FightingDataService {
	/*
	 * 断线重连返回数据
	 */
	public static JSONObject getFightingData(User user, String roomId){
		
		Player player1 = null;
		Player player2 = null;
		List<Player> list = Share.matchMap.get(roomId);
		for(int i=0;i<list.size();i++){
			if(list.get(i).isRobot()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getName().equals(user.getName())){
				player1 = list.get(i);
			}else{
				player2 = list.get(i);
			}
		}
		
		JSONObject jo1 = parsePlayerInfor(player1);
		
		JSONObject jo2 = parsePlayerInfor(player2);
		
		
		JSONObject joAll = new JSONObject();
		joAll.put("roomId", Integer.parseInt(roomId));
		joAll.put("ownBattlePlayerInfor", jo1);
		joAll.put("rivalBattlePlayerInfor", jo2);
		if(player2.getCurRound().getWeatherInfor()!=null){
			joAll.put("weatherInfor", player2.getCurRound().getWeatherInfor());
		}else{
			joAll.put("weatherInfor", "");
		}
		
		//换user
		player1.setUser(user);
		//更改离线状态
		player1.setOffline(false);
		
		
		return joAll;
	}
	
	public static JSONObject parsePlayerInfor(Player player){
		
		Map<String, RoleElves> roleElves = player.getRoleElves();
		
		JSONObject jo = new JSONObject();
		JSONArray JARoleElves = new JSONArray();
		for(String key : roleElves.keySet()){
			RoleElves roleElve = roleElves.get(key);
			JSONObject joRoleElves = new JSONObject();
			
			joRoleElves.put("elvesDataInforVo", roleElve.getElve());
			joRoleElves.put("childEffectList", roleElve.getChildEffectListJA());
			joRoleElves.put("stateSkillEffectList", roleElve.getStateSkillEffectListJA());
			joRoleElves.put("randomList", roleElve.getRandomList());
			joRoleElves.put("randomIndex", roleElve.getRandomIndex());
			joRoleElves.put("skillUseRecord", parseSkillUseRecords(roleElve.getSkillUseRecord()));
			joRoleElves.put("isFirstSkill", roleElve.isFirstSkill());
			joRoleElves.put("wuHitValue", roleElve.getWuHitValue());
			joRoleElves.put("hitValue", roleElve.getHitValue());
			
			JARoleElves.add(joRoleElves);
		}
		Elve nowElve = null;
		if(player.getRound()!=1 && player.getCurRound().getBeChangeRoleElves()!=null){
			nowElve = player.getCurRound().getBeChangeRoleElves().getElve();
		}else{
			nowElve = player.getCurRound().getNowRoleElves().getElve();
		}
		String nowElveId = nowElve.geteLvesID()+"_"+nowElve.getGender()+"_"+nowElve.getCharacter()+"_"+nowElve.getSameIndex();
		jo.put("player", parsePlayer(player));
		jo.put("roleElvesInforList", JARoleElves);
		jo.put("curBattleElvesId", nowElveId);
		return jo;
	}
	
	/*
	 * 匹配到对手后，将玩家信息解析到JSONObject
	 */
	public static JSONObject playerToJo(Player player, int roomId){
		Map<String, RoleElves> roleElves = player.getRoleElves();
		Elve elve = null;
		JSONArray randomList = null;
		JSONObject elvesJO = new JSONObject();
		
		JSONObject jo = new JSONObject();
		//携带精灵数组
		JSONArray JsonElves = new JSONArray();
		for(String key : roleElves.keySet()){
			elve = roleElves.get(key).getElve();
			randomList = roleElves.get(key).getRandomList();
			elvesJO.put("elvesDataInforVo", elve);
			elvesJO.put("randomList", randomList);
			JsonElves.add(elvesJO);
		}
		//玩家信息
		JSONObject jsonPlayer = FightingDataService.parsePlayer(player);;
		jo.put("player", jsonPlayer);
		jo.put("elves", JsonElves);
		jo.put("roomId", roomId);
		return jo;
	}
	
	public static JSONObject parsePlayer(Player player){
		JSONObject jo = new JSONObject();
		jo.put("uid", player.getUid());
		jo.put("username", player.getUsername());
		jo.put("nickname", player.getNickname());
		jo.put("gender", player.getGender());
		jo.put("level", player.getLevel());
		jo.put("trainer_head_id", player.getTrainer_head_id());
		jo.put("elves_head_id", player.getElves_head_id());
		jo.put("fight", player.getFight());
		jo.put("winPoint", player.getWinPoint());
		jo.put("winRate", player.getWinRate());
		jo.put("dan", player.getWinRank());
		jo.put("serverName", player.getServerName());
		
		return jo;
	}
	
	public static JSONArray parseSkillUseRecords(List<SkillUseRecord> skillUseRecords){
		JSONArray skillDataVoJA = new JSONArray();
		if(skillUseRecords != null){
			for(int i=0;i<skillUseRecords.size();i++){
				JSONObject jo = new JSONObject();
				jo.put("isHit", skillUseRecords.get(i).isHit());
				jo.put("isEffect", skillUseRecords.get(i).isEffect());
				jo.put("skillDataVo", parseSkill(skillUseRecords.get(i).getSkillDataVo()));
				skillDataVoJA.add(jo);
			}
		}
		
		return skillDataVoJA;
	}
	
	public static JSONObject parseSkill(SkillDataVo skillDataVo){
		JSONObject jo = new JSONObject();
		jo.put("Skill_number", skillDataVo.getSkill_number());
		jo.put("Skill_name", skillDataVo.getSkill_name());
		jo.put("Skill_id", skillDataVo.getSkill_id());
		jo.put("Skill_ability", skillDataVo.getSkill_ability());
		jo.put("Skill_type", skillDataVo.getSkill_type());
		jo.put("ATK_tpye", skillDataVo.getATK_tpye());
		jo.put("species", skillDataVo.getSpecies());
		jo.put("Skill_power", skillDataVo.getSkill_power());
		jo.put("PP", skillDataVo.getPP());
		jo.put("Hit", skillDataVo.getHit());
		jo.put("Priority", skillDataVo.getPriority());
		jo.put("Target", skillDataVo.getTarget());
		jo.put("Target_number", skillDataVo.getTarget_number());
		jo.put("ATK_time", skillDataVo.getATK_time());
		jo.put("Ratio", skillDataVo.getRatio());
		jo.put("Death", skillDataVo.getDeath());
		jo.put("Vital", skillDataVo.getVital());
		jo.put("takeArr", skillDataVo.getTakeArr());
		jo.put("takeValueArr", skillDataVo.getTakeValueArr());
		jo.put("textArr", skillDataVo.getTextArr());
		jo.put("Skill_text", skillDataVo.getSkill_text());
		jo.put("round", skillDataVo.getRound());
		
		return jo;
	}
	
	public static JSONObject conBattleRoundResult(Player player1, Player player2){
		JSONArray battleRoundResult = player2.getBattleRoundResult();
//		JSONArray battleRoundResult = player2.getBattleRoundResultMap().get(player2.getRound());
		
		CurRound curRound1 = player1.getCurRound();
		CurRound curRound2 = player2.getCurRound();
		Map<String, SkillDataVo> skillMap = curRound1.getNowRoleElves().getStudySkills();
		JSONObject ownSkill = null;
		for(String key : skillMap.keySet()){
			if(skillMap.get(key).getSkill_id() == curRound1.getNowSkill().getSkill_id()){
				ownSkill = SkillService.getBaseSkill(player1, player2, curRound1.getNowSkill());
				break;
			}
		}
		if(ownSkill == null){
			ownSkill = SkillService.getBaseSkill2(curRound1, curRound2, curRound1.getNowSkill().getSkill_id());
		}
		Elve elve1 = null;
		if(curRound2.getNowSkill().getSkill_id()==10018){
			if(curRound1.getBeChangeRoleElves()!=null){
				elve1 = curRound1.getBeChangeRoleElves().getElve();
			}else{
				elve1 = curRound1.getNowRoleElves().getElve();
			}
			if(elve1.geteLvesID() != curRound1.getNowRoleElves().getElve().geteLvesID()){
				for(int i=0;i<battleRoundResult.size();i++){
					JSONObject jo = battleRoundResult.getJSONObject(i);
					if(jo.getString("skillId").equals("10018")){
						Elve nowElve = curRound1.getNowRoleElves().getElve();
						String changeElvesId = nowElve.geteLvesID()+"_"+nowElve.getGender()+"_"+nowElve.getCharacter()+"_"+nowElve.getSameIndex();
						jo.put("changeElvesId", changeElvesId);
					}
				}
			}	
		}else{
			elve1 = curRound1.getNowRoleElves().getElve();
		}
		String ownElvesKey = elve1.geteLvesID()+"_"+elve1.getGender()+"_"+elve1.getCharacter()+"_"+elve1.getSameIndex();
		
		
		skillMap = curRound2.getNowRoleElves().getStudySkills();
		JSONObject rivalSkill = null;
		for(String key : skillMap.keySet()){
			if(skillMap.get(key).getSkill_id() == curRound2.getNowSkill().getSkill_id()){
				rivalSkill = SkillService.getBaseSkill(player2, player1, curRound2.getNowSkill());
				break;
			}
		}
		if(rivalSkill == null){
			rivalSkill = SkillService.getBaseSkill2(curRound2, curRound1, curRound2.getNowSkill().getSkill_id());
		}
		Elve elve2 = null;
		if(curRound1.getNowSkill().getSkill_id()==10018){
			if(curRound2.getBeChangeRoleElves()!=null){
				elve2 = curRound2.getBeChangeRoleElves().getElve();
			}else{
				elve2 = curRound2.getNowRoleElves().getElve();
			}
			if(elve2.geteLvesID() != curRound2.getNowRoleElves().getElve().geteLvesID()){
				for(int i=0;i<battleRoundResult.size();i++){
					JSONObject jo = battleRoundResult.getJSONObject(i);
					if(jo.getString("skillId").equals("10018")){
						Elve nowElve = curRound2.getNowRoleElves().getElve();
						String changeElvesId = nowElve.geteLvesID()+"_"+nowElve.getGender()+"_"+nowElve.getCharacter()+"_"+nowElve.getSameIndex();
						jo.put("changeElvesId", changeElvesId);
					}
				}
			}
		}else{
			elve2 = curRound2.getNowRoleElves().getElve();
		}
		String rivalElvesKey = elve2.geteLvesID()+"_"+elve2.getGender()+"_"+elve2.getCharacter()+"_"+elve2.getSameIndex();
		
		JSONObject jo = new JSONObject();
		jo.put("ownSkill", ownSkill);
		jo.put("rivalSkill", rivalSkill);
		jo.put("ownElvesKey", ownElvesKey);
		jo.put("rivalElvesKey", rivalElvesKey);
		jo.put("ownRandomIndex", curRound1.getNowRoleElves().getRandomIndex());
		jo.put("rivalRandomIndex", curRound2.getNowRoleElves().getRandomIndex());
		jo.put("battleRoundResult", battleRoundResult);
		
		return jo;
	}
	
}
