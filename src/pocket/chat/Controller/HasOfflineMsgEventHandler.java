package pocket.chat.Controller;

import java.util.Set;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.chat.dao.MongoOffDao;
import pocket.chat.dao.MongoOffDaoImpl;

public class HasOfflineMsgEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("Infor");
		JSONObject json = JSONObject.fromObject(infor);
		String recipientUid = json.getString("recipientUid");
		
		MongoOffDao dao = MongoOffDaoImpl.getInstance();
		Set<String> senderUids = dao.findMany("recipientUid", recipientUid);
		
		boolean hasOfflineMsg = false;
		
		if(senderUids.size() > 0){
			hasOfflineMsg = true;
		}
		
		JSONObject jo = new JSONObject();
		jo.put("hasOfflineMsg", hasOfflineMsg);
		jo.put("recipientUid", recipientUid);
		jo.put("senderUids", senderUids);

		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jo.toString());
		send("ResHasOfflineMsg", resParams, user);
		
	}

}
