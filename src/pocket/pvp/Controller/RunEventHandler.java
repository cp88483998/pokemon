package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.pvp.entity.Player;
import pocket.total.entity.Share;
/**
 * 请求逃跑
 * ReqRun
 * @author 陈鹏
 */
public class RunEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		if(list != null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).isOffline()){
					continue;
				}
				if(list.get(i).getUser().getId() == user.getId()){
					Player player1 = list.get(i);
					//3:逃跑
					player1.setStatus(3);
					ISFSObject resParams = new SFSObject();
					resParams.putUtfString("Infor", "Run OK");
					//发给自己
					send("ResRun", resParams, user);
				}else{
					//发给对手
					send("ResRivalRun", params, list.get(i).getUser());
				}
			}
			Share.matchMap.remove(roomId);
//			trace("matchMap2 size :"+Share.matchMap.size());
//			trace("run ok！");
		}else{
			send("ResRun", params, user);
		}
		
		
		
	}

}
