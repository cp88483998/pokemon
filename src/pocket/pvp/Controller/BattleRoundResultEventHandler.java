package pocket.pvp.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.RoundData;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.service.HurtCom;
import pocket.pvp.service.Judge;
import pocket.total.entity.Share;
import pocket.total.util.StaticClass;
/**
 * 战斗回合数据请求
 * ResBattleRoundResult
 * @author 陈鹏
 */
public class BattleRoundResultEventHandler extends BaseClientRequestHandler{
//	Compute com = new Compute();
	HurtCom hurtCom = new HurtCom();
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String roomId = params.getUtfString("roomId");
		int round = params.getInt("round");
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
		Player player2 = null;
		Elve elve1 = null;
		Elve elve2 = null;
		SkillDataVo skill1 = null;
		SkillDataVo skill2 = null;
		RoleElves roleElves1 = null;
		RoleElves roleElves2 = null;
		for(int i=0;i<list.size();i++){
			if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
				roleElves1 = player1.getCurRound().getNowRoleElves();
				elve1 = player1.getCurRound().getNowRoleElves().getElve();
				skill1 = player1.getCurRound().getNowSkill();
			}else{
				player2 = list.get(i);
				roleElves2 = player2.getCurRound().getNowRoleElves();
				elve2 = player2.getCurRound().getNowRoleElves().getElve();
				skill2 = player2.getCurRound().getNowSkill();
			}
		}
		ISFSObject resParams = new SFSObject();
		/*
		 * 先判断对方有没有存该回合数据
		 */
		boolean hasCom1 = false; 
		boolean hasCom2 = false; 
		int index = 0;
		Map<Integer, List<RoundData>> map1 = player1.getRoundDatas();
		for(int i : map1.keySet()){
			if(i == round){
				//若存在，就直接把该回合数据返回给玩家
				hasCom1 = true;
				index = i;
			}
		}
		Map<Integer, List<RoundData>> map2 = player2.getRoundDatas();
		for(int i : map2.keySet()){
			if(i == round){
				//若存在，就直接把该回合数据返回给玩家
				hasCom2 = true;
			}
		}
		if(hasCom1 || hasCom2){
//			trace("hashCom!");
			List<RoundData> list1 = map1.get(index);
			JSONArray joArray = JSONArray.fromObject(list1);
			resParams.putUtfString("Infor", joArray.toString());
			send("ResBattleRoundResult", resParams, user);
		}
		
		/*
		 * 先手判断
		 */
