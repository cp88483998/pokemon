package pocket.timeSpace.Controller;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import pocket.total.entity.Share;
/**
 * 请求进入时空缝隙
 * <p>Title: EnterTimeSpaceEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年8月9日
 */
public class EnterTimeSpaceEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
		Share.inBossPlayer.add(user);
		
		ISFSObject resParams = new SFSObject();
		resParams.putBool("isSuc", true);
		send("ResEnterTimeSpace", resParams, user);
	}

}
