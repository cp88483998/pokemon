package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

public class CanelChangeElvesEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("CanelChangeElvesEventHandler start!");
		String roomId = params.getUtfString("roomId");
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
		player1.setFreeChangeElve(false);
		if(!player2.isOffline()){
			send("ResRivalCanelChangeElves", params, player2.getUser());
		}
		send("ResCanelChangeElves", params, user);
	}
	
}
