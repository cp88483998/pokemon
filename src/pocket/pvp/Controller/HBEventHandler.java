package pocket.pvp.Controller;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 * 心跳包
 * <p>Title: HBEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月19日
 */
public class HBEventHandler extends BaseClientRequestHandler{
	
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		send("Heart_Server", null, user);
	}
	
}
