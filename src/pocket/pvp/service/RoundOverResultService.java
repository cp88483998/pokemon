package pocket.pvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.child_effect.Child_Effect_Factory;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.entity.SkillUseRecord;
import pocket.pvp.skill_species.Species;
import pocket.pvp.skill_species.SpeciesType;
import pocket.total.entity.Share;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;

public class RoundOverResultService {
	
	/*
	 * 更新双方玩家属性
	 */
	public static void updateRoleElves(Player player1, Player player2){
		JSONObject joRound1 = player1.getJoRound();
		
		JSONObject joWea = null;
		if(!joRound1.get("weatherInfor").equals("")){
			joWea = joRound1.getJSONObject("weatherInfor");
		}
		
		JSONObject elves1Info = joRound1.getJSONObject("elves1").getJSONObject("elvesDataInforVo");
		JSONArray elves1Effect = FilterChildEffect(joRound1.getJSONObject("elves1").getJSONArray("childEffectList"));
		JSONArray elves1Buff = joRound1.getJSONObject("elves1").getJSONArray("stateSkillEffectList");
		JSONObject elves1Skill = joRound1.getJSONObject("elves1").getJSONObject("skillInfor");
		JSONArray elves1Record = joRound1.getJSONObject("elves1").getJSONArray("skillUseRecord");
		boolean isHit1 = false;
		int randomIndex1 = joRound1.getJSONObject("elves1").getInt("randomIndex");
		boolean isFirstSkill1 = joRound1.getJSONObject("elves1").getBoolean("isFirstSkill");
		long wuHitValue1 = joRound1.getJSONObject("elves1").getLong("wuHitValue");
		int hitValue1 = joRound1.getJSONObject("elves1").getInt("hitValue");
		
		JSONObject elves2Info = joRound1.getJSONObject("elves2").getJSONObject("elvesDataInforVo");
		JSONArray elves2Effect = FilterChildEffect(joRound1.getJSONObject("elves2").getJSONArray("childEffectList"));
		JSONArray elves2Buff = joRound1.getJSONObject("elves2").getJSONArray("stateSkillEffectList");
		JSONObject elves2Skill = joRound1.getJSONObject("elves2").getJSONObject("skillInfor");
		JSONArray elves2Record = joRound1.getJSONObject("elves2").getJSONArray("skillUseRecord");
		boolean isHit2 = false;
		int randomIndex2 = joRound1.getJSONObject("elves2").getInt("randomIndex");
		boolean isFirstSkill2 = joRound1.getJSONObject("elves2").getBoolean("isFirstSkill");
		long wuHitValue2 = joRound1.getJSONObject("elves2").getLong("wuHitValue");
		int hitValue2 = joRound1.getJSONObject("elves2").getInt("hitValue");
		
		String studySkillList1 = null;
		String studySkillList2 = null;
		//更新天气
		if(joWea!=null){
//			player1.setWeatherInfor(joWea);
//			player2.setWeatherInfor(joWea);
			player1.getCurRound().setWeatherInfor(joWea);
			player2.getCurRound().setWeatherInfor(joWea);
		}
		
		//更新精灵的hp，子效果，buff
		RoleElves attackRoleElves = player1.getCurRound().getNowRoleElves();
		RoleElves beAttackRoleElves = player2.getCurRound().getNowRoleElves();
		Elve elve1 = attackRoleElves.getElve();
		Elve elve2 = beAttackRoleElves.getElve();
		if(Integer.parseInt(elves1Info.getString("eLvesID")) == elve1.geteLvesID() 
				&& elves1Info.getInt("gender")==elve1.getGender()
				&& elves1Info.getInt("character")==elve1.getCharacter()
				&& elves1Info.getInt("sameIndex")==elve1.getSameIndex()){
			//更新hp
			elve1.setHp(elves1Info.getInt("hp"));
			isHit1 = elves1Skill.getBoolean("isHit");
			studySkillList1 = elves1Info.getString("studySkillList");
			//更新命中
			player1.getCurRound().setHit(isHit1);
			//更新子效果
			attackRoleElves.setChildEffectListJA(elves1Effect);
			//更新子效果列表
			updateChildEffectList(elves1Effect, attackRoleElves);
			//更新buff
			attackRoleElves.setStateSkillEffectListJA(elves1Buff);
			//更新技能
			attackRoleElves.getElve().setStudySkillList(studySkillList1);
			//更新randomIndex
			attackRoleElves.setRandomIndex(randomIndex1);
			//更新skillUseRecord
			updateRecords(attackRoleElves, elves1Record);
			//更新isFirstSkill
			attackRoleElves.setFirstSkill(isFirstSkill1);
			//更新wuHitValue
			attackRoleElves.setWuHitValue(wuHitValue1);
			//更新hitValue
			attackRoleElves.setHitValue(hitValue1);
			//更新技能pp值
			updateSkillPP(studySkillList1, attackRoleElves);
		}
		if(Integer.parseInt(elves2Info.getString("eLvesID")) == elve1.geteLvesID() 
				&& elves2Info.getInt("gender")==elve1.getGender()
				&& elves2Info.getInt("character")==elve1.getCharacter()
				&& elves2Info.getInt("sameIndex")==elve1.getSameIndex()){
			elve1.setHp(elves2Info.getInt("hp"));
			isHit1 = elves2Skill.getBoolean("isHit");
			studySkillList1 = elves2Info.getString("studySkillList");
			
			player1.getCurRound().setHit(isHit1);
			attackRoleElves.setChildEffectListJA(elves2Effect);
			updateChildEffectList(elves2Effect, attackRoleElves);
			attackRoleElves.setStateSkillEffectListJA(elves2Buff);
			attackRoleElves.getElve().setStudySkillList(studySkillList1);
			attackRoleElves.setRandomIndex(randomIndex2);
			updateRecords(attackRoleElves, elves2Record);
			attackRoleElves.setFirstSkill(isFirstSkill2);
			attackRoleElves.setWuHitValue(wuHitValue2);
			attackRoleElves.setHitValue(hitValue2);
			updateSkillPP(studySkillList1, attackRoleElves);
		}
		if(Integer.parseInt(elves1Info.getString("eLvesID")) == elve2.geteLvesID() 
				&& elves1Info.getInt("gender")==elve2.getGender()
				&& elves1Info.getInt("character")==elve2.getCharacter()
				&& elves1Info.getInt("sameIndex")==elve2.getSameIndex()){
			elve2.setHp(elves1Info.getInt("hp"));
			isHit2 = elves1Skill.getBoolean("isHit");
			studySkillList2 = elves1Info.getString("studySkillList");
			
			player2.getCurRound().setHit(isHit2);
			beAttackRoleElves.setChildEffectListJA(elves1Effect);
			updateChildEffectList(elves1Effect, beAttackRoleElves);
			beAttackRoleElves.setStateSkillEffectListJA(elves1Buff);
			beAttackRoleElves.getElve().setStudySkillList(studySkillList2);	
			beAttackRoleElves.setRandomIndex(randomIndex1);
			updateRecords(beAttackRoleElves, elves1Record);
			beAttackRoleElves.setFirstSkill(isFirstSkill1);
			beAttackRoleElves.setWuHitValue(wuHitValue1);
			beAttackRoleElves.setHitValue(hitValue1);
			updateSkillPP(studySkillList2, beAttackRoleElves);
		}
		if(Integer.parseInt(elves2Info.getString("eLvesID")) == elve2.geteLvesID() 
				&& elves2Info.getInt("gender")==elve2.getGender()
				&& elves2Info.getInt("character")==elve2.getCharacter()
				&& elves2Info.getInt("sameIndex")==elve2.getSameIndex()){
			elve2.setHp(elves2Info.getInt("hp"));
			isHit2 = elves2Skill.getBoolean("isHit");
			studySkillList2 = elves2Info.getString("studySkillList");
			
			player2.getCurRound().setHit(isHit2);
			beAttackRoleElves.setChildEffectListJA(elves2Effect);
			updateChildEffectList(elves2Effect, beAttackRoleElves);
			beAttackRoleElves.setStateSkillEffectListJA(elves2Buff);
			beAttackRoleElves.getElve().setStudySkillList(studySkillList2);	
			beAttackRoleElves.setRandomIndex(randomIndex2);
			updateRecords(beAttackRoleElves, elves2Record);
			beAttackRoleElves.setFirstSkill(isFirstSkill2);
			beAttackRoleElves.setWuHitValue(wuHitValue2);
			beAttackRoleElves.setHitValue(hitValue2);
			updateSkillPP(studySkillList2, beAttackRoleElves);
		}
	}
	/*
	 * 更新子效果
	 */
	public static void updateChildEffectList(JSONArray chileEffectJA, RoleElves roleElves){
		JSONObject jo = null;
		int effectId ;
		int target;
		String targetValue;
		List<Child_Effect> list = new ArrayList<Child_Effect>();
		Child_Effect_Factory cef = new Child_Effect_Factory();
		Child_Effect child_Effect = null;
		for(int i=0;i<chileEffectJA.size();i++){
			jo = chileEffectJA.getJSONObject(i);
			effectId = jo.getInt("id");
			if(effectId <= 120 && effectId >= 101){
				target = jo.getInt("target");
				targetValue = jo.getString("targetValue");
				child_Effect = cef.create(effectId+"", target, targetValue);
				list.add(child_Effect);
			}
		}
		roleElves.setChildEffectList(list);
	}
	
