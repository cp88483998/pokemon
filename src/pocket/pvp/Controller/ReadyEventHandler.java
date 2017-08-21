package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.total.entity.Share;
/**
 * 请求准备
 * ReqReady
 * @author 陈鹏
 */
public class ReadyEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("userId :"+user.getId());
		String roomId = params.getUtfString("roomId");
//		trace("roomId :"+roomId);
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
		
		//2:准备状态
		player1.setStatus(2);
		RoleElves nowRoleElve = null;
		for(String elveId : player1.getRoleElves().keySet()){
			nowRoleElve = player1.getRoleElves().get(elveId);
			break;
		}
		CurRound curRound = new CurRound();
		curRound.setNowRoleElves(nowRoleElve);
		player1.setCurRound(curRound);
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", "Ready OK");
		//发给自己
		send("ResReady", resParams, user);
		if(!player2.isOffline()){
			//发给对手
			send("ResRivalReady", params, player2.getUser());
		}
		else{
			//2:准备状态
			player2.setStatus(2);
			RoleElves nowRoleElve2 = null;
			for(String elveId : player2.getRoleElves().keySet()){
				nowRoleElve2 = player2.getRoleElves().get(elveId);
				break;
			}
			CurRound curRound2 = new CurRound();
			curRound2.setNowRoleElves(nowRoleElve2);
			player2.setCurRound(curRound2);
			resParams.putUtfString("Infor", "Ready OK");
			//发给
			send("ResRivalReady", params, user);
		}
		
//		trace("ready ok!");
	}

}
