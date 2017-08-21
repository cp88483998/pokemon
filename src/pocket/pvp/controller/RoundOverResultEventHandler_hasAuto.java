package pocket.pvp.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.service.DisconService;
import pocket.pvp.service.FightingDataService;
import pocket.pvp.service.RoundOverResultService;
import pocket.pvp.service.RoundResultService;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;
import pocket.total.util.PVPResult;
/**
 * 请求当前回合结束
 * <p>Title: RoundOverResultEventHandler_hasAuto<／p>
 * <p>Description: 一：1.双方都在线，双方都发送了roundOverResult才能校验，校验完成后更新本回合精灵数据。
 * 				   	  2.一方不在线，不需要验证，直接更新数据，
 * 				   	  3.双方都使用了多回合技能，直接计算下回合数据。
 * 	 			         二：涉及到部分离线操作。	
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class RoundOverResultEventHandler_hasAuto extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("RoundOverResultEventHandler Start!");
		String roomId = params.getUtfString("roomId");
		int round = params.getInt("round");
//		trace("round :"+round);
		String infor = params.getUtfString("Infor");
//		trace("infor :"+infor);
		
		JSONObject joRound = JSONObject.fromObject(infor);
		
		round++;
//		trace("round++ :"+round);
		
		ISFSObject resParams = new SFSObject();

		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    Player player2 = null;
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
	    
	    player1.setJoRound(joRound);
	    
	    //更新回合时间
	    long nowTime = System.currentTimeMillis()/1000;
  		player1.setRoundTime(nowTime);
  		player2.setRoundTime(nowTime);
  		
//	    trace(player2.getUser().getName()+" is offline :"+player2.isOffline());
//	    trace(player1.getUser().getName()+" is offline :"+player1.isOffline());
	    
    	String compareResult = null;
    	boolean isComOk = false;
    	boolean hasJoRound2 = player2.getJoRound()!= null;
//    	trace("JoRound1 :"+player1.getJoRound());
    	if(!player2.isOffline() && hasJoRound2){
    		//验证两边回合数据是否相等
//    		trace("JoRound2 :"+player2.getJoRound());
	    	compareResult = compare(player1.getJoRound(), player2.getJoRound());
//    		compareResult = compare3(player1.getJoRound(), player2.getJoRound());
	    	isComOk = compareResult.equals("OK");
    	}
    	
    	//若相等
    	if(isComOk || player2.isOffline()){
    		//更新精灵的hp，子效果，buff
    		RoundOverResultService.updateRoleElves(player1, player2);
//    		trace(player1.getCurRound().getNowRoleElves().getChildEffectList().size());
//    		trace(player1.getCurRound().getNowRoleElves().getChildEffectList());
    		
    		RoleElves attackRoleElves = player1.getCurRound().getNowRoleElves();
    		RoleElves beAttackRoleElves = player2.getCurRound().getNowRoleElves();
    		
    		Elve elve1 = attackRoleElves.getElve();
    		Elve elve2 = beAttackRoleElves.getElve();
    		
//    		boolean isHit1 = player1.getCurRound().isHit();
//    		boolean isHit2 = player2.getCurRound().isHit();
    		
			boolean isOwnDie = elve1.getIsDie();
//			trace("isOwnDie :"+isOwnDie);
//			trace("Rival HP :"+elve2.getHp());
			boolean isRivalDie = elve2.getIsDie();
//			trace("isRivalDie :"+isRivalDie);
			
		    boolean isOwnWin = false;
		    boolean isRivalWin = false;
			
			//还有没有别的精灵
			boolean ownHasOth = false;
			boolean rivalHasOth = false;
			
			//查找是否还有别的精灵
			Map<String, RoleElves> map1 = player1.getRoleElves();
			for(String elveId : map1.keySet()){
				if(!map1.get(elveId).getElve().getIsDie()){
					//是否还有别的精灵
					ownHasOth = true;
					break;
				}
			}
			Map<String, RoleElves> map2 = player2.getRoleElves();
			for(String elveId : map2.keySet()){
				if(!map2.get(elveId).getElve().getIsDie()){
					//是否还有别的精灵
					rivalHasOth = true;
					break;
				}
			}
			
			JSONObject nextOwnSkill = null;
			JSONObject nextRivalSkill = null;
			
			//0:继续战斗， 1:战斗结束， 100:战斗数据校验出错
			int result = 0;
			//若我没有别的精灵了，判定我方战败
			if(!ownHasOth && rivalHasOth){
				result = 1;
				isOwnWin = false;
				isRivalWin = true;
				Share.matchMap.remove(roomId);
//				trace("Me fail and remove room!");
			}
			
			//若对方没有别的精灵了，判定对方战败
			if(!rivalHasOth && ownHasOth){
				result = 1;
				isOwnWin = true;
				isRivalWin = false;
				Share.matchMap.remove(roomId);
//				trace("Rival fail and remove room!");
			}
			
			//若都有别的精灵，继续战斗
			if(ownHasOth && rivalHasOth){
				result = 0;
				isOwnWin = false;
				isRivalWin = false;
//				trace("continue fight!");
			}	
			
			if(player2.isOffline() && !player2.isRobot() && player2.getRound()-player2.getDisConRound()>=5){
				result = 1;
				isOwnWin = true;
				isRivalWin = false;
//				trace("Rival fail and remove room!");
				send("ResBattleRivalRun", null, user);

				Share.matchMap.remove(roomId);
			}
			
			//若精灵死亡，特殊技能清空，可以重新选择技能
			if(!isOwnDie){
				nextOwnSkill = RoundOverResultService.nextSkill(player1);
			}
			
			if(!isRivalDie){
				nextRivalSkill = RoundOverResultService.nextSkill(player2);
			}

			JSONObject jo = new JSONObject();
			jo.put("result", result);
			jo.put("isWin", isOwnWin);
			jo.put("nextOwnSkill", nextOwnSkill);
			jo.put("nextRivalSkill", nextRivalSkill);
			jo.put("isOwnDie", isOwnDie);
			jo.put("isRivalDie", isRivalDie);
			jo.put("round", round);
			resParams.putUtfString("Infor", jo.toString());
			send("ResRoundOverResult", resParams, user);
			
			if(!player2.isOffline()){
				JSONObject jo1 = new JSONObject();
				jo1.put("result", result);
				jo1.put("isWin", isRivalWin);
				jo1.put("nextOwnSkill", nextRivalSkill);
				jo1.put("nextRivalSkill", nextOwnSkill);
				jo1.put("isOwnDie", isRivalDie);
				jo1.put("isRivalDie", isOwnDie);
				jo1.put("round", round);
				resParams.putUtfString("Infor", jo1.toString());
				send("ResRoundOverResult", resParams, player2.getUser());
				
			}
			
			player1.setPlayOver(true);
			player2.setPlayOver(true);
				
			player1.setRound(round);
			player2.setRound(round);
//			trace("round :"+round);
//			trace("player1.round :"+player1.getRound());
    	
			/*
			 * 判断下一回合双方是不是特殊技能，若是多回合技能，就直接计算
			 */
			if(nextOwnSkill != null && nextRivalSkill != null && result != 1){
				
				JSONArray joAll = RoundOverResultService.nextBattleRoundResult(player1, player2, nextOwnSkill, nextRivalSkill);
				
				player1.setBattleRoundResult(joAll);
				player2.setBattleRoundResult(joAll);
//				Map<Integer, JSONArray> battleRoundResultMap = new HashMap<Integer, JSONArray>();
//				battleRoundResultMap.put(round, joAll);
//				player1.setBattleRoundResultMap(battleRoundResultMap);
//				player2.setBattleRoundResultMap(battleRoundResultMap);
				player1.setPlayOver(false);
				player2.setPlayOver(false);
				
//				JSONObject jsonRoundCE = player2.getCurRound().getRoundChildEffect();
				JSONObject joObject = new JSONObject();
//				joObject.put("roundChildEffect", jsonRoundCE);
				joObject.put("battleRoundResult", joAll);
				resParams.putUtfString("Infor", joObject.toString());
				send("ResBattleRoundResult", resParams, player1.getUser());
				if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
					send("ResBattleRoundResult", resParams, player2.getUser());
				}
				//对手断线重连
				if(!player2.isOffline() && player2.isConBack() && !player2.isBattleReconData()){
//					trace("player2 is connect back!");
					jo = FightingDataService.conBattleRoundResult(player2, player1);
					
					resParams.putUtfString("Infor", jo.toString());
					send("ResReconBattleRoundResult", resParams, user);
					//发完ResReconBattleRoundResult，修改conback
					player2.setConBack(false);
				}
			}
			
			//记得清空回合数据
	    	player1.setJoRound(null);
	    	player2.setJoRound(null);
	    	player1.setBattleRoundResult(null);
	    	player2.setBattleRoundResult(null);
	    	player1.getCurRound().setBeChangeRoleElves(null);
	    	player2.getCurRound().setBeChangeRoleElves(null);
	    	player1.setMustLeaveJO(null);
	    	player2.setMustLeaveJO(null);