//		trace("elve1 SkillList :"+elve1.getStudySkillList());
//		trace("elve2 SkillList :"+elve2.getStudySkillList());
		boolean isOver = false;
		RoundData roundData1 = new RoundData();
		RoundData roundData2 = new RoundData();
		JSONArray joAll = new JSONArray();
		
		if(elve2 != null && skill2 != null && !hasCom1){
//			trace("not hashCom! start com!");
			Elve elveFirst = Judge.judgeFirst(roleElves1, roleElves2, skill1, skill2);
			/*
			 * elve1先手
			 */
			if(elveFirst.geteLvesID()==elve1.geteLvesID() && elveFirst.getGender()==elve1.getGender() && elveFirst.getCharacter()==elve1.getCharacter() && elveFirst.getSameIndex()==elve1.getSameIndex()){
//				trace("elve1 first!");
				roundData1 = process(player1, player2, roleElves1, roleElves2, skill1);
				if(roundData1.getIsRivalDie() || roundData1.getIsOwnDie()){
					isOver = true;
				}
				if(isOver){
//					trace("elve1 first! and over!");
					//如果结束
					joAll.add(roundData1);
					
					resParams.putUtfString("Infor", joAll.toString());
					send("ResBattleRoundResult", resParams, user);
					
				}else{
//					trace("elve1 first! and not over!");
					//未结束，elve2使用技能
					roundData2 = process(player2, player1, roleElves2, roleElves1, skill2);
					joAll.add(roundData1);
					joAll.add(roundData2);
					
					resParams.putUtfString("Infor", joAll.toString());
					send("ResBattleRoundResult", resParams, user);
				}
			}
			
			/*
			 * elve2先手
			 */
			if(elveFirst.geteLvesID()==elve2.geteLvesID() && elveFirst.getGender()==elve2.getGender() && elveFirst.getCharacter()==elve2.getCharacter() && elveFirst.getSameIndex()==elve2.getSameIndex()){
//				trace("elve2 first!");
				roundData1 = process(player2, player1, roleElves2, roleElves1, skill2);
				if(roundData1.getIsRivalDie() || roundData1.getIsOwnDie()){
					isOver = true;
				}
				if(isOver){
//					trace("elve2 first! and over!");
					//如果结束
					joAll.add(roundData1);
					
					resParams.putUtfString("Infor", joAll.toString());
					send("ResBattleRoundResult", resParams, user);
					
				}else{
//					trace("elve2 first! and not over!");
					//未结束，elve1使用技能
					roundData2 = process(player1, player2, roleElves1, roleElves2, skill1);
					joAll.add(roundData1);
					joAll.add(roundData2);
					
					resParams.putUtfString("Infor", joAll.toString());
					send("ResBattleRoundResult", resParams, user);
				}
			}
			//保存回合数据
			List<RoundData> list1 = new ArrayList<RoundData>();
			list1.add(roundData1);
			list1.add(roundData2);
			
			map1.put(round, list1);
			map2.put(round, list1);
		}
	}
	
	public RoundData process(Player player1, Player player2, RoleElves roleElves1, RoleElves roleElves2, SkillDataVo skill1){
		/*
		 * 命中判断
		 */
		Elve elve1 = roleElves1.getElve();
		Elve elve2 = roleElves2.getElve();
		boolean isHit = Judge.judgeHit(elve1, elve2, skill1);
		String attackKey = elve1.geteLvesID()+"_"+elve1.getGender()+"_"+elve1.getCharacter()+"_"+elve1.getSameIndex();
		String beAttackKey = elve2.geteLvesID()+"_"+elve2.getGender()+"_"+elve2.getCharacter()+"_"+elve2.getSameIndex();
		/*
		 * 伤害值计算
		 */
		if(!isHit){
			//未中
//			JSONObject joHurt = new JSONObject();
//			joHurt.put("isHit", false);
//			joHurt.put("hitValue", 0);
//			joHurt.put("xkMultiple", 0);
//			joHurt.put("isVital", false);
//			joHurt.put("isRivalDie", false);
//			joHurt.put("isOwnDie", false);
			RoundData roundData = new RoundData();
			roundData.setHit(isHit);
			roundData.setAttackKey(attackKey);
			roundData.setBeAttackKey(beAttackKey);
			roundData.setSkillId(String.valueOf(skill1.getSkill_id()));
			return roundData;
		}
		
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
//		trace("isOwnDie :"+isOwnDie);
		//伤害值
		if(isVital){
			hurtValue *= 2;
		}
		
//		JSONObject joHurt = new JSONObject();
//		joHurt.put("isHit", true);
//		joHurt.put("hitValue", hurtValue);
//		joHurt.put("xkMultiple", pgm);
//		joHurt.put("isVital", isVital);
//		joHurt.put("isRivalDie", isRivalDie);
//		joHurt.put("isOwnDie", isOwnDie);
		
		RoundData roundData = new RoundData();
		roundData.setHit(isHit);
		roundData.setAttackKey(attackKey);
		roundData.setBeAttackKey(beAttackKey);
		roundData.setSkillId(String.valueOf(skill1.getSkill_id()));
		roundData.setHitValue(hurtValue);
		roundData.setXkMultiple(pgm);
		roundData.setVital(isVital);
		roundData.setRivalDie(isRivalDie);
		roundData.setOwnDie(isOwnDie);
		return roundData;
		
	}
	
	public JSONObject roundOverResult(Player player1, Player player2){
//		boolean isOwnDie = player1.getNowElve().isDie();
//		boolean isRivalDie = player2.getNowElve().isDie();
		boolean isWin = false;
		//下回合对手使用的技能数据
		SkillDataVo nextRivalSkill = null;
		//下回合我方使用的技能数据
		SkillDataVo nextOwnSkill = null;
		//还有没有别的精灵
		boolean hasOth1 = false;
		boolean hasOth2 = false;
		
		//查找是否还有别的精灵
		Map<String, RoleElves> map1 = player1.getRoleElves();
		for(String elveId : map1.keySet()){
			if(!map1.get(elveId).getElve().getIsDie()){
//				trace("map1："+map1.get(elveId).getElve().getIsDie());
				//是否还有别的精灵
				hasOth1 = true;
//				trace("hasOth1 :"+hasOth1);
				break;
			}
		}
		Map<String, RoleElves> map2 = player2.getRoleElves();
		for(String elveId : map2.keySet()){
			if(!map2.get(elveId).getElve().getIsDie()){
//				trace("map2："+map2.get(elveId).isDie());
				//是否还有别的精灵
				hasOth2 = true;
//				trace("hasOth2 :"+hasOth2);
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
//			jo.put("isOwnDie", isOwnDie);
//			jo.put("isRivalDie", isRivalDie);
//			trace("Me fail!");
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
//			jo.put("isOwnDie", isOwnDie);
//			jo.put("isRivalDie", isRivalDie);
//			trace("Rival fail!");
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
//			jo.put("isOwnDie", isOwnDie);
//			jo.put("isRivalDie", isRivalDie);
//			trace("continue fight!");
			return jo;
		}
		return null;
	}
}
