package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

/**
 * 请求战斗开始
 * ReqBattle
 * @author 陈鹏
 *
 */
public class BattleEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start battleEventHandler :"+user.getName());
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		ISFSObject resParams = new SFSObject();
		JSONObject jo = new JSONObject();
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
		//4:战斗
		JSONArray ranFloatArr = player1.getCurRound().getNowRoleElves().getRandomList();
		player1.setStatus(4);
		Elve nowElve = player1.getCurRound().getNowRoleElves().getElve();
		String elvesId = nowElve.geteLvesID()+"_"+nowElve.getGender()+"_"+nowElve.getCharacter()+"_"+nowElve.getSameIndex();
		jo.put("elvesId", elvesId);
		jo.put("randomArr", ranFloatArr);
		resParams.putUtfString("Infor", jo.toString());
		send("ResBattle", resParams, user);
		if(!player2.isOffline()){
			send("ResRivalBattle", resParams, player2.getUser());
		}else{
			//4:战斗
			JSONArray ranFloatArr2 = player2.getCurRound().getNowRoleElves().getRandomList();
			player2.setStatus(4);
			Elve nowElve2 = player2.getCurRound().getNowRoleElves().getElve();
			String elvesId2 = nowElve2.geteLvesID()+"_"+nowElve2.getGender()+"_"+nowElve2.getCharacter()+"_"+nowElve2.getSameIndex();
			jo.put("elvesId", elvesId2);
			jo.put("randomArr", ranFloatArr2);
			resParams.putUtfString("Infor", jo.toString());
			send("ResRivalBattle", resParams, user);
		}
		
		//更新回合时间
	    long nowTime = System.currentTimeMillis()/1000;
  		player1.setRoundTime(nowTime);
//  	trace("battle setRoundTime :"+nowTime);
	}
}
