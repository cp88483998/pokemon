package pocket.chat.Controller;

import org.bson.Document;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.chat.dao.MongoOffDao;
import pocket.chat.dao.MongoOffDaoImpl;

/**
 * 存储离线消息类
 * <p>Title: StorageOfflineMsgEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月10日
 */
public class StorageOfflineMsgEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
		String infor = params.getUtfString("Infor");
		JSONObject json = JSONObject.fromObject(infor);
		
		String senderUid = user.getName();
		String recipientUid = json.getString("recipientUid");
		String context = json.getString("context");
		String additionalMsg = json.getString("additionalMsg");
		
		
		Document document = new Document();
		document.append("senderUid", senderUid);
		document.append("recipientUid", recipientUid);
		document.append("context", context);
		document.append("additionalMsg", additionalMsg);
		document.append("createdAt", System.currentTimeMillis());
		MongoOffDao dao = MongoOffDaoImpl.getInstance();
		boolean result = dao.insertOne(document);
		
		if(!result){
			for(int i=0;i<2;i++){
				result = dao.insertOne(document);
				if(result){
					break;
				}
			}
		}
		
		JSONObject jo1 = new JSONObject();
		jo1.put("isSuc", true);
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jo1.toString());
		send("ResStorageOfflineMsg", resParams, user);
		trace("insert offline msg!");
	}
	
}
