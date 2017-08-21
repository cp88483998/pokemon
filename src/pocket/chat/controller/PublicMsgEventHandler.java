package pocket.chat.controller;

import java.util.ArrayList;
import java.util.List;


import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import pocket.chat.entity.Message;
import pocket.chat.service.PublicService;
import pocket.total.entity.Share;

/**
 * <p>Title: PublicMsgEventHandler<／p>
 * <p>Description: 公共消息请求处理类，为防止前端更改玩家显示信息，需从数据库中找出玩家信息，并更新“AdditionalMsg”中的玩家信息内容<／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class PublicMsgEventHandler extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		User sender = (User) event.getParameter(SFSEventParam.USER);
//		trace("sender :"+sender.getName());
//		trace(sender.getIpAddress());
		String message = (String) event.getParameter(SFSEventParam.MESSAGE);
		
		ISFSObject extraParams = (ISFSObject) event.getParameter(SFSEventParam.OBJECT);
		
		String additionalMsg = extraParams.getUtfString("AdditionalMsg");
		
//		trace("additionalMsg :"+additionalMsg);
		
//		String username = extraParams.getUtfString("username");
//		String username1 = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
//		trace("username1 :"+username1);
//		trace("LOGIN_IN_DATA :"+event.getParameter(SFSEventParam.LOGIN_IN_DATA));
		
		try {
			//区分系统消息还是公共消息
			if(additionalMsg.charAt(0)-'0' == 0){
//				trace("message:"+message);
				//获取玩家所在room的ID，以此区分所在服务器
				Room room = (Room)event.getParameter(SFSEventParam.ROOM);
				int roomId = room.getId();
				
				//从数据库中找出玩家信息，并更新“AdditionalMsg”中的玩家信息内容
				String realInfo = PublicService.findUserInfo(roomId, 
						Long.parseLong(sender.getName()), 
						extraParams.getUtfString("AdditionalMsg"));
				extraParams.putUtfString("AdditionalMsg", realInfo);
				
				Message msg = new Message();
				msg.setContext(message);
				msg.setParams(realInfo);
				
				if(Share.offline_public_msg.get(roomId) != null){
					List<Message> list = Share.offline_public_msg.get(roomId);
					if(list.size() < 10){
						list.add(msg);
					}else{
						list.remove(0);
						list.add(msg);
					}
				}else{
					List<Message> list = new ArrayList<Message>();
					list.add(msg);
					Share.offline_public_msg.put(roomId, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
