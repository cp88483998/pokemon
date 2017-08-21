package pocket.pvp.Controller;

import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.service.FightingDataService;
import pocket.pvp.service.RoundOverResultService;
import pocket.pvp.service.RoundResultService;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;

/**
 * 请求更换精灵处理类
 * <p>Title: ChangeElvesEventHandler_hasAuto<／p>
 * <p>Description:1.若非死亡更换，(1)需判断对手是否使用了技能或者更换了精灵（resultType!=10），若resultType!=10，直接计算回合数据
 * 							 (2)若对手离线，直接选择技能，并计算回合数据
 * 							 (3)若对手断线重连，并且是第一回合(isConBack)，需要发送ResReconBattleRoundResult给对方
 * 				  2.若isMustLeave，需判断对方是否已经播放完动画（isMustLeaveOver==true），才能发送ResRivalChangeElves给对方<／p>
 * 				  3.免费更换精灵（对手死亡时可获得一次免费更换机会，若已使用多回合技能，不能免费更换）
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class ChangeElvesEventHandler_hasAuto extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("Infor");
//		trace("infor :"+infor);
		String roomId = params.getUtfString("roomId");
		JSONObject jsonObject = JSONObject.fromObject(infor);
		String elvesId = jsonObject.getString("elvesId");
		
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    Player player2 = null;
	    RoleElves roleElve = null;
	    Elve value = null;
	    for(int i=0;i<list.size();i++){
	    	if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
			}else{
				player2 = list.get(i);
			}
		}
	    JSONArray ranFloatArr = player1.getCurRound().getNowRoleElves().getRandomList();
	    if(!jsonObject.getBoolean("isDie") && !player1.isFreeChangeElve() && !jsonObject.getBoolean("isMustLeave")){
//	    	trace("not die change");
	    	Map<String, RoleElves> map = player1.getRoleElves();
	  		for(String key : map.keySet()){
	  			roleElve = map.get(key);
	  			value = roleElve.getElve();
//	  			if(elveId1==Integer.parseInt(key) && gender1==value.getGender() && character1==value.getCharacter() && sameIndex1==value.getSameIndex()){
	  			if(elvesId.equals(key)){
	  				roleElve.setRandomIndex(0);
	  				CurRound curRound = player1.getCurRound();
	  				curRound.setResultType(0);//0 更换精灵 1 使用技能
	  				curRound.setBeChangeRoleElves(curRound.getNowRoleElves());
//	  				trace("BeChangeRoleElves :"+curRound.getBeChangeRoleElves().getElve());
	  				curRound.setNowRoleElves(roleElve);
//	  				trace("setNowRoleElve :"+roleElve.getElve().toString());
	  				break;
	  			}
	  		}
	    }else if(jsonObject.getBoolean("isDie") || player1.isFreeChangeElve() || jsonObject.getBoolean("isMustLeave")){
//	    	trace("isDie or isFree or isMustLeave");
	    	Map<String, RoleElves> map = player1.getRoleElves();
	  		for(String key : map.keySet()){
	  			roleElve = map.get(key);
	  			value = roleElve.getElve();
//	  			if(elveId1==Integer.parseInt(key) && gender1==value.getGender() && character1==value.getCharacter() && sameIndex1==value.getSameIndex()){
	  			if(elvesId.equals(key)){
	  				roleElve.setRandomIndex(0);
	  				CurRound curRound = player1.getCurRound();
	  				
	  				//设置resultType，防止使用的是蓄力或者硬直技能
	  				curRound.setResultType(10);
	  				curRound.setSpecies(null);
  					curRound.setBeChangeRoleElves(curRound.getNowRoleElves());
//  				trace("BeChangeRoleElves :"+curRound.getBeChangeRoleElves().getElve());
	  				curRound.setNowRoleElves(roleElve);
//	  				trace("setNowRoleElve :"+roleElve.getElve().toString());
	  				break;
	  			}
	  		}
	    }
	    ISFSObject resParams = new SFSObject();
	    
		//判断精灵是否死亡
	    //若没死亡，等待对手选择技能，并且本回合不能使用技能，不需要ResChangeElves、ResRivalChangeElves
	    if(!jsonObject.getBoolean("isDie") && !player1.isFreeChangeElve() && !jsonObject.getBoolean("isMustLeave")){
//	    	trace("change elve not die and not free!");
			
//	    	trace("player2.getCurRound().getResultType() :"+player2.getCurRound().getResultType());
	    	JSONArray joAll = null;
	    	JSONObject joObject = new JSONObject();
	    	
	    	//若对手已经使用了技能或者更换了精灵，直接计算battleRoundResult
	    	if(player2.getCurRound().getResultType() != 10){
	    		joAll = RoundResultService.SpeciesJudgeCha(player1, player2);//player1更换精灵，player2使用技能
	    		
	    	}else{	
				//若对手离线，并且还没有选技能 或者 对手断线重连，并且是第一回合
				if(player2.isOffline() || player2.isConBack() || player2.isBattleReconData()){
//					trace("player2 isOffline! "+player2.isOffline());
//					trace("player2 is connect back! "+player2.isConBack());
					//对手先自动选技能
					JSONObject jo2 = SkillService.autoChooseSkill(player2, player1);
					resParams.putUtfString("Infor", jo2.toString());
					
					//将数据发给在线的玩家
					send("ResRivalUseSkill", resParams, user);
					
					//计算战斗数据
					joAll = RoundResultService.SpeciesJudgeCha(player1, player2);
				}
	    	}
	    	
	    	if(joAll != null){
				player1.setBattleRoundResult(joAll);
				player2.setBattleRoundResult(joAll);
				
				player1.setPlayOver(false);
				player2.setPlayOver(false);
				
				//走到这里，理论上joAll不可能出现为null的情况
//				joObject.put("roundChildEffect", jsonRoundCE);
				joObject.put("battleRoundResult", joAll);
				resParams.putUtfString("Infor", joObject.toString());
				send("ResBattleRoundResult", resParams, user);
				
				if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
					send("ResBattleRoundResult", resParams, player2.getUser());
				}
				
				//对手断线重连
				if(!player2.isOffline() && player2.isConBack() && !player2.isBattleReconData()){
//					trace("player2 is connect back!");
					JSONObject jo = FightingDataService.conBattleRoundResult(player2, player1);
					
					resParams.putUtfString("Infor", jo.toString());
					send("ResReconBattleRoundResult", resParams, player2.getUser());
					
					player2.setConBack(false);
				}
				
//				trace("send ResBattleRoundResult");
				player1.getCurRound().setBeChangeRoleElves(null);
				player2.getCurRound().setBeChangeRoleElves(null);
			}
		}
	    //若是免费更换的精灵
	    if(player1.isFreeChangeElve() || jsonObject.getBoolean("isMustLeave")){
//	    	trace("isFreeChangeElve!"+player1.isFreeChangeElve());
//	    	trace("isMustLeave!"+jsonObject.getBoolean("isMustLeave"));
	    	String resElvesId = value.geteLvesID()+"_"+value.getGender()+"_"+value.getCharacter()+"_"+value.getSameIndex();
			JSONObject jo1 = new JSONObject();
			jo1.put("isUseSkill", true);
			jo1.put("randomArr", ranFloatArr);
			jo1.put("elvesId", resElvesId);
			jo1.put("isDie", jsonObject.getBoolean("isDie"));
			resParams.putUtfString("Infor", jo1.toString());
			send("ResChangeElves", resParams, user);
			
			//发给对手
			JSONObject jo2 = new JSONObject();
//			trace("rival change roleElve :"+roleElve.getElve().toString());
			jo2.put("elves", roleElve.getElve());
			jo2.put("isUseSkill", true);
			jo2.put("isFreeChangeElve", false);
			jo2.put("randomArr", ranFloatArr);
			resParams.putUtfString("Infor", jo2.toString());
			if(player1.isFreeChangeElve()){
				if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
					send("ResRivalChangeElves", resParams, player2.getUser());
				}else{
					//若对手离线，就存下来，等对手上线后再发给他
					player2.setMustLeaveJO(jo2);
				}
				player1.setFreeChangeElve(false);
			}
			if(jsonObject.getBoolean("isMustLeave")){
				//若是强制离场，必须更新换场精灵的数据，因为在roundOverResult中没有换场精灵的数据。
				JSONObject oldElvesJO = jsonObject.getJSONObject("oldElves");
				updateOldRoleElves(oldElvesJO, player1);
				
//				trace("getStudySkills :"+player1.getCurRound().getBeChangeRoleElves().getStudySkills());
//				trace("getStudySkillList :"+player1.getCurRound().getBeChangeRoleElves().getElve().getStudySkillList());
				
				if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData() && player2.isMustLeaveOver()){
					send("ResRivalChangeElves", resParams, player2.getUser());
					
					player2.setMustLeaveOver(false);
				}else{
					//若对手离线，就存下来，等对手上线后再发给他
					player2.setMustLeaveJO(jo2);
				}
			}
			
	    }
	    //若死亡，通知对手也可以更换精灵
	    else if(jsonObject.getBoolean("isDie")){
//			trace("change elve die!");
			String resElvesId = value.geteLvesID()+"_"+value.getGender()+"_"+value.getCharacter()+"_"+value.getSameIndex();
			JSONObject jo1 = new JSONObject();
			jo1.put("isUseSkill", true);
			jo1.put("randomArr", ranFloatArr);
			jo1.put("elvesId", resElvesId);
			jo1.put("isDie", jsonObject.getBoolean("isDie"));
			resParams.putUtfString("Infor", jo1.toString());
			send("ResChangeElves", resParams, user);
			
			//发给对手
			boolean isFreeCha = player2.getCurRound().getResultType() != 1;
			JSONObject jo2 = new JSONObject();
//			trace("rival change roleElve :"+roleElve.getElve().toString());
			jo2.put("elves", roleElve.getElve());
			jo2.put("isUseSkill", true);
			player2.setFreeChangeElve(isFreeCha);
			jo2.put("isFreeChangeElve", isFreeCha);
			jo2.put("randomArr", ranFloatArr);
			resParams.putUtfString("Infor", jo2.toString());
			if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
				send("ResRivalChangeElves", resParams, player2.getUser());
			}
			
			//若ResultType = 1，对手使用了多回合技能
			if(!isFreeCha){
				player2.setFreeChangeElve(false);
				send("ResRivalCanelChangeElves", null, user);
			}
			
			//若对手离线
			if(player2.isOffline() || player2.isConBack() || player2.isBattleReconData()){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				player2.setFreeChangeElve(false);
				send("ResRivalCanelChangeElves", null, user);
			}
		}
	    
//	    trace("ChangeElvesEventHandler ok!");
	}
	
	public static void updateOldRoleElves(JSONObject oldElvesJO, Player player){
		
//		JSONObject joWea = null;
//		if(!oldElvesJO.get("weatherInfor").equals("")){
//			joWea = oldElvesJO.getJSONObject("weatherInfor");
//		}
//		if(joWea!=null){
//			player.setWeatherInfor(joWea);
//		}
		
		JSONObject elves1Info = oldElvesJO.getJSONObject("elvesDataInforVo");
		JSONArray elves1Effect = RoundOverResultService.FilterChildEffect(oldElvesJO.getJSONArray("childEffectList"));
		JSONArray elves1Buff = oldElvesJO.getJSONArray("stateSkillEffectList");
		JSONObject elves1Skill = oldElvesJO.getJSONObject("skillInfor");
		JSONArray elves1Record = oldElvesJO.getJSONArray("skillUseRecord");
		boolean isHit1 = false;
		int randomIndex1 = oldElvesJO.getInt("randomIndex");
		boolean isFirstSkill1 = oldElvesJO.getBoolean("isFirstSkill");
		long wuHitValue1 = oldElvesJO.getLong("wuHitValue");
		int hitValue1 = oldElvesJO.getInt("hitValue");
		
		//更新精灵的hp，子效果，buff
		RoleElves oldRoleElves = player.getCurRound().getBeChangeRoleElves();
		Elve oldElve = oldRoleElves.getElve();
		if(Integer.parseInt(elves1Info.getString("eLvesID")) == oldElve.geteLvesID() 
				&& elves1Info.getInt("gender")==oldElve.getGender()
				&& elves1Info.getInt("character")==oldElve.getCharacter()
				&& elves1Info.getInt("sameIndex")==oldElve.getSameIndex()){
			//更新hp
			oldElve.setHp(elves1Info.getInt("hp"));
			isHit1 = elves1Skill.getBoolean("isHit");
			String studySkillList1 = elves1Info.getString("studySkillList");
			//更新命中
			player.getCurRound().setHit(isHit1);
			//更新子效果
			oldRoleElves.setChildEffectListJA(elves1Effect);
			RoundOverResultService.updateChildEffectList(elves1Effect, oldRoleElves);
			//更新buff
			oldRoleElves.setStateSkillEffectListJA(elves1Buff);
			//更新技能
			oldRoleElves.getElve().setStudySkillList(studySkillList1);
			//更新randomIndex
			oldRoleElves.setRandomIndex(randomIndex1);
			//更新skillUseRecord
			RoundOverResultService.updateRecords(oldRoleElves, elves1Record);
			//更新isFirstSkill
			oldRoleElves.setFirstSkill(isFirstSkill1);
			//更新wuHitValue
			oldRoleElves.setWuHitValue(wuHitValue1);
			//更新hitValue
			oldRoleElves.setHitValue(hitValue1);
			//更新技能pp值
			RoundOverResultService.updateSkillPP(studySkillList1, oldRoleElves);
			
		}
	}
}
