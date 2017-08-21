package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.pvp.service.FightingDataService;
import pocket.total.entity.Share;

/**
 * 请求重连战斗回合数据处理类
 * <p>Title: ReconBattleRoundResultEventHandler<／p>
 * <p>Description: 1.服务端发送ResBattleRoundResult，还未发送ResRoundOverResult时，断线一方已经重连了，
 * 					   那么服务端要等待重连一方也播放一次回合数据（ResReconBattleRoundResult），双发都发送了ReqRoundOverResult才是回合结束
				   2.已经发送了ResRoundOverResult，那么断链一方进入游戏后等待下回合数据，下发ResBattleRoundResult时播放战斗数据（ResReconBattleRoundResult）<／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class ReconBattleRoundResultEventHandler extends BaseClientRequestHandler {
	
	/*
	 * "ownSkill"://返回的具体技能的数据
	    "rivalSkill"://返回的具体技能的数据
	    "ownElvesKey":12306_1_1_0//我方精灵id
	    "rivalElvesKey":12306_1_1_0//对方精灵id
	    "battleRoundResult":同接口ResBattleRoundResult//战斗回合数据
	 */
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start ReconBattleRoundResult");
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
		Player player2 = null;
		if(list != null){
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
		}
		
		//发送了ReqBattleReconData请求后，才能返回ResReconBattleRoundResult
		player1.setBattleReconData(false);	
		player1.setConBack(true);
//		player1.setOffline(false);
		
		if(player2.getBattleRoundResult() != null){
			
			JSONObject jo = FightingDataService.conBattleRoundResult(player1, player2);
			
			ISFSObject resParams = new SFSObject();
			resParams.putUtfString("Infor", jo.toString());
			send("ResReconBattleRoundResult", resParams, user);
			
			player1.setConBack(false);
			
//			trace("send ReconBattleRoundResult!");
			
			if(player1.getMustLeaveJO()!=null){
				resParams.putUtfString("Infor", player1.getMustLeaveJO().toString());
				send("ResRivalChangeElves", resParams, player2.getUser());
				player1.setMustLeaveJO(null);
				
				player1.setMustLeaveOver(false);
			}

		}
	}

}
