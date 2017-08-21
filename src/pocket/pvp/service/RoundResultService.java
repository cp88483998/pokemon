package pocket.pvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.child_effect.Child_Effect_Factory;
import pocket.pvp.entity.ChildEffectParent;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.RoundData;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.skill_species.Species;
import pocket.pvp.skill_species.SpeciesType;

public class RoundResultService {
	
	/*
	 * player1更换精灵，player2用技能
	 */
	public static JSONArray SpeciesJudgeCha(Player player1, Player player2){
		
		CurRound curRound1 = player1.getCurRound();
		RoleElves beAttackRoleElves = curRound1.getNowRoleElves();
		
		CurRound curRound2 = player2.getCurRound();
		int resultType2 = curRound2.getResultType();
		RoleElves attackRoleElves = curRound2.getNowRoleElves();
		SkillDataVo skill2 = curRound2.getNowSkill();
		
		JSONArray joAll = null;
		//若对手已经选择了技能，就直接计算
		if(resultType2 == 1){//1:使用技能

			//判断对手使用的什么类型技能
			Species species2 = player2.getCurRound().getSpecies();
			if(species2.getSpeciesType().equals(SpeciesType.YingZhiHit)){
				if(species2.getCurRoundCount() == 1){//第一回合用技能
					joAll = RoundResultService.BattleRoundResult2(player1.getRound(), player2, player1, attackRoleElves, beAttackRoleElves, skill2);
				}
				if(species2.getCurRoundCount() == 2){//第二回合不能用技能
					joAll = RoundResultService.BattleRoundResult5(player1.getRound(), player1, player2);
				}
			}else{
				//其余的多回合，蓄力，硬直，与普通技能一样计算
				joAll = RoundResultService.BattleRoundResult2(player1.getRound(), player2, player1, attackRoleElves, beAttackRoleElves, skill2);
			}
			
		}
		//若对手已经更换了精灵
		if(resultType2 == 0){
			joAll = RoundResultService.BattleRoundResult3(player1.getRound(), player2, player1);
		}
		return joAll;
	}
	