//	    	player1.setBattleRoundResultMap(null);
//	    	player2.setBattleRoundResultMap(null);
	    	
	    	if(nextOwnSkill == null && result != 1){
//	    		trace("nextOwnSkill = null");
	    		player1.getCurRound().setResultType(10);
	    	}
	    	if(nextRivalSkill == null && result != 1){
//	    		trace("nextRivalSkill = null");
	    		player2.getCurRound().setResultType(10);
	    		
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		
//	    		trace(player2.getUser().getName()+" player2 isOffline :"+player2.isOffline());
	    		/*
	    		 * 离线判断
	    		 * 若离线，得自动选择技能，或者更换精灵
	    		 */
	    		if(player2.isOffline()){
//	    			trace("player2 isOffline!");
	    			JSONArray joAll = null;
    				
    				//判断当前精灵是否死亡
    				if(isRivalDie){
//    					trace("player2 now elve isDie!");
    					//1.player2先自动换一个活着的精灵
    					jo = DisconService.dieChangeElve(player2, player1);
    					if(jo != null){
    						resParams.putUtfString("Infor", jo.toString());
    						send("ResRivalChangeElves", resParams, user);
    					}
		  				
    				}
    				
					// 2.当使用的是多回合技能时，离线的一方自动选技能
    				if(player1.getCurRound().getResultType() == 1 && !player1.getCurRound().getNowRoleElves().getElve().getIsDie()){
    					jo = SkillService.autoChooseSkill(player2, player1);
        				resParams.putUtfString("Infor", jo.toString());
        				//将数据发给在线的玩家
        				send("ResRivalUseSkill", resParams, player1.getUser());
        				
        				//若对手上回合使用多回合技能，就直接计算
        				if(player1.getCurRound().getResultType() == 1){
        					joAll = RoundResultService.SpeciesJudgeSkill(player1, player2);
        				}
    	    			
    	    			if(joAll != null){
    	    				player1.setBattleRoundResult(joAll);
    	    				player2.setBattleRoundResult(joAll);
//    	    				Map<Integer, JSONArray> battleRoundResultMap = new HashMap<Integer, JSONArray>();
//    	    				battleRoundResultMap.put(round, joAll);
//    	    				player1.setBattleRoundResultMap(battleRoundResultMap);
//    	    				player2.setBattleRoundResultMap(battleRoundResultMap);
    	    				player1.setPlayOver(false);
    	    				player2.setPlayOver(false);
    	    				
    	    				//joAll != null 说明对手本回合有动作
    	    				JSONObject joObject = new JSONObject();
//    		    			joObject.put("roundChildEffect", player1.getCurRound().getRoundChildEffect());
    	    				joObject.put("battleRoundResult", joAll);
    	    				resParams.putUtfString("Infor", joObject.toString());
    	    				send("ResBattleRoundResult", resParams, user);
    	    			}
    				}
	    		}
		    }
	    	
	    	/*
	    	 * 若战斗结束，返回用户战斗结果数据
	    	 */
	    	if(result == 1){
	    		JSONObject playerJO1 = new JSONObject();
				JSONObject playerJO2 = new JSONObject();
				JSONArray playerJA = new JSONArray();
				
//				trace("-----serverURL:"+Share.serverURL.get(player1.getUid()+"")+"-----");

				playerJO1.put("uid", player1.getUid()+"");
				playerJO1.put("isWin", isOwnWin);
				playerJO1.put("url", Share.serverURL.get(player1.getUid()+""));
//				trace("-----PVP result :"+playerJO1+"-----");
				playerJA.add(playerJO1);
				if(Share.serverURL.get(player2.getUid()+"") != null){
					playerJO2.put("uid", player2.getUid()+"");
					playerJO2.put("isWin", isRivalWin);
					playerJO2.put("url", Share.serverURL.get(player2.getUid()+""));
					playerJA.add(playerJO2);
				}
				
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
//						trace("resultJO :"+resultJO);
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
//						int winRank = winsRankCom(winPoint);
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
//						trace("not get and status : 1");
						sendJO.put("status", 1);//1：获取失败
						sendJO.put("PVPTimes", 0);
						sendJO.put("winTimes", 0);
						sendJO.put("winRate", 0);
						sendJO.put("winPoint", 0);
						sendJO.put("winRank", 0);
						sendJO.put("isWinRankUp", false);
					}
					resParams.putUtfString("Infor", sendJO.toString());
					if(playerJO.getString("uid").equals(player1.getUid()+"")){
						send("ResBattleOverResult", resParams, player1.getUser());
					}
					else if(playerJO.getString("uid").equals(player2.getUid()+"") && !player2.isOffline()){
						send("ResBattleOverResult", resParams, player2.getUser());
					}
					
				}
	    	}
    	}
    	/*
    	 * 若校验错误，返回错误数据
    	 */
    	if(!isComOk && !player2.isOffline() && player2.getJoRound()!= null){
    		int result = 100;
    		JSONObject jo = new JSONObject();
			jo.put("result", result);
			jo.put("reason", compareResult);
			jo.put("jo1", player1.getJoRound());
			jo.put("jo2", player2.getJoRound());
    		resParams.putUtfString("Infor", jo.toString());
			send("ResRoundOverResult", resParams, user);
			send("ResRoundOverResult", resParams, player2.getUser());
			trace("jo1 :"+ player1.getJoRound());
			trace("jo2 :"+ player2.getJoRound());
			Share.matchMap.remove(roomId);
    	}
	}
	
	private String compare(JSONObject joRound1, JSONObject joRound2){
		JSONObject joR1E1Wea = null;
		if(!joRound1.get("weatherInfor").equals("")){
			joR1E1Wea = joRound1.getJSONObject("weatherInfor");
		}
		JSONObject joR2E1Wea = null;
		if(!joRound2.get("weatherInfor").equals("")){
			joR2E1Wea = joRound2.getJSONObject("weatherInfor");
		}
		
		JSONObject joRound1Elve1 = joRound1.getJSONObject("elves1");
		JSONObject joR1E1Info = joRound1Elve1.getJSONObject("elvesDataInforVo");
		JSONArray joR1E1Effect = joRound1Elve1.getJSONArray("childEffectList");
		JSONArray joR1E1Buff = joRound1Elve1.getJSONArray("stateSkillEffectList");
		int joR1E1Ran = joRound1Elve1.getInt("randomIndex");
		
		JSONObject joRound1Elve2 = joRound1.getJSONObject("elves2");
		JSONObject joR1E2Info = joRound1Elve2.getJSONObject("elvesDataInforVo");
		JSONArray joR1E2Effect = joRound1Elve2.getJSONArray("childEffectList");
		JSONArray joR1E2Buff = joRound1Elve2.getJSONArray("stateSkillEffectList");
		int joR1E2Ran = joRound1Elve2.getInt("randomIndex");
		
		JSONObject joRound2Elve1 = joRound2.getJSONObject("elves1");
		JSONObject joR2E1Info = joRound2Elve1.getJSONObject("elvesDataInforVo");
		JSONArray joR2E1Effect = joRound2Elve1.getJSONArray("childEffectList");
		JSONArray joR2E1Buff = joRound2Elve1.getJSONArray("stateSkillEffectList");
		int joR2E1Ran = joRound2Elve1.getInt("randomIndex");
		
		JSONObject joRound2Elve2 = joRound2.getJSONObject("elves2");
		JSONObject joR2E2Info = joRound2Elve2.getJSONObject("elvesDataInforVo");
		JSONArray joR2E2Effect = joRound2Elve2.getJSONArray("childEffectList");
		JSONArray joR2E2Buff = joRound2Elve2.getJSONArray("stateSkillEffectList");
		int joR2E2Ran = joRound2Elve2.getInt("randomIndex");
		
		//找出id一样的精灵
		//joRound1Elve1
		String result1 = null;
		String result2 = null;
		String result3 = null;
		String result4 = null;
		String result5 = null;
		if(joR1E1Info.getString("eLvesID").equals(joR2E1Info.getString("eLvesID")) 
				&& joR1E1Info.getInt("gender")==joR2E1Info.getInt("gender")
				&& joR1E1Info.getInt("character")==joR2E1Info.getInt("character") 
				&& joR1E1Info.getInt("sameIndex")==joR2E1Info.getInt("sameIndex")){
			//info对比
//			trace("joR1E1Info, joR2E1Info");
			result1 = compareInfo(joR1E1Info, joR2E1Info);
			//effect对比
			result2 = compareEffect(joR1E1Effect, joR2E1Effect);
			result3 = compareRanIndex(joR1E1Ran, joR2E1Ran);
			if(joR1E1Buff.size() >0 && joR2E1Buff.size() >0){
				result4 = compareBuff(joR1E1Buff, joR2E1Buff);
			}
		}else{
//			trace("joR1E1Info, joR2E2Info");
			result1 = compareInfo(joR1E1Info, joR2E2Info);
			result2 = compareEffect(joR1E1Effect, joR2E2Effect);
			result3 = compareRanIndex(joR1E1Ran, joR2E2Ran);
			if(joR1E1Buff.size() >0 && joR2E2Buff.size() >0){
				result4 = compareBuff(joR1E1Buff, joR2E2Buff);
			}
		}
		
		//joRound1Elve2
		if(joR1E2Info.getString("eLvesID").equals(joR2E1Info.getString("eLvesID")) 
				&& joR1E2Info.getInt("gender")==joR2E1Info.getInt("gender")
				&& joR1E2Info.getInt("character")==joR2E1Info.getInt("character") 
				&& joR1E2Info.getInt("sameIndex")==joR2E1Info.getInt("sameIndex")){
//			trace("joR1E2Info, joR2E1Info");
//			trace("eLve id :"+joR1E2Info.getString("eLvesID")+":"+joR2E1Info.getString("eLvesID"));
			result1 = compareInfo(joR1E2Info, joR2E1Info);
			result2 = compareEffect(joR1E2Effect, joR2E1Effect);
			result3 = compareRanIndex(joR1E2Ran, joR2E1Ran);
			if(joR1E2Buff.size() >0 && joR2E1Buff.size() >0){
				result4 = compareBuff(joR1E2Buff, joR2E1Buff);
			}
			
		}else{
//			trace("joR1E2Info, joR2E2Info");
			result1 = compareInfo(joR1E2Info, joR2E2Info);
			result2 = compareEffect(joR1E2Effect, joR2E2Effect);
			result3 = compareRanIndex(joR1E2Ran, joR2E2Ran);
			if(joR1E2Buff.size() >0 && joR2E2Buff.size() >0){
				result4 = compareBuff(joR1E2Buff, joR2E2Buff);
			}
		}
		
		if(joR1E1Wea!=null && joR2E1Wea!=null){
			result5 = compareWea(joR1E1Wea, joR2E1Wea);
		}
		
//		trace("eLve id :"+joR1E2Info.getString("eLvesID")+":"+joR2E1Info.getString("eLvesID"));
		if(!result1.equals("OK")){
//			trace("info not right!");
			return result1;
		}
		if(!result2.equals("OK")){
//			trace("effect not right!");
			return result2;
		}
		if(!result3.equals("OK")){
//			trace("randomIndex not right!");
			return result3;
		}
		if(result4 != null){
			if(!result4.equals("OK")){
//				trace("buff_id not right!");
				return result4;
			}
		}
		if(result5 != null){
			if(!result5.equals("OK")){
//				trace("weather not right!");
				return result5;
			}
		}
		
		return "OK";
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private String compare3(JSONObject joRound1, JSONObject joRound2){
//		trace("joRound1 :"+joRound1);
//		trace("joRound2 :"+joRound2);
		
		JSONObject joR1E1Wea = null;
		if(!joRound1.get("weatherInfor").equals("")){
//			trace(joRound1.get("weatherInfor"));
			joR1E1Wea = joRound1.getJSONObject("weatherInfor");
		}
		JSONObject joR2E1Wea = null;
		if(!joRound2.get("weatherInfor").equals("")){
//			trace(joRound2.get("weatherInfor"));
			joR2E1Wea = joRound2.getJSONObject("weatherInfor");
		}
		
		String result1 = null;
 		String result2 = null;
 		String result3 = null;
 		String result4 = null;
 		String result5 = null;
 		
 		Iterator it1 = joRound1.keys();
		while(it1.hasNext()){
			String key1 = (String) it1.next(); 
			if(key1.equals("weatherInfor")){
				continue;
			}
            JSONObject joRound1Elve1 = joRound1.getJSONObject(key1);  
            JSONObject joR1E1Info = joRound1Elve1.getJSONObject("elvesDataInforVo");
     		JSONArray joR1E1Effect = joRound1Elve1.getJSONArray("childEffectList");
     		JSONArray joR1E1Buff = null;
     		if(joRound1Elve1.getJSONArray("stateSkillEffectList").size() != 0){
     			joR1E1Buff = joRound1Elve1.getJSONArray("stateSkillEffectList");
     		}
     		int joR1E1Ran = joRound1Elve1.getInt("randomIndex");
     		
     		Iterator it2 = joRound2.keys();
            while(it2.hasNext()){
            	String key2 = (String) it2.next(); 
            	if(key2.equals("weatherInfor")){
    				continue;
    			}
                JSONObject joRound2Elve1 = joRound2.getJSONObject(key2); 
                JSONObject joR2E1Info = joRound2Elve1.getJSONObject("elvesDataInforVo");
        		JSONArray joR2E1Effect = joRound2Elve1.getJSONArray("childEffectList");
        		JSONArray joR2E1Buff = null;
        		if(joRound2Elve1.getJSONArray("stateSkillEffectList").size() != 0){
        			joR2E1Buff = joRound2Elve1.getJSONArray("stateSkillEffectList");
        		}
        		int joR2E1Ran = joRound2Elve1.getInt("randomIndex");
                
         		if(joR1E1Info.getString("eLvesID").equals(joR2E1Info.getString("eLvesID")) 
         				&& joR1E1Info.getInt("gender")==joR2E1Info.getInt("gender")
         				&& joR1E1Info.getInt("character")==joR2E1Info.getInt("character") 
         				&& joR1E1Info.getInt("sameIndex")==joR2E1Info.getInt("sameIndex")){
         			//info对比
//         			trace("joR1E1Info, joR2E1Info");
         			result1 = compareInfo(joR1E1Info, joR2E1Info);
         			//effect对比
         			result2 = compareEffect(joR1E1Effect, joR2E1Effect);
         			result3 = compareRanIndex(joR1E1Ran, joR2E1Ran);
         			if(joR1E1Buff!=null && joR2E1Buff!=null && joR1E1Buff.size()>0 && joR2E1Buff.size()>0){
         				result4 = compareBuff(joR1E1Buff, joR2E1Buff);
         			}
         			break;
         		}
            }
            if(joR1E1Wea!=null && joR2E1Wea!=null){
    			result5 = compareWea(joR1E1Wea, joR2E1Wea);
    		}
            
            if(!result1.equals("OK")){
//     			trace("info not right!");
     			return result1;
     		}
     		if(!result2.equals("OK")){
//     			trace("effect not right!");
     			return result2;
     		}
     		if(!result3.equals("OK")){
//     			trace("randomIndex not right!");
     			return result3;
     		}
     		if(result4 != null){
     			if(!result4.equals("OK")){
//     				trace("buff_id not right!");
     				return result4;
     			}
     		}
     		if(result5 != null){
    			if(!result5.equals("OK")){
//    				trace("weather not right!");
    				return result5;
    			}
    		}
		}
		return "OK";
	}
	
	private String compareWea(JSONObject jo1, JSONObject jo2){
		int round1 = jo1.getInt("round");
		int round2 = jo2.getInt("round");
		
		int index1 = jo1.getInt("index");
		int index2 = jo2.getInt("index");
		
		int priority1 = jo1.getInt("priority");
		int priority2 = jo2.getInt("priority");
		
		if(round1 != round2){
			return "round :"+round1+":"+round2;
		}
		
		if(index1 != index2){
			return "index :"+index1+":"+index2;
		}
		
		if(priority1 != priority2){
			return "priority :"+priority1+":"+priority2;
		}
		return "OK";
	}
	
	private String compareBuff(JSONArray ja1, JSONArray ja2){
		String result;
		if(ja1.size() != ja2.size()){
			return "buff lost!";
		}
		for(int i=0;i<ja1.size();i++){
			JSONObject jo1 = ja1.getJSONObject(i);
			for(int j=0;j<ja2.size();j++){
				JSONObject jo2 = ja2.getJSONObject(j);
				result = compareBuffId(jo1, jo2);
				if(!result.equals("OK")){
					break;
				}
			}
		}
		return "OK";
	}
	
	private String compareBuffId(JSONObject jo1, JSONObject jo2){
		String buffId1 = jo1.getJSONObject("buffDataVo").getString("buff_id");
		String buffId2 = jo2.getJSONObject("buffDataVo").getString("buff_id");
		if(buffId1.equals(buffId2)){
			return "buff_id :"+buffId1+":"+buffId2;
		}
		return "OK";
	}
	
	private String compareRanIndex(int ranIndex1, int ranIndex2){
		if(ranIndex1 != ranIndex2){
			return "randomIndex :"+ranIndex1+":"+ranIndex2;
		}
		return "OK";
	}
	
	//对比玩家基本数据
	private String compareInfo(JSONObject jo1, JSONObject jo2){
//		if(jo1.getInt("character")!=jo2.getInt("character")){
//			return false;
//		}
		if(jo1.getInt("hp")!=jo2.getInt("hp")){
//			trace("hp :"+jo1.getInt("hp")+":"+jo2.getInt("hp"));
			return "hp :"+jo1.getInt("hp")+":"+jo2.getInt("hp");
		}
		if(jo1.getInt("exp")!=jo2.getInt("exp")){
//			trace("exp");
			return "exp :"+jo1.getInt("exp")+":"+jo2.getInt("exp");
		}
		if(jo1.getInt("sameIndex")!=jo2.getInt("sameIndex")){
//			trace("sameIndex");
			return "sameIndex :"+jo1.getInt("sameIndex")+":"+jo2.getInt("sameIndex");
		}
		if(jo1.getInt("hpLevel")!=jo2.getInt("hpLevel")){
//			trace("hpLevel");
			return "hpLevel :"+jo1.getInt("hpLevel")+":"+jo2.getInt("hpLevel");
		}
		if(jo1.getInt("wuAttackLevel")!=jo2.getInt("wuAttackLevel")){
//			trace("wuAttackLevel");
			return "wuAttackLevel :"+jo1.getInt("wuAttackLevel")+":"+jo2.getInt("wuAttackLevel");
		}
		if(jo1.getInt("wuDefenseLevel")!=jo2.getInt("wuDefenseLevel")){
//			trace("wuDefenseLevel");
			return "wuDefenseLevel :"+jo1.getInt("wuDefenseLevel")+":"+jo2.getInt("wuDefenseLevel");
		}
		if(jo1.getInt("teAttackLevel")!=jo2.getInt("teAttackLevel")){
//			trace("teAttackLevel");
			return "teAttackLevel :"+jo1.getInt("teAttackLevel")+":"+jo2.getInt("teAttackLevel");
		}
		if(jo1.getInt("teDefenseLevel")!=jo2.getInt("teDefenseLevel")){
//			trace("teDefenseLevel");
			return "teDefenseLevel :"+jo1.getInt("teDefenseLevel")+":"+jo2.getInt("teDefenseLevel");
		}
		if(jo1.getInt("speedLevel")!=jo2.getInt("speedLevel")){
//			trace("speedLevel");
			return "speedLevel :"+jo1.getInt("speedLevel")+":"+jo2.getInt("speedLevel");
		}
		if(!jo1.getString("carryitem").equals(jo2.getString("carryitem"))){
//			trace("carryitem");
			return "carryitem :"+jo1.getString("carryitem")+":"+jo2.getString("carryitem");
		}
		if(!jo1.getString("carryequip").equals(jo2.getString("carryequip"))){
//			trace("carryequip");
			return "carryequip :"+jo1.getString("carryequip")+":"+jo2.getString("carryequip");
		}
		if(!jo1.getString("features").equals(jo2.getString("features"))){
//			trace("features");
			return "features :"+jo1.getString("features")+":"+jo2.getString("features");
		}
		if(!jo1.getString("eLvesname").equals(jo2.getString("eLvesname"))){
//			trace("eLvesname");
			return "eLvesname :"+jo1.getString("eLvesname")+":"+jo2.getString("eLvesname");
		}
		if(jo1.getInt("rarity")!=jo2.getInt("rarity")){
//			trace("rarity");
			return "rarity :"+jo1.getInt("rarity")+":"+jo2.getInt("rarity");
		}
//		if(!jo1.getString("eLvesID").equals(jo2.getString("eLvesID"))){
//			return false;
//		}
		if(jo1.getInt("level")!=jo2.getInt("level")){
//			trace("level");
			return "level :"+jo1.getInt("level")+":"+jo2.getInt("level");
		}
		if(jo1.getInt("upgradeLv")!=jo2.getInt("upgradeLv")){
//			trace("upgradeLv");
			return "upgradeLv :"+jo1.getInt("upgradeLv")+":"+jo2.getInt("upgradeLv");
		}
		if(jo1.getInt("breakthroughLevel")!=jo2.getInt("breakthroughLevel")){
//			trace("breakthroughLevel");
			return "breakthroughLevel :"+jo1.getInt("breakthroughLevel")+":"+jo2.getInt("breakthroughLevel");
		}
		if(jo1.getInt("trainedLevel")!=jo2.getInt("trainedLevel")){
//			trace("trainedLevel");
			return "trainedLevel :"+jo1.getInt("trainedLevel")+":"+jo2.getInt("trainedLevel");
		}
		if(jo1.getBoolean("isTraining")!=jo2.getBoolean("isTraining")){
//			trace("isTraining");
			return "isTraining :"+jo1.getBoolean("isTraining")+":"+jo2.getBoolean("isTraining");
		}
		if(jo1.getBoolean("isBag")!=jo2.getBoolean("isBag")){
//			trace("isBag");
			return "isTraining :"+jo1.getBoolean("isTraining")+":"+jo2.getBoolean("isTraining");
		}
		if(jo1.getBoolean("isSpirteLeague")!=jo2.getBoolean("isSpirteLeague")){
//			trace("isSpirteLeague");
			return "isSpirteLeague :"+jo1.getBoolean("isSpirteLeague")+":"+jo2.getBoolean("isSpirteLeague");
		}
		if(jo1.getBoolean("isMining")!=jo2.getBoolean("isMining")){
//			trace("isMining");
			return "isMining :"+jo1.getBoolean("isMining")+":"+jo2.getBoolean("isMining");
		}
		if(jo1.getBoolean("isLock")!=jo2.getBoolean("isLock")){
//			trace("isLock");
			return "isLock :"+jo1.getBoolean("isLock")+":"+jo2.getBoolean("isLock");
		}
		if(jo1.getBoolean("isDie")!=jo2.getBoolean("isDie")){
//			trace("isDie");
			return "isDie :"+jo1.getBoolean("isDie")+":"+jo2.getBoolean("isDie");
		}
		if(!jo1.getString("mega").equals(jo2.getString("mega"))){
//			trace("mega");
			return "mega :"+jo1.getString("mega")+":"+jo2.getString("mega");
		}
		if(!jo1.getString("studySkillList").equals(jo2.getString("studySkillList"))){
//			trace("studySkillList");
			return "studySkillList :"+jo1.getString("studySkillList")+":"+jo2.getString("studySkillList");
		}
//		if(jo1.getInt("gender")!=jo2.getInt("gender")){
//			return false;
//		}
		if(!jo1.getString("own").equals(jo2.getString("own"))){
//			trace("own");
			return "own :"+jo1.getString("own")+":"+jo2.getString("own");
		}
		return "OK";
	}
	
	//对比子效果数据
	private String compareEffect(JSONArray ja1, JSONArray ja2){
		String result = "OK";
//		for(int i=0;i<ja1.size();i++){
//			JSONObject jo1 = ja1.getJSONObject(i);
//			int id = jo1.getInt("id");
//			for(int j=0;j<ja2.size();j++){
//				JSONObject jo2 = ja2.getJSONObject(j);
//				if(jo2.getInt("id") == id){
//					result = compareEffect2(jo1, jo2);
//					if(!result.equals("OK")){
//						return result;
//					}
//				}
//			}
//		}
		
		//由于会存在两个子效果ID相同的情况，所以只能用双for循环来遍历
		for(int i=0;i<ja1.size();i++){
			JSONObject jo1 = ja1.getJSONObject(i);
			for(int j=0;j<ja2.size();j++){
				JSONObject jo2 = ja2.getJSONObject(j);
				result = compareEffect2(jo1, jo2);
				if(result.equals("OK")){
					break;
				}
			}
		}
		return result;
	}
	private String compareEffect2(JSONObject jo1, JSONObject jo2){
		if(!jo1.getString("name").equals(jo2.getString("name"))){
//			trace("name");
			return "name :"+jo1.getString("name")+":"+jo2.getString("name");
		}
		if(jo1.getInt("effectRate")!=jo2.getInt("effectRate")){
//			trace("effectRate");
			return "effectRate :"+jo1.getInt("effectRate")+":"+jo2.getInt("effectRate");
		}
		if(!jo1.getString("targetValue").equals(jo2.getString("targetValue"))){
//			trace("targetValue");
			return "targetValue :"+jo1.getString("targetValue")+":"+jo2.getString("targetValue");
		}
		if(jo1.getInt("target")!=jo2.getInt("target")){
//			trace("target");
			return "target :"+jo1.getInt("target")+":"+jo2.getInt("target");
		}
		if(!jo1.getString("parentObjId").equals(jo2.getString("parentObjId"))){
//			trace("parentObjId");
			return "parentObjId :"+jo1.getString("parentObjId")+":"+jo2.getString("parentObjId");
		}
		if(jo1.getInt("effectiveTimeId")!=jo2.getInt("effectiveTimeId")){
//			trace("effectiveTimeId");
			return "effectiveTimeId :"+jo1.getInt("effectiveTimeId")+":"+jo2.getInt("effectiveTimeId");
		}
		if(jo1.getInt("take")!=jo2.getInt("take")){
//			trace("take");
			return "take :"+jo1.getInt("take")+":"+jo2.getInt("take");
		}
		if(!jo1.getString("takeValue").equals(jo2.getString("takeValue"))){
//			trace("takeValue");
			return "takeValue :"+jo1.getString("takeValue")+":"+jo2.getString("takeValue");
		}
		if(jo1.getBoolean("isOnce")!=jo2.getBoolean("isOnce")){
//			trace("isOnce");
			return "isOnce :"+jo1.getBoolean("isOnce")+":"+jo2.getBoolean("isOnce");
		}
		if(jo1.getBoolean("isTriggerLineOne")!=jo2.getBoolean("isTriggerLineOne")){
//			trace("isTriggerLineOne");
			return "isTriggerLineOne :"+jo1.getBoolean("isTriggerLineOne")+":"+jo2.getBoolean("isTriggerLineOne");
		}
		if(!jo1.getString("triggerFailLine").equals(jo2.getString("triggerFailLine"))){
//			trace("triggerFailLine");
			return "triggerFailLine :"+jo1.getString("triggerFailLine")+":"+jo2.getString("triggerFailLine");
		}
		if(jo1.getBoolean("isCanntAction")!=jo2.getBoolean("isCanntAction")){
//			trace("isCanntAction");
			return "isCanntAction :"+jo1.getBoolean("isCanntAction")+":"+jo2.getBoolean("isCanntAction");
		}
		if(jo1.getBoolean("isSkillFail")!=jo2.getBoolean("isSkillFail")){
//			trace("isSkillFail");
			return "isSkillFail :"+jo1.getBoolean("isSkillFail")+":"+jo2.getBoolean("isSkillFail");
		}
		if(jo1.getBoolean("isCantChangeElves")!=jo2.getBoolean("isCantChangeElves")){
//			trace("isCantChangeElves");
			return "isCantChangeElves :"+jo1.getBoolean("isCantChangeElves")+":"+jo2.getBoolean("isCantChangeElves");
		}
		if(jo1.getBoolean("isCantLeave")!=jo2.getBoolean("isCantLeave")){
//			trace("isCantLeave");
			return "isCantLeave :"+jo1.getBoolean("isCantLeave")+":"+jo2.getBoolean("isCantLeave");
		}
		if(jo1.getBoolean("isMustLeave")!=jo2.getBoolean("isMustLeave")){
//			trace("isMustLeave");
			return "isMustLeave :"+jo1.getBoolean("isMustLeave")+":"+jo2.getBoolean("isMustLeave");
		}
		if(jo1.getBoolean("isMustHit")!=jo2.getBoolean("isMustHit")){
//			trace("isMustHit");
			return "isMustHit :"+jo1.getBoolean("isMustHit")+":"+jo2.getBoolean("isMustHit");
		}
		if(jo1.getBoolean("isMustBeHit")!=jo2.getBoolean("isMustBeHit")){
//			trace("isMustBeHit");
			return "isMustBeHit :"+jo1.getBoolean("isMustBeHit")+":"+jo2.getBoolean("isMustBeHit");
		}
		if(jo1.getBoolean("isCanntResetHp")!=jo2.getBoolean("isCanntResetHp")){
//			trace("isCanntResetHp");
			return "isCanntResetHp :"+jo1.getBoolean("isCanntResetHp")+":"+jo2.getBoolean("isCanntResetHp");
		}
		if(jo1.getBoolean("isCannotFeatures")!=jo2.getBoolean("isCannotFeatures")){
//			trace("isCannotFeatures");
			return "isCannotFeatures :"+jo1.getBoolean("isCannotFeatures")+":"+jo2.getBoolean("isCannotFeatures");
		}
		if(jo1.getBoolean("isCanntChangeLevel")!=jo2.getBoolean("isCanntChangeLevel")){
//			trace("isCanntChangeLevel");
			return "isCanntChangeLevel :"+jo1.getBoolean("isCanntChangeLevel")+":"+jo2.getBoolean("isCanntChangeLevel");
		}
		if(jo1.getBoolean("isElvesAttributeFail")!=jo2.getBoolean("isElvesAttributeFail")){
//			trace("isElvesAttributeFail");
			return "isElvesAttributeFail :"+jo1.getBoolean("isElvesAttributeFail")+":"+jo2.getBoolean("isElvesAttributeFail");
		}
		if(jo1.getBoolean("isKeepOneHp")!=jo2.getBoolean("isKeepOneHp")){
//			trace("isKeepOneHp");
			return "isKeepOneHp :"+jo1.getBoolean("isKeepOneHp")+":"+jo2.getBoolean("isKeepOneHp");
		}
		if(jo1.getBoolean("isCanntChangeSkill")!=jo2.getBoolean("isCanntChangeSkill")){
//			trace("isCanntChangeSkill");
			return "isCanntChangeSkill :"+jo1.getBoolean("isCanntChangeSkill")+":"+jo2.getBoolean("isCanntChangeSkill");
		}
		if(jo1.getBoolean("isCanntVital")!=jo2.getBoolean("isCanntVital")){
//			trace("isCanntVital");
			return "isCanntVital :"+jo1.getBoolean("isCanntVital")+":"+jo2.getBoolean("isCanntVital");
		}
		if(jo1.getBoolean("isStopOneAtk")!=jo2.getBoolean("isStopOneAtk")){
//			trace("isStopOneAtk");
			return "isStopOneAtk :"+jo1.getBoolean("isStopOneAtk")+":"+jo2.getBoolean("isStopOneAtk");
		}
		if(jo1.getBoolean("isCouSameSkill")!=jo2.getBoolean("isCouSameSkill")){
//			trace("isCouSameSkill");
			return "isCouSameSkill :"+jo1.getBoolean("isCouSameSkill")+":"+jo2.getBoolean("isCouSameSkill");
		}
		if(jo1.getBoolean("isOnlyLastSkill")!=jo2.getBoolean("isOnlyLastSkill")){
//			trace("isOnlyLastSkill");
			return "isOnlyLastSkill :"+jo1.getBoolean("isOnlyLastSkill")+":"+jo2.getBoolean("isOnlyLastSkill");
		}
		if(jo1.getBoolean("isChangeTarget")!=jo2.getBoolean("isChangeTarget")){
//			trace("isChangeTarget");
			return "isChangeTarget :"+jo1.getBoolean("isChangeTarget")+":"+jo2.getBoolean("isChangeTarget");
		}
		return "OK";
	}
	
}
