package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

public class BattleReadyEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
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
		player1.setBattleReady(true);
		if(!player2.isOffline()){
			if(player2.isBattleReady()){
				send("ResBattleReady", null, user);
				send("ResBattleReady", null, player2.getUser());
			}
		}
		if(player2.isOffline()){
			player2.setBattleReady(true);
			send("ResBattleReady", null, user);
		}
	}

}