	/*
	 * 更新技能pp值
	 */
	public static void updateSkillPP(String studySkillListStr, RoleElves roleElves){
		String[] studySkillLists = studySkillListStr.split(";");
		String[] studySkillList = null;
		String skillId = null;
		int skillPP = 0;
		Map<String, SkillDataVo> studySkillsMap = roleElves.getStudySkills();
		for(int i=0;i<studySkillLists.length;i++){
			studySkillList = studySkillLists[i].split(",");
			skillId = studySkillList[0];
			skillPP = Integer.parseInt(studySkillList[1]);
			studySkillsMap.get(skillId).setPP(skillPP);;
		}
	}
	
	/*
	 * 更新技能使用记录
	 */
	public static void updateRecords(RoleElves roleElves, JSONArray recordsJA){
		JSONObject joRecord = null;
		List<SkillUseRecord> skillUseRecords = new ArrayList<SkillUseRecord>();
		
		for(int i=0;i<recordsJA.size();i++){
			joRecord = recordsJA.getJSONObject(i);	
			boolean isHit = joRecord.getBoolean("isHit");
			boolean isEffect = joRecord.getBoolean("isEffect");
			
			JSONObject skillDataVoJO = joRecord.getJSONObject("skillDataVo");
			//封装SkillDataVo
			SkillDataVo skillDataVo = new SkillDataVo();
			skillDataVo.setSkill_number(skillDataVoJO.getInt("Skill_number"));
			skillDataVo.setSkill_name(skillDataVoJO.getString("Skill_name"));
			skillDataVo.setSkill_id(Integer.parseInt(skillDataVoJO.getString("Skill_id")));
			skillDataVo.setSkill_ability(skillDataVoJO.getString("Skill_ability"));
			skillDataVo.setSkill_type(skillDataVoJO.getString("Skill_type"));
			skillDataVo.setATK_tpye(skillDataVoJO.getString("ATK_tpye"));
			skillDataVo.setSpecies(skillDataVoJO.getString("species"));
			skillDataVo.setSkill_power(skillDataVoJO.getInt("Skill_power"));
			skillDataVo.setPP(skillDataVoJO.getInt("PP"));
			skillDataVo.setHit(skillDataVoJO.getInt("Hit"));
			skillDataVo.setPriority(skillDataVoJO.getInt("Priority"));
			skillDataVo.setTarget(skillDataVoJO.getInt("target"));
			skillDataVo.setTarget_number(skillDataVoJO.getInt("target_number"));
			skillDataVo.setATK_time(skillDataVoJO.getString("ATK_time"));
			skillDataVo.setRatio(skillDataVoJO.getString("Ratio"));
			skillDataVo.setRate(skillDataVoJO.getInt("Rate"));
			skillDataVo.setDeath(skillDataVoJO.getInt("Death"));
			skillDataVo.setVital(MyUtil.parseYesOrNo(skillDataVoJO.getBoolean("Vital")));
			skillDataVo.setTakeArr(MyUtil.jaToIntList(skillDataVoJO.getJSONArray("takeArr")));
			skillDataVo.setTakeValueArr(MyUtil.jaToStrList(skillDataVoJO.getJSONArray("takeValueArr")));
			skillDataVo.setTextArr(MyUtil.jaToStrList(skillDataVoJO.getJSONArray("textArr")));
			skillDataVo.setSkill_text(skillDataVoJO.getString("Skill_text"));
			skillDataVo.setRound(skillDataVoJO.getLong("round"));
			
			SkillUseRecord skillUseRecord = new SkillUseRecord(isHit, isEffect, skillDataVo);
			skillUseRecords.add(skillUseRecord);
		}
		roleElves.setSkillUseRecord(skillUseRecords);
	}
	
