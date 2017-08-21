package pocket.pvp.controller;

import java.util.Iterator;
import java.util.List;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.service.DisconService;
import pocket.pvp.service.RoundResultService;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;

/**
 * 断线事件处理类
 * <p>Title: DisconnectEventHandler_hasAuto<／p>
 * <p>Description: 1.若在匹配池中，删除匹配池中玩家
 * 				   2.若在匹配成功池中，给对手发送断线消息
 * 				   3.(1)若对方也掉线，直接移除房间
 * 					 (2)若处于匹配状态，ResRivalRun
 * 					 (3)若处于准备状态，ResRivalBattle<／p>
 * 					 (4)若处于战斗状态，判断我方需要做什么？选技能，更换精灵，取消更换。
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class DisconnectEventHandler_hasAuto extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		User userDiscon = (User) event.getParameter(SFSEventParam.USER);
//		trace("user disconnect :"+userDiscon.getId());
//		trace("user disconnect :"+userDiscon.getName());
		
		int disUid = userDiscon.getId();
		
		//遍历匹配池，找到掉线玩家并删除
		List<Player> list = null;
		boolean hasPlayer = false;
		Iterator<Integer> keys = Share.matchPool.keySet().iterator();
		while(keys.hasNext()){
			Integer key = keys.next();
			list = Share.matchPool.get(key);
			Iterator<Player> iter = list.iterator();
			while(iter.hasNext()){
				Player p = iter.next();
				if(disUid == p.getUser().getId()){
					hasPlayer = true;
					iter.remove();
					break;
				}
			}
			if(hasPlayer){
				break;
			}
		}
		
		//遍历匹配结果map，找到掉线玩家并作自动处理
		boolean hasRoom = false;
		//由于无法获知rommId，只能遍历map去找
		String roomId = null;
		Iterator<String> keys2 = Share.matchMap.keySet().iterator();
		while(keys2.hasNext()){
			String key = keys2.next();
			list = Share.matchMap.get(key);
			Iterator<Player> iter = list.iterator();
			while(iter.hasNext()){
				Player p = iter.next();
				//找到user和对手所在的list
				if(p.isRobot()){
					continue;
				}
				if(disUid == p.getUser().getId()){
					roomId = key;
					hasRoom = true;
					break;
				}
			}
			if(hasRoom){
				break;
			}
		}
		
		if(hasRoom){
			//若玩家处于PVP中，就开启AI模式
			Player player1 = null;
			Player player2 = null;	
			for(int j=0;j<list.size();j++){
				if(list.get(j).isRobot()){
					player2 = list.get(j);
					continue;
				}
				if(list.get(j).getUser().getId() == userDiscon.getId()){
					player1 = list.get(j);
					//设置离线状态
					player1.setOffline(true);
					player1.setDisConRound(player1.getRound());
				}else{
					player2 = list.get(j);
				}
			}
			
			//若对手已经发送了roundOverResult
			if(player2.isOffline()){
				//若对方也掉线了，就直接remove掉
				Share.matchMap.remove(roomId);
			}
			
			//对方在线
			if(!player2.isOffline()){
				ISFSObject resParams = new SFSObject();
				send("ResRivalDisconnect", resParams, player2.getUser());
				
				//若处于匹配状态
				if(player1.getStatus()==1){//1:匹配
					//发给对手
					player1.setStatus(3);//3：逃跑
					send("ResRivalRun", resParams, player2.getUser());
					Share.matchMap.remove(roomId);
					
				}
				
				//若处于准备状态
				if(player1.getStatus()==2){
					player1.setStatus(4);//4：战斗开始
					JSONObject jo = new JSONObject();
					JSONArray ranFloatArr = player1.getCurRound().getNowRoleElves().getRandomList();
					Elve nowElve = player1.getCurRound().getNowRoleElves().getElve();
					String elvesId = nowElve.geteLvesID()+"_"+nowElve.getGender()+"_"+nowElve.getCharacter()+"_"+nowElve.getSameIndex();
					jo.put("elvesId", elvesId);
					jo.put("randomArr", ranFloatArr);
					resParams.putUtfString("Infor", jo.toString());
					//发给对手
					send("ResRivalBattle", resParams, player2.getUser());
				}
				
				//若处于战斗开始状态 
				if(player1.getStatus()==4){
					//若player2在线
		//				trace("player2 is online()");
					
					/*
					 * 判断我方需要做什么？选技能，更换精灵，取消更换。
					 */
					if(!player1.isBattleReady()){
						player1.setBattleReady(true);
						send("ResBattleReady", null, player2.getUser());
					}
					
					if(player2.getJoRound() != null && player1.getJoRound() == null){
		//					trace("disConnect update!");
		//					trace("player2 :"+player2.getJoRound());
						JSONObject jo = DisconService.roundOverResult(player2, player1);
						resParams.putUtfString("Infor", jo.toString());
						send("ResRoundOverResult", resParams, player2.getUser());
						
					}
					
					//1.判断对手是否已经使用了技能或者更换了精灵
					JSONArray joAll = null;
					
					//获取当前的精灵
					boolean isOwnDie = player1.getCurRound().getNowRoleElves().getElve().getIsDie();
					//判断当前精灵是否死亡
					if(isOwnDie){
		//					trace("player1 now elve isDie!");
						JSONObject jo = DisconService.dieChangeElve(player1, player2);
		  				resParams.putUtfString("Infor", jo.toString());
		  				send("ResRivalChangeElves", resParams, player2.getUser());
		  				
					}
					//若对手已经使用了技能或者更换了精灵，那我得自动选精灵或技能
					if(player2.getCurRound().getResultType() != 10 && player2.isPlayOver()){
		//					trace("player2 has use skill or change elve!");
						//2.自动选技能
						JSONObject jo = SkillService.autoChooseSkill(player1, player2);
						resParams.putUtfString("Infor", jo.toString());
						send("ResRivalUseSkill", resParams, player2.getUser());
						
						//若对手上回合使用多回合技能，就直接计算
						if(player2.getCurRound().getResultType() == 1){
							joAll = RoundResultService.SpeciesJudgeSkill(player1, player2);
						}
						if(player2.getCurRound().getResultType() == 0){
							joAll = RoundResultService.SpeciesJudgeCha(player2, player1);
						}
		    			
		    			if(joAll != null){
		    				player1.setBattleRoundResult(joAll);
		    				player2.setBattleRoundResult(joAll);
		    				//理论上joAll != null
		    				JSONObject joObject = new JSONObject();
		//        				joObject.put("roundChildEffect", player1.getCurRound().getRoundChildEffect());
		    				joObject.put("battleRoundResult", joAll);
		    				resParams.putUtfString("Infor", joObject.toString());
		    				send("ResBattleRoundResult", resParams, player2.getUser());
		//        				trace("ResBattleRoundResult send to vital!");
		    			}
					}
					//2.mustLeave播放完毕
					if(player2.isMustLeaveOver()){
						SkillService.autoChooseRoleElves(player1);
		//					ChangeElvesEventHandler_hasAuto.updateOldRoleElves(rivalElvesJO, player2);
						
						//发给对手
						JSONObject jo2 = new JSONObject();
		//					trace("rival change roleElve :"+player2.getCurRound().getNowRoleElves().getElve().toString());
						jo2.put("elves", player1.getCurRound().getNowRoleElves().getElve());
						jo2.put("isUseSkill", true);
						jo2.put("isFreeChangeElve", false);
						jo2.put("randomArr", player1.getCurRound().getNowRoleElves().getRandomList());
						resParams.putUtfString("Infor", jo2.toString());
						send("ResRivalChangeElves", resParams, player2.getUser());
						
						player2.setMustLeaveOver(false);
					}
					//3.免费更换进会
					if(player1.isFreeChangeElve()){
						send("ResRivalCanelChangeElves", null, player2.getUser());
						player1.setFreeChangeElve(false);
					}
				}
			}	
		}
		
		
		//删除ServerUrl池中的user信息
		Share.serverURL.remove(userDiscon.getName());
	}
	
}
