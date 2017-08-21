package pocket.chat.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import pocket.chat.entity.Message;
import pocket.total.entity.Share;

public class CachePublicMsgEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start CachePublicMsgEventHandler!");
		int roomId = user.getLastJoinedRoom().getId();
		ISFSObject resParams = new SFSObject();
		if(Share.offline_public_msg.get(roomId) != null){
			List<Message> list = Share.offline_public_msg.get(roomId);
			JSONArray jsonArray = JSONArray.fromObject(list);
			resParams.putUtfString("Infor", jsonArray.toString());
			send("WorldHistory_Server", resParams, user);
		}else{
			JSONArray jsonArray = new JSONArray();
			resParams.putUtfString("Infor", jsonArray.toString());
			send("WorldHistory_Server", resParams, user);
		}
	}
	
}
