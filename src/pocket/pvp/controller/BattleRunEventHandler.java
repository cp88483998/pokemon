package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.total.entity.Share;
import pocket.total.util.PVPResult;
/**
 * 请求战斗中逃跑
 * ReqBattleRun
 * @author 陈鹏
 */
public class BattleRunEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("userId :"+user.getId());
//		String infor = params.getUtfString("Infor");
		String roomId = params.getUtfString("roomId");
//		JSONObject jsonObject = JSONObject.fromObject(infor);
//		int elvesId = jsonObject.getInt("elvesId");
		boolean isSuc = false;
//		Random random = new Random();
//		double ran = random.nextInt(100)*0.01;
//		if(ran > 0.5){
			isSuc = true;
//		}
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
		Player player2 = null;
		if(list != null){
			if(isSuc){
			    for(int i=0;i<list.size();i++){
			    	if(list.get(i).isOffline()){
						player2 = list.get(i);
						continue;
					}
					if(list.get(i).getUser().getId() == user.getId()){
						player1 = list.get(i);
						//3:逃跑
						player1.setStatus(3);
					}else{
						player2 = list.get(i);
					}
				}
			}
			JSONObject jo = new JSONObject();
			jo.put("isSuc", isSuc);
			ISFSObject resParams = new SFSObject();
			resParams.putUtfString("Infor", jo.toString());
			send("ResBattleRun", resParams, user);
			if(!player2.isOffline()){
				send("ResBattleRivalRun", resParams, player2.getUser());
			}
			
			Share.matchMap.remove(roomId);
//			trace("matchMap2 size :"+Share.matchMap.size());
//			trace("run ok！");
			
			
			
			JSONObject playerJO1 = new JSONObject();
			JSONObject playerJO2 = new JSONObject();
			JSONArray playerJA = new JSONArray();
//			trace("Share.serverURL :"+Share.serverURL);
			playerJO1.put("uid", player1.getUid()+"");
			playerJO1.put("isWin", false);
			playerJO1.put("url", Share.serverURL.get(player1.getUid()+""));
			playerJA.add(playerJO1);
			if(Share.serverURL.get(player2.getUid()+"") != null){
//				trace(player2.getUid()+"");
//				trace(Share.serverURL.get(player2.getUid()+""));
				playerJO2.put("uid", player2.getUid()+"");
				playerJO2.put("isWin", true);
				playerJO2.put("url", Share.serverURL.get(player2.getUid()+""));
				playerJA.add(playerJO2);
			}
//			trace(playerJA);
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
//				trace("resultJO :"+resultJO);
				sendJO = new JSONObject();
				if(resultJO != null){
//					trace("resultJO :"+resultJO);
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
//					int winRank = winsRankCom(winPoint);
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
//					trace("not get and status : 1");
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
				else if(playerJO.getString("uid").equals(player2.getUid()+"")){
					send("ResBattleOverResult", resParams, player2.getUser());
				}
			}
		}else{
			JSONObject jo = new JSONObject();
			jo.put("isSuc", true);
			ISFSObject resParams = new SFSObject();
			resParams.putUtfString("Infor", jo.toString());
			send("ResBattleRun", resParams, user);
		}
		
		
		
//		Find find = new Find();
//		send("ResBattleRivalRun", resParams, find.findMatch(user).getUser());;
	}
	
}
