package pocket.pvp.Controller;

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
 * 请求重连战斗数据处理类
 * <p>Title: BattleReconDataEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */

public class BattleReconDataEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
		String infor = params.getUtfString("Infor");
		JSONObject jo = JSONObject.fromObject(infor);
		ISFSObject resParams = new SFSObject();
		String roomId = null;
		Player player1 = null;
		Player player2 = null;
		List<Player> list = null;
		boolean isBattleRecon = false;
		for(String key : Share.matchMap.keySet()){
			list = Share.matchMap.get(key);
			for(int i=0;i<list.size();i++){
				if(list.get(i).isOffline()){
					continue;
				}
				if(list.get(i).getUser().getName().equals(user.getName())){
					roomId = key;
					isBattleRecon = true;
					break;
				}
			}
			if(isBattleRecon){
//				trace("find match team!");
				break;
			}
		}
		for(int i=0;i<list.size();i++){
			if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getName().equals(user.getName())){
				player1 = list.get(i);
			}else{
				player2 = list.get(i);
			}
		}
		
		player1.setOffline(false);
		player1.setBattleReconData(true);	
		
		boolean isRecon = jo.getBoolean("isRecon");
		if(isRecon){
			
			JSONObject joAll = FightingDataService.getFightingData(user, roomId);
			resParams.putUtfString("Infor", joAll.toString());
//			trace("BattleReconDataEventHandler :"+joAll);
			send("ResBattleReconData", resParams, user);
			
		}else{
			if(!player2.isOffline()){
				JSONObject jo1 = new JSONObject();
				jo1.put("isSuc", true);
				resParams.putUtfString("Infor", jo1.toString());
				send("ResBattleRivalRun", resParams, player2.getUser());
			}
			Share.matchMap.remove(roomId);
		}
		
	}

}
