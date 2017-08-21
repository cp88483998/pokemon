package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

/**
 * 请求动画播放完毕类，目的是同步双方进度
 * <p>Title: ElvesShowOverEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月19日
 */
public class ElvesShowOverEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    for(int i=0;i<list.size();i++){
	    	if(list.get(i).isOffline()){
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
			}
		}
//	    player1.setPlayOver(true);
	    
	    ISFSObject resParams = new SFSObject();
	    send("ResElvesShowOver", resParams, user);
	    
	    if(player1.getBattleRoundResult()!=null){
	    	JSONArray joAll = player1.getBattleRoundResult();
			JSONObject joObject = new JSONObject();
			joObject.put("battleRoundResult", joAll);
			resParams.putUtfString("Infor", joObject.toString());
			
			send("ResBattleRoundResult", resParams, user);
	    }
	    
	}

}
