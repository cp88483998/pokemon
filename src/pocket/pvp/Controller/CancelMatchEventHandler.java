package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.pvp.dao.MongoPVPDao;
import pocket.pvp.dao.MongoPVPDaoImpl;
import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

/**
 * 请求取消匹配
 * ReqCanelMatch
 * @author 陈鹏
 */
public class CancelMatchEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start cancel!");
		boolean findPlayer = false;
		List<Player> playerList = null;
		for(Integer key : Share.matchPool.keySet()){
			playerList = Share.matchPool.get(key);
			for(int i=0;i<playerList.size();i++){
				if(playerList.get(i).getUser().getId() == user.getId()){
					MongoPVPDao dao = MongoPVPDaoImpl.getInstance();
					findPlayer = true;
					dao.updatePVPData(playerList.get(i).getCountry(), "cancelCount");
					playerList.remove(i);
					trace("-----cancel match-----");
				}
			}
			if(findPlayer){
				break;
			}
		}
		
		send("ResCanelMatch", null, user);
	}
}
