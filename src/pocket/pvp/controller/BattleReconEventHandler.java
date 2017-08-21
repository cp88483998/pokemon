package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

/**
 * 请求断线重连
 * <p>Title: BattleReconEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class BattleReconEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		/*
		 * 找出player，通知客户端重连
		 */
		boolean isBattleRecon = false;
		List<Player> list = null;
		for(String key : Share.matchMap.keySet()){
			list = Share.matchMap.get(key);
			for(int i=0;i<list.size();i++){
				if(list.get(i).isOffline()){
					continue;
				}
				if(list.get(i).getUser().getName().equals(user.getName())){
					isBattleRecon = true;
					break;
				}
			}
			if(isBattleRecon){
				break;
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("isBattleRecon", isBattleRecon);
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jo.toString());
		//服务端通知客户端重连
		send("ResBattleRecon", resParams, user);
	}

}
