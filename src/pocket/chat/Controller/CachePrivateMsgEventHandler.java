package pocket.chat.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.chat.dao.MongoOffDao;
import pocket.chat.dao.MongoOffDaoImpl;
import pocket.chat.entity.OfflineMsg;

/**
 * 获取离线消息类
 * <p>Title: CachePrivateMsgEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月10日
 */
public class CachePrivateMsgEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start CachePrivateMsgEventHandler!");
//		String senderUid = params.getUtfString("senderUid");
		
		String recipientUid = user.getName();
		
		MongoOffDao dao = MongoOffDaoImpl.getInstance();
		List<OfflineMsg> offlineMsgs = dao.findOneAll("recipientUid", recipientUid);
		
		JSONObject jo = new JSONObject();
		jo.put("privateMsg", offlineMsgs);
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jo.toString());
		
		send("ResCachePrivateMsg", resParams, user);
		
		//最后删除已取出的离线消息
		try {
			dao.deleteAll("recipientUid", recipientUid);
		} catch (Exception e) {
			trace("delete offline msg fail!");
		}
	}
	
}
