package pocket.pvp.service;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.skill_species.Species;

public class DisconService{
	
	/**
	 * 死亡更换精灵的时候，断线
	 * @param player1
	 * @param player2
	 * @return ResRivalChangeElves
	 */
	public static JSONObject dieChangeElve(Player player1, Player player2){
		CurRound curRound1 = player1.getCurRound();
		RoleElves roleElves1 = null;
		//1.player1先自动换一个活着的精灵
		for(String key : player1.getRoleElves().keySet()){
			if(!player1.getRoleElves().get(key).getElve().getIsDie()){
				roleElves1 = player1.getRoleElves().get(key);
				break;
			}
		}
		
		if(roleElves1 != null){
			//设置resultType，防止使用的是蓄力或者硬直技能
			curRound1.setResultType(10);
			curRound1.setSpecies(null);
				
			curRound1.setBeChangeRoleElves(curRound1.getNowRoleElves());
			curRound1.setNowRoleElves(roleElves1);
			
			JSONArray ranFloatArr = player1.getCurRound().getNowRoleElves().getRandomList();
			
			//发给在线的一方player2
			boolean isFreeCha = player2.getCurRound().getResultType() != 1;
			JSONObject jo = new JSONObject();
			jo.put("elves", roleElves1.getElve());
			jo.put("isUseSkill", true);
			player2.setFreeChangeElve(isFreeCha);
			jo.put("isFreeChangeElve", isFreeCha);
			jo.put("randomArr", ranFloatArr);
			return jo;
		}
		return null;
	}
	
	/**
	 * 战斗回合结束处理
	 * 当player1掉线，player2已经发送了roundOverResult
	 * @param player1
	 * @param player2
	 * @return ResRoundOverResult
	 */
	public static JSONObject roundOverResult(Player player1, Player player2){
		//更新精灵的hp
		RoundOverResultService.updateRoleElves(player1, player2);
		
		RoleElves attackRoleElves = player1.getCurRound().getNowRoleElves();
		RoleElves beAttackRoleElves = player2.getCurRound().getNowRoleElves();
		
		Elve elve1 = attackRoleElves.getElve();
		Elve elve2 = beAttackRoleElves.getElve();
		
		boolean isHit1 = player1.getCurRound().isHit();
		boolean isHit2 = player2.getCurRound().isHit();
		
		boolean isOwnDie = elve1.getIsDie();
		boolean isRivalDie = elve2.getIsDie();
		
	    @SuppressWarnings("unused")
		boolean isOwnWin = false;
	    boolean isRivalWin = false;
		
		//还有没有别的精灵
		boolean ownHasOth = false;
		boolean rivalHasOth = false;
		
		//查找是否还有别的精灵
		Map<String, RoleElves> map1 = player1.getRoleElves();
		for(String elveId : map1.keySet()){
			if(!map1.get(elveId).getElve().getIsDie()){
//						trace("map1："+map1.get(elveId).getElve().isDie());
				//是否还有别的精灵
				ownHasOth = true;
//						trace("ownHasOth :"+ownHasOth);
				break;
			}
		}
		Map<String, RoleElves> map2 = player2.getRoleElves();
		for(String elveId : map2.keySet()){
			if(!map2.get(elveId).getElve().getIsDie()){
//				trace("map2："+map2.get(elveId).isDie());
				//是否还有别的精灵
				rivalHasOth = true;
//						trace("rivalHasOth :"+rivalHasOth);
				break;
			}
		}
		
		JSONObject nextOwnSkill = null;
		Species species1 = null;
		Species species2 = null;
		if(player1.getCurRound().getResultType() == 1){
			species1 = player1.getCurRound().getSpecies();
//					trace("species1 :"+species1.toString());
			nextOwnSkill = RoundOverResultService.nextSpeciesSkill(species1, isHit1);
		}
		JSONObject nextRivalSkill = null;
		if(player2.getCurRound().getResultType() == 1){
			species2 = player2.getCurRound().getSpecies();
//					trace("species2 :"+species2.toString());
			nextRivalSkill = RoundOverResultService.nextSpeciesSkill(species2, isHit2);
		}
		
		int result = 0;//0:继续战斗， 1:战斗结束， 100:战斗数据校验出错
		//若我没有别的精灵了，判定我方战败
		if(!ownHasOth && rivalHasOth){
			result = 1;
			isOwnWin = false;
			isRivalWin = true;
		}
		
		//若对方没有别的精灵了，判定对方战败
		if(!rivalHasOth && ownHasOth){
			result = 1;
			isOwnWin = true;
			isRivalWin = false;
		}
		
		//若都有别的精灵，继续战斗
		if(ownHasOth && rivalHasOth){
			result = 0;
			isOwnWin = false;
			isRivalWin = false;
		}	
		
		//若精灵死亡，特殊技能清空，可以重新选择技能
		if(isOwnDie){
			nextOwnSkill = null;
		}
		if(isRivalDie){
			nextRivalSkill = null;
		}
//		ISFSObject resParams = new SFSObject();
		int round = player1.getRound()+1;
		player1.setRound(round);
		player2.setRound(round);
		
		JSONObject jo = null;
//		if(!player2.isOffline()){
			jo = new JSONObject();
			jo.put("result", result);
			jo.put("isWin", isRivalWin);
			jo.put("nextOwnSkill", nextRivalSkill);
			jo.put("nextRivalSkill", nextOwnSkill);
			jo.put("isOwnDie", isRivalDie);
			jo.put("isRivalDie", isOwnDie);
			jo.put("round", round);
//			resParams.putUtfString("Infor", jo.toString());
//			send("ResRoundOverResult", resParams, player2.getUser());
//		}
		
		//记得清空回合数据
    	player1.setJoRound(null);
    	player2.setJoRound(null);
    	player1.setBattleRoundResult(null);
    	player2.setBattleRoundResult(null);
    	
    	if(nextOwnSkill == null){
    		player1.getCurRound().setResultType(10);
    	}
    	if(nextRivalSkill == null){
    		player2.getCurRound().setResultType(10);
    	}	
		
    	return jo;
	}
	
}