	public static JSONArray SpeciesJudgeSkill(Player player1, Player player2){
		
		CurRound curRound1 = player1.getCurRound();
		RoleElves attackRoleElves = curRound1.getNowRoleElves();
		SkillDataVo skill1 = curRound1.getNowSkill();
		
		CurRound curRound2 = player2.getCurRound();
		int resultType2 = curRound2.getResultType();
		RoleElves beAttackRoleElves = curRound2.getNowRoleElves();
		SkillDataVo skill2 = curRound2.getNowSkill();
		
		int round = player1.getRound();
		
		Species species2 = curRound2.getSpecies();
		JSONArray joAll = null;
		if(resultType2 == 1){
			
			if(species2.getSpeciesType().equals(SpeciesType.YingZhiHit)){
				if(species2.getCurRoundCount() == 1){//第一回合用技能
					joAll = RoundResultService.BattleRoundResult(round, player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
				}
				if(species2.getCurRoundCount() == 2){//第二回合不能用技能
					joAll = RoundResultService.BattleRoundResult4(round, player1, player2, attackRoleElves, beAttackRoleElves, skill1);
				}
			}
			else{
				//其余的都属于普通技能，正常计算
				joAll = RoundResultService.BattleRoundResult(round, player1, player2, attackRoleElves, beAttackRoleElves, skill1, skill2);
			}
			
		}else if(resultType2 == 0){
			//更换的精灵，直接计算一个回合
			joAll = RoundResultService.BattleRoundResult2(round, player1, player2, attackRoleElves, beAttackRoleElves, skill1);
			
		}
		return joAll;
	}
	
	/*
	 * 第二回合，双方都使用硬直技能
	 */
	public static JSONArray BattleRoundResult6(int round, Player player1, Player player2) {
		
		return null;
	}
	
	/*
	 * 第二回合，Player1使用更换精灵，Player2使用硬直技能
	 */
	public static JSONArray BattleRoundResult5(int round, Player player1, Player player2) {
		JSONArray joAll = new JSONArray();
		
		JSONArray attackRandomArr1 = player1.getCurRound().getNowRoleElves().getRandomList();
		
		RoundData roundData1 = new RoundData();
		roundData1.setResultType(0);
		Elve beChangeElve1 = player1.getCurRound().getBeChangeRoleElves().getElve();
		Elve changeElve1 = player1.getCurRound().getNowRoleElves().getElve();
		String beChangeElvesId1 = beChangeElve1.geteLvesID()+"_"+beChangeElve1.getGender()+"_"+beChangeElve1.getCharacter()+"_"+beChangeElve1.getSameIndex();
		String changeElvesId1 = changeElve1.geteLvesID()+"_"+changeElve1.getGender()+"_"+changeElve1.getCharacter()+"_"+changeElve1.getSameIndex();
		roundData1.setBeChangeElvesId(beChangeElvesId1);
		roundData1.setChangeElvesId(changeElvesId1);
		roundData1.setAttackRandomArr(attackRandomArr1);
		
		List<RoundData> list1 = new ArrayList<RoundData>();
		list1.add(roundData1);
		
		player1.getRoundDatas().put(round, list1);
		player2.getRoundDatas().put(round, list1);

		joAll.add(roundData1);
		return joAll;
		
	}
	
	/*
	 * 第二回合，Player1使用普通技能，Player2不使用技能
	 */
	public static JSONArray BattleRoundResult4(int round, Player player1, Player player2, RoleElves attackRoleElves, RoleElves beAttackRoleElves, SkillDataVo skill1) {
		JSONArray joAll = new JSONArray();
		RoundData roundData1 = process(player1, player2, attackRoleElves, beAttackRoleElves, skill1);
		
		List<RoundData> list1 = new ArrayList<RoundData>();
		list1.add(roundData1);
		
		player1.getRoundDatas().put(round, list1);
		player2.getRoundDatas().put(round, list1);

		joAll.add(roundData1);
		return joAll;
		
	}
		
	/*
	 * 计算回合数据，双方都更换了精灵
	 */
	public static JSONArray BattleRoundResult3(int round, Player player1, Player player2) {
		JSONArray joAll = new JSONArray();
		JSONArray attackRandomArr1 = player1.getCurRound().getNowRoleElves().getRandomList();
		JSONArray attackRandomArr2 = player2.getCurRound().getNowRoleElves().getRandomList();
		
		RoundData roundData2 = new RoundData();
		roundData2.setResultType(0);
		Elve beChangeElve2 = player2.getCurRound().getBeChangeRoleElves().getElve();
		Elve changeElve2 = player2.getCurRound().getNowRoleElves().getElve();
		String beChangeElvesId2 = beChangeElve2.geteLvesID()+"_"+beChangeElve2.getGender()+"_"+beChangeElve2.getCharacter()+"_"+beChangeElve2.getSameIndex();
		String changeElvesId2 = changeElve2.geteLvesID()+"_"+changeElve2.getGender()+"_"+changeElve2.getCharacter()+"_"+changeElve2.getSameIndex();
		roundData2.setBeChangeElvesId(beChangeElvesId2);
		roundData2.setChangeElvesId(changeElvesId2);
		roundData2.setAttackRandomArr(attackRandomArr2);
		
		RoundData roundData1 = new RoundData();
		roundData1.setResultType(0);
		Elve beChangeElve1 = player1.getCurRound().getBeChangeRoleElves().getElve();
		Elve changeElve1 = player1.getCurRound().getNowRoleElves().getElve();
		String beChangeElvesId1 = beChangeElve1.geteLvesID()+"_"+beChangeElve1.getGender()+"_"+beChangeElve1.getCharacter()+"_"+beChangeElve1.getSameIndex();
		String changeElvesId1 = changeElve1.geteLvesID()+"_"+changeElve1.getGender()+"_"+changeElve1.getCharacter()+"_"+changeElve1.getSameIndex();
		roundData1.setBeChangeElvesId(beChangeElvesId1);
		roundData1.setChangeElvesId(changeElvesId1);
		roundData1.setAttackRandomArr(attackRandomArr1);
		
		joAll.add(roundData2);
		joAll.add(roundData1);
		//保存回合数据
		List<RoundData> list1 = new ArrayList<RoundData>();
		list1.add(roundData2);
		list1.add(roundData1);
		
		Map<Integer, List<RoundData>> map1 = player1.getRoundDatas();
		Map<Integer, List<RoundData>> map2 = player2.getRoundDatas();
		map1.put(round, list1);
		map2.put(round, list1);
//		player1.setCurRound(null);
//		player2.setCurRound(null);
		return joAll;
	}
	
	/*
	 * 计算回合数据，player1使用了技能，player2更换精灵
	 */
	public static JSONArray BattleRoundResult2(int round, Player player1, Player player2, RoleElves attackRoleElves, RoleElves beAttackRoleElves, SkillDataVo skill1) {
		RoundData roundData1 = new RoundData();
		JSONArray joAll = new JSONArray();
		
		//先放更换精灵的数据
		RoundData roundData2 = new RoundData();
		roundData2.setResultType(0);
		Elve beChangeElve = player2.getCurRound().getBeChangeRoleElves().getElve();
		Elve changeElve = player2.getCurRound().getNowRoleElves().getElve();
		String beChangeElvesId = beChangeElve.geteLvesID()+"_"+beChangeElve.getGender()+"_"+beChangeElve.getCharacter()+"_"+beChangeElve.getSameIndex();
		String changeElvesId = changeElve.geteLvesID()+"_"+changeElve.getGender()+"_"+changeElve.getCharacter()+"_"+changeElve.getSameIndex();
		roundData2.setBeChangeElvesId(beChangeElvesId);
		roundData2.setChangeElvesId(changeElvesId);
		JSONArray attackRandomArr = attackRoleElves.getRandomList();
		roundData2.setAttackRandomArr(attackRandomArr);
		
		roundData1 = process(player1, player2, attackRoleElves, beAttackRoleElves, skill1);
		joAll.add(roundData2);
		joAll.add(roundData1);
		//保存回合数据
		List<RoundData> list1 = new ArrayList<RoundData>();
		list1.add(roundData2);
		list1.add(roundData1);
		
		Map<Integer, List<RoundData>> map1 = player1.getRoundDatas();
		Map<Integer, List<RoundData>> map2 = player2.getRoundDatas();
		map1.put(round, list1);
		map2.put(round, list1);
//		player1.setCurRound(null);
//		player2.setCurRound(null);
		return joAll;
	}
	
	/*
	 * 计算回合数据，双方都使用了技能
	 */
	public static JSONArray BattleRoundResult(int round, Player player1, Player player2, RoleElves attackRoleElves, RoleElves beAttackRoleElves, SkillDataVo skill1, SkillDataVo skill2) {
		boolean isOver = false;
		RoundData roundData1 = new RoundData();
		RoundData roundData2 = new RoundData();
		JSONArray joAll = new JSONArray();
		Elve elve1 = attackRoleElves.getElve();
		Elve elve2 = beAttackRoleElves.getElve();
		if(elve2 != null && skill2 != null){
			/*
			 * 1.先手判断
			 */
			Elve elveFirst = Judge.judgeFirst(attackRoleElves, beAttackRoleElves, skill1, skill2);
			// elve1先手
			if(elveFirst.geteLvesID()==elve1.geteLvesID() && elveFirst.getGender()==elve1.getGender() && elveFirst.getCharacter()==elve1.getCharacter() && elveFirst.getSameIndex()==elve1.getSameIndex()){
				roundData1 = process(player1, player2, attackRoleElves, beAttackRoleElves, skill1);
				if(roundData1.getIsRivalDie() || roundData1.getIsOwnDie()){
					isOver = true;
				}
				if(isOver){
					//如果结束
					joAll.add(roundData1);
				}else{
					//未结束，elve2使用技能
					roundData2 = process(player2, player1, beAttackRoleElves, attackRoleElves, skill2);
					joAll.add(roundData1);
					joAll.add(roundData2);
				}
			}
			
			// elve2先手
			if(elveFirst.geteLvesID()==elve2.geteLvesID() && elveFirst.getGender()==elve2.getGender() && elveFirst.getCharacter()==elve2.getCharacter() && elveFirst.getSameIndex()==elve2.getSameIndex()){
				roundData1 = process(player2, player1, beAttackRoleElves, attackRoleElves, skill2);
				if(roundData1.getIsRivalDie() || roundData1.getIsOwnDie()){
					isOver = true;
				}
				if(isOver){
					//如果结束
					joAll.add(roundData1);
				}else{
					//未结束，elve1使用技能
					roundData2 = process(player1, player2, attackRoleElves, beAttackRoleElves, skill1);
					joAll.add(roundData1);
					joAll.add(roundData2);
				}
			}
			//保存回合数据
			List<RoundData> list1 = new ArrayList<RoundData>();
			list1.add(roundData1);
			list1.add(roundData2);
			
			Map<Integer, List<RoundData>> map1 = player1.getRoundDatas();
			Map<Integer, List<RoundData>> map2 = player2.getRoundDatas();
			map1.put(round, list1);
			map2.put(round, list1);
		}
//		player1.setCurRound(null);
//		player2.setCurRound(null);
		return joAll;
	}
	
	private static RoundData process(Player player1, Player player2, RoleElves attackRoleElves, RoleElves beAttackRoleElves, SkillDataVo skill1){
		/*
		 * 2.命中判断
		 */
		Elve elve1 = attackRoleElves.getElve();
		Elve elve2 = beAttackRoleElves.getElve();
		HurtCom hurtCom = new HurtCom();
		boolean isHit = Judge.judgeHit(elve1, elve2, skill1);
		String attackKey = elve1.geteLvesID()+"_"+elve1.getGender()+"_"+elve1.getCharacter()+"_"+elve1.getSameIndex();
		String beAttackKey = elve2.geteLvesID()+"_"+elve2.getGender()+"_"+elve2.getCharacter()+"_"+elve2.getSameIndex();
		//随机100个不重复float
//		float[] attackRandomArr = MyUtil.ranFloatArr(100);
//		float[] beAttackRandomArr = MyUtil.ranFloatArr(100);
		JSONArray attackRandomArr = attackRoleElves.getRandomList();
		JSONArray beAttackRandomArr = beAttackRoleElves.getRandomList();
		/*
		 * 3.伤害值计算
		 */
		if(!isHit){
			//未中
			RoundData roundData = new RoundData();
			roundData.setHit(isHit);
			roundData.setResultType(1);
			roundData.setAttackKey(attackKey);
			roundData.setBeAttackKey(beAttackKey);
			roundData.setSkillId(String.valueOf(skill1.getSkill_id()));
			roundData.setAttackRandomArr(attackRandomArr);
			roundData.setBeAttackRandomArr(beAttackRandomArr);
			return roundData;
		}
		
		
//		int hurtValue = hurtCom.hurt(attackRoleElves, beAttackRoleElves, skill1);
		int hurtValue = 1;
		String skill_ability1 = skill1.getSkill_ability();
		//相克倍数(系数)
		double pgm = hurtCom.pgm(skill_ability1, elve2);
		//判定是否要害攻击
		boolean isVital = hurtCom.isVital(skill1);
		//伤害值
		if(isVital){
			hurtValue *= 2;
		}
		
		
		//对手是否死亡（被攻击方）
		boolean isRivalDie = elve2.getHp() == 0;
//		int newHp = elve2.getHp() - hurtValue;
//		if(newHp >= 0){
//			elve2.setHp(newHp);
//		}else{
//			elve2.setHp(0);
//			elve2.setDie(true);
//			isRivalDie = true;
//		}
		
		//我方是否死亡（攻击方）
		boolean isOwnDie = elve1.getIsDie();
//		trace("isOwnDie :"+isOwnDie);
		
		RoundData roundData = new RoundData();
		roundData.setHit(isHit);
		roundData.setResultType(1);
		roundData.setAttackKey(attackKey);
		roundData.setBeAttackKey(beAttackKey);
		roundData.setSkillId(String.valueOf(skill1.getSkill_id()));
		roundData.setHitValue(hurtValue);
		roundData.setXkMultiple(pgm);
		roundData.setVital(isVital);
		roundData.setRivalDie(isRivalDie);
		roundData.setOwnDie(isOwnDie);
		roundData.setAttackRandomArr(attackRandomArr);
		roundData.setBeAttackRandomArr(beAttackRandomArr);
		return roundData;
		
	}
	//创建子效果
	public static Child_Effect setChild_Effect(JSONObject jsonObject){
		String name = jsonObject.getString("name");
//		trace("name :"+name);
		double effectRate = jsonObject.getDouble("effectRate");
//			String applyElvesKey = jsonObject.getString("applyElvesKey");
		int childEffectParentID = jsonObject.getInt("childEffectParent");
		String targetValue = jsonObject.getString("targetValue");
		int target = jsonObject.getInt("target");
		String parentObjId = jsonObject.getString("parentObjId");
//		JSONArray joArr = jsonObject.getJSONArray("parameterArr");
//		List<String> parameterArr = new ArrayList<String>();
//		for(int i=0;i<joArr.size();i++){
//			
//		}
		int effectiveTimeId = jsonObject.getInt("effectiveTimeId");
		int take = jsonObject.getInt("take");
		String takeValue = jsonObject.getString("takeValue");
		boolean isOnce = jsonObject.getBoolean("isOnce");
		boolean isTriggerLineOne = jsonObject.getBoolean("isTriggerLineOne");
		String triggerFailLine = jsonObject.getString("triggerFailLine");
		int id = jsonObject.getInt("id");
//		String effectId = jsonObject.getString("effectId");
		
		boolean isCanntAction = jsonObject.getBoolean("isCanntAction");
		boolean isSkillFail = jsonObject.getBoolean("isSkillFail");
		boolean isCantChangeElves = jsonObject.getBoolean("isCantChangeElves");
		boolean isCantLeave = jsonObject.getBoolean("isCantLeave");
		boolean isMustLeave = jsonObject.getBoolean("isMustLeave");
		boolean isMustHit = jsonObject.getBoolean("isMustHit");
		boolean isMustBeHit = jsonObject.getBoolean("isMustBeHit");
		boolean isCanntResetHp = jsonObject.getBoolean("isCanntResetHp");
		boolean isCannotFeatures = jsonObject.getBoolean("isCannotFeatures");
		boolean isCanntChangeLevel = jsonObject.getBoolean("isCanntChangeLevel");
		boolean isElvesAttributeFail = jsonObject.getBoolean("isElvesAttributeFail");
		boolean isKeepOneHp = jsonObject.getBoolean("isKeepOneHp");
		boolean isCanntChangeSkill = jsonObject.getBoolean("isCanntChangeSkill");
		boolean isCanntVital = jsonObject.getBoolean("isCanntVital");
		boolean isStopOneAtk = jsonObject.getBoolean("isStopOneAtk");
		boolean isCouSameSkill = jsonObject.getBoolean("isCouSameSkill");
		boolean isOnlyLastSkill = jsonObject.getBoolean("isOnlyLastSkill");
		boolean isChangeTarget = jsonObject.getBoolean("isChangeTarget");
		
		Child_Effect_Factory factory = new Child_Effect_Factory();
		Child_Effect child_effect = factory.create(String.valueOf(id), target, targetValue);
		if(child_effect == null){
			return null;
		}
		child_effect.setName(name);
		child_effect.setEffectRate(effectRate);
//			child_effect.setApplyElvesKey(applyElvesKey);
		child_effect.setParentObjId(parentObjId);
		child_effect.setEffectiveTimeId(effectiveTimeId);
		child_effect.setTake(take);
		child_effect.setTakeValue(takeValue);
		child_effect.setOnce(isOnce);
		child_effect.setId(id);
		child_effect.setTargetValue(targetValue);
		child_effect.setChildEffectParent(getEnum(childEffectParentID));
		child_effect.setTriggerLineOne(isTriggerLineOne);
		child_effect.setTriggerFailLine(triggerFailLine);
		
		child_effect.setCanntAction(isCanntAction);
		child_effect.setSkillFail(isSkillFail);
		child_effect.setCantChangeElves(isCantChangeElves);
		child_effect.setCantLeave(isCantLeave);
		child_effect.setMustLeave(isMustLeave);
		child_effect.setMustHit(isMustHit);
		child_effect.setMustBeHit(isMustBeHit);
		child_effect.setCanntResetHp(isCanntResetHp);
		child_effect.setCannotFeatures(isCannotFeatures);
		child_effect.setCanntChangeLevel(isCanntChangeLevel);
		child_effect.setElvesAttributeFail(isElvesAttributeFail);
		child_effect.setKeepOneHp(isKeepOneHp);
		child_effect.setCanntChangeSkill(isCanntChangeSkill);
		child_effect.setCanntVital(isCanntVital);
		child_effect.setStopOneAtk(isStopOneAtk);
		child_effect.setCouSameSkill(isCouSameSkill);
		child_effect.setOnlyLastSkill(isOnlyLastSkill);
		child_effect.setChangeTarget(isChangeTarget);
		
		return child_effect;
		
	}
	
	private static ChildEffectParent getEnum(int i){
		switch (i) {
		case 1:
			return ChildEffectParent.Buff;
		case 2:
			return ChildEffectParent.Features;
		case 3:
			return ChildEffectParent.Effect;
		case 4:
			return ChildEffectParent.Equip;
		case 5:
			return ChildEffectParent.Weather;
		case 6:
			return ChildEffectParent.Skill;
		default:
			return ChildEffectParent.None;
		}
	}
}
