package pocket.chat.controller;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class PrivateMsgEventHandler extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
//		User sender = (User) event.getParameter(SFSEventParam.USER);
//		User recipient = (User) event.getParameter(SFSEventParam.RECIPIENT);
//		String message = (String) event.getParameter(SFSEventParam.MESSAGE);
//		trace(sender.getName()+" send private message to "+recipient.getName()+" : "+message);
//		ISFSObject extraParams = (ISFSObject) event.getParameter(SFSEventParam.OBJECT);
//		String params = extraParams.getUtfString("AdditionalMsg");
	}

}