	/*
	 * 判断下一回合双方是不是特殊技能，若是特殊技能，就直接计算
	 * 情况：1.我方使用蓄气
	 * 		2.我方使用硬直
	 * 		3.我方使用多回合
	 * 		4.我方使用蓄力反击
	 */
	public static JSONArray nextBattleRoundResult(Player player1, Player player2, JSONObject nextOwnSkill, JSONObject nextRivalSkill){
		JSONArray joAll = null;
		RoleElves attackRoleElves = player1.getCurRound().getNowRoleElves();
		RoleElves beAttackRoleElves = player2.getCurRound().getNowRoleElves();
		SkillDataVo skill1 = player1.getCurRound().getNowSkill();
		SkillDataVo skill2 = player2.getCurRound().getNowSkill();
		//下回合我方用蓄气
		if(nextOwnSkill.getString("name").equals(SpeciesType.PowerHit)){
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerHit)){
				//若双方都使用蓄气，就直接计算
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.YingZhiHit)){
				//我方用蓄气，对方用硬直
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.MultipleRoundHit)){
				//我方用蓄气，对方用多回合
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerBackHit)){
				//若对方使用蓄力反击，就直接计算
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
		}
		
		//下回合我方用硬直(不能攻击)
		if(nextOwnSkill.getString("name").equals(SpeciesType.YingZhiHit)){
			if(nextRivalSkill.getString("name").equals(SpeciesType.YingZhiHit)){
				//若双方都使用硬直
				joAll = RoundResultService.BattleRoundResult6(player1.getRound(), player1, player2);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerHit)){
				//若对方使用蓄气，我方不能攻击
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player2, player1, beAttackRoleElves, attackRoleElves, skill2);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.MultipleRoundHit)){
				//对方用多回合，与蓄气同理
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player2, player1, beAttackRoleElves, attackRoleElves, skill2);
			}
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerBackHit)){
				//若对方使用蓄气反击，我方不能攻击
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player2, player1, beAttackRoleElves, attackRoleElves, skill2);
			}
		}
		
		//下回合我方用多回合
		if(nextOwnSkill.getString("name").equals(SpeciesType.MultipleRoundHit)){
			if(nextRivalSkill.getString("name").equals(SpeciesType.MultipleRoundHit)){
				//若双方都使用多回合技能，也直接计算
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.YingZhiHit)){
				//若对方使用硬直，对方不能攻击
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerHit)){
				//若对方使用蓄气
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerBackHit)){
				//若对方使用蓄气反击
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
		}
		
		//下回合我方用蓄力反击
		if(nextOwnSkill.getString("name").equals(SpeciesType.PowerBackHit)){
			if(nextRivalSkill.getString("name").equals(SpeciesType.MultipleRoundHit)){
				//若对方使用多回合技能
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.YingZhiHit)){
				//若对方使用硬直，对方不能攻击
				joAll = RoundResultService.BattleRoundResult4(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1);
			}
			
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerHit)){
				//若对方使用蓄气
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			if(nextRivalSkill.getString("name").equals(SpeciesType.PowerBackHit)){
				//若双方都使用蓄气反击
				joAll = RoundResultService.BattleRoundResult(player1.getRound(), player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
		}
		
		return joAll;
	}
	
	/*
	 * 下回合使用技能
	 */
	public static JSONObject nextSkill(Player player1){
		JSONObject nextOwnSkill = null;
		boolean isHit1 = player1.getCurRound().isHit();
		RoleElves attackRoleElves = player1.getCurRound().getNowRoleElves();
		if(player1.getCurRound().getResultType() == 1){
			Species species1 = player1.getCurRound().getSpecies();
			nextOwnSkill = RoundOverResultService.nextSpeciesSkill(species1, isHit1);
		}
		if(nextOwnSkill == null && !player1.isOffline()){
			boolean hasOwnSkillPP = false;
			Map<String, SkillDataVo> map = attackRoleElves.getStudySkills();
			for(String key : map.keySet()){
				if(map.get(key).getPP() != 0){
					hasOwnSkillPP = true;
					break;
				}
			}
			if(!hasOwnSkillPP){
				nextOwnSkill = new JSONObject();
				nextOwnSkill.put("name", "挣扎");
				nextOwnSkill.put("skillId", 10165);
			}
		}
		return nextOwnSkill;
	}
	/*
	 * 下回合特殊技能
	 */
	public static JSONObject nextSpeciesSkill(Species species, boolean isHit){
		
		String speciesType = species.getSpeciesType();
		int nextRoundCount = species.getCurRoundCount()+1;
		
		
//		if(speciesType.equals("普通技能")){
//			return null;
//		}
//		if(speciesType.equals("一击必杀")){
//			return null;
//		}
//		if(speciesType.equals("多次技能")){
//			return null;
//		}
		
		if(speciesType.equals(SpeciesType.MultipleRoundHit) && isHit == true){//多回合技能必须命中才能生效
			if(species.getCurRoundCount() < species.getRoundCount()){
				JSONObject nextSkill = new JSONObject();
				nextSkill.put("name", speciesType);
				nextSkill.put("skillId", species.getSkill_id());
				nextSkill.put("curRoundCount", nextRoundCount);
				nextSkill.put("roundCount", species.getRoundCount());
				species.setCurRoundCount(nextRoundCount);
				return nextSkill;
			}
		}
		if(speciesType.equals(SpeciesType.PowerHit) && isHit == true){//蓄气第一回合一定命中
			if(species.getCurRoundCount() < species.getRoundCount()){
				JSONObject nextSkill = new JSONObject();
				nextSkill.put("name", speciesType);
				nextSkill.put("skillId", species.getSkill_id());
				nextSkill.put("curRoundCount", nextRoundCount);
				species.setCurRoundCount(nextRoundCount);
				return nextSkill;
			}
		}
		if(speciesType.equals(SpeciesType.YingZhiHit) && isHit == true){//硬直第一回合必须命中才能生效
			if(species.getCurRoundCount() < species.getRoundCount()){
				JSONObject nextSkill = new JSONObject();
				nextSkill.put("name", speciesType);
				nextSkill.put("skillId", species.getSkill_id());
				nextSkill.put("curRoundCount", nextRoundCount);
				species.setCurRoundCount(nextRoundCount);
				return nextSkill;
			}
		}
		if(speciesType.equals(SpeciesType.PowerBackHit) && isHit == true){//蓄气反击第一回合一定命中
			if(species.getCurRoundCount() < species.getRoundCount()){
				JSONObject nextSkill = new JSONObject();
				nextSkill.put("name", speciesType);
				nextSkill.put("skillId", species.getSkill_id());
				nextSkill.put("curRoundCount", nextRoundCount);
				nextSkill.put("roundCount", species.getRoundCount());
				species.setCurRoundCount(nextRoundCount);
				return nextSkill;
			}
		}
		return null;
	}
	
	public boolean isHit(Elve elve1, Elve elve2, SkillDataVo skill1){
    	boolean isHit = Judge.judgeHit(elve1, elve2, skill1);
    	return isHit;
	}
	public JSONObject getAttackHitValue(RoleElves roleElves1, RoleElves roleElves2, SkillDataVo skill1){
		HurtCom hurtCom = new HurtCom();
		Elve elve1 = roleElves1.getElve();
		Elve elve2 = roleElves1.getElve();
		int hurtValue = hurtCom.hurt(roleElves1, roleElves2, skill1);
		String skill_ability1 = skill1.getSkill_ability();
		//相克倍数(系数)
		double pgm = hurtCom.pgm(skill_ability1, elve2);
		
		int c = 0;
		for(int i=0;i<StaticClass.skill_data_js.size();i++){
			JSONObject jsonObject2 = StaticClass.skill_data_js.getJSONObject(i);
			if(jsonObject2.getInt("Skill_number") == skill1.getSkill_id()){
				//要害值
				c = jsonObject2.getInt("vital");
			}
		}
		if(c > 4){
			c = 4;
		}
		Random random = new Random();
		double ran = random.nextDouble();
		//是否是要害攻击
		boolean isVital = false;
		
		switch (c) {
		case 0:
			isVital = ran < 0.0625;
			break;
		case 1:
			isVital = ran < 0.125;
			break;
		case 2:
			isVital = ran < 0.25;
			break;
		case 3:
			isVital = ran < 0.333;
			break;
		case 4:
			isVital = ran < 0.5;
			break;
		default:
			break;
		}
		
		//对手是否死亡（被攻击方）
		boolean isRivalDie = false;
		int newHp = elve2.getHp() - hurtValue;
		if(newHp >= 0){
			elve2.setHp(newHp);
		}else{
			elve2.setHp(0);
			elve2.setDie(true);
			isRivalDie = true;
		}
		
		//我方是否死亡（攻击方）
		boolean isOwnDie = elve1.getIsDie();
		//伤害值
		if(isVital){
			hurtValue *= 2;
		}
		
		JSONObject jo = new JSONObject();
		jo.put("hitValue", hurtValue);
		jo.put("xkMultiple", pgm);
		jo.put("isVital", isVital);
		jo.put("isRivalDie", isRivalDie);
		jo.put("isOwnDie", isOwnDie);
		
		return jo;
	}
	
	
	public JSONObject roundOverResult(String roomId, int userId){
		
		//下回合对手使用的技能数据
		SkillDataVo nextRivalSkill = null;
		//下回合我方使用的技能数据
		SkillDataVo nextOwnSkill = null;
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    Player player2 = null;
	    for(int i=0;i<list.size();i++){
			if(list.get(i).getUser().getId() == userId){
				player1 = list.get(i);
			}else{
				player2 = list.get(i);
			}
		}
	    
		boolean isOwnDie = player1.getCurRound().getNowRoleElves().getElve().getIsDie();
		boolean isRivalDie = player2.getCurRound().getNowRoleElves().getElve().getIsDie();
		boolean isWin = false;
		
		//还有没有别的精灵
		boolean hasOth1 = false;
		boolean hasOth2 = false;
		
		//查找是否还有别的精灵
		Map<String, RoleElves> map1 = player1.getRoleElves();
		for(String elveId : map1.keySet()){
			if(!map1.get(elveId).getElve().getIsDie()){
				//是否还有别的精灵
				hasOth1 = true;
				break;
			}
		}
		Map<String, RoleElves> map2 = player2.getRoleElves();
		for(String elveId : map2.keySet()){
			if(!map2.get(elveId).getElve().getIsDie()){
				//是否还有别的精灵
				hasOth2 = true;
				break;
			}
		}
		
		int result = 0;
		//若我没有别的精灵了，判定我方战败
		if(!hasOth1 && hasOth2){
			result = 1;
			isWin = false;
			JSONObject jo = new JSONObject();
			jo.put("result", result);
			jo.put("isWin", isWin);
			jo.put("nextOwnSkill", nextOwnSkill);
			jo.put("nextRivalSkill", nextRivalSkill);
			jo.put("isOwnDie", isOwnDie);
			jo.put("isRivalDie", isRivalDie);
			return jo;
		}
		
		//若对方没有别的精灵了，判定对方战败
		if(!hasOth2 && hasOth1){
			result = 1;
			isWin = true;
			JSONObject jo = new JSONObject();
			jo.put("result", result);
			jo.put("isWin", isWin);
			jo.put("nextOwnSkill", nextOwnSkill);
			jo.put("nextRivalSkill", nextRivalSkill);
			jo.put("isOwnDie", isOwnDie);
			jo.put("isRivalDie", isRivalDie);
			return jo;
		}
		
		//若都有别的精灵，继续战斗
		if(hasOth1 && hasOth2){
			result = 0;
			isWin = false;
			JSONObject jo = new JSONObject();
			jo.put("result", result);
			jo.put("isWin", isWin);
			jo.put("nextOwnSkill", nextOwnSkill);
			jo.put("nextRivalSkill", nextRivalSkill);
			jo.put("isOwnDie", isOwnDie);
			jo.put("isRivalDie", isRivalDie);
			return jo;
		}
		return null;
	}
	
	public static JSONArray FilterChildEffect(JSONArray ja){
		int childEffectParent = 0;
		for(int i=0;i<ja.size();i++){
			childEffectParent = ja.getJSONObject(i).getInt("childEffectParent");
			if(childEffectParent==2 || childEffectParent==4){
				ja.remove(i);
			}
		}
		return ja;
	}
	
	@SuppressWarnings("unused")
	private static int winsRankCom(int wins_points){
		int wins_rank = 1;
		if(wins_points>=0 && wins_points<=999){
			wins_rank = 1;
		}
		if(wins_points>=1000 && wins_points<=1199){
			wins_rank = 2;
		}
		if(wins_points>=1200 && wins_points<=1399){
			wins_rank = 3;
		}
		if(wins_points>=1400 && wins_points<=1599){
			wins_rank = 4;
		}
		if(wins_points>=1600 && wins_points<=1799){
			wins_rank = 5;
		}
		if(wins_points>=1800 && wins_points<=1999){
			wins_rank = 6;
		}
		if(wins_points>=2000 && wins_points<=2199){
			wins_rank = 7;	
		}
		if(wins_points>=2200 && wins_points<=2499){
			wins_rank = 8;
		}
		if(wins_points>=2500){
			wins_rank = 9;
		}
		return wins_rank;
	}
	
}
