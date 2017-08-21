package pocket.timeSpace.controller;

import java.util.Iterator;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.total.entity.Share;

/**
 * 退出时空缝隙副本类
 * <p>Title: ExitTimeSpaceEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年8月9日
 */
public class ExitTimeSpaceEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		List<User> inBossPlayer = Share.inBossPlayer;
		Iterator<User> iter = inBossPlayer.iterator();
		while(iter.hasNext()){
			if(iter.next().getId() == user.getId()){
				iter.remove();
				break;
			}
		}
		send("ReqExitTimeSpace", null, user);
	}

}
