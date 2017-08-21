package pocket.total.controller;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import pocket.total.entity.Share;
import pocket.total.util.Verify;

/**
 * 登录请求处理类
 * <p>Title: LoginEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月19日
 */
public class LoginEventHandler extends BaseServerEventHandler{
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		
		String name = (String) event.getParameter(SFSEventParam.LOGIN_NAME); 
//		trace("name :"+name);
		ISFSObject params = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
//		//取得 Session
//		ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);
		//取得登录请求响应数据
//		ISFSObject resParams = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
		
//		trace("params :"+params);
		String url = params.getUtfString("url");
		String token = params.getUtfString("token");
		String uid = params.getUtfString("uid");
		Share.serverURL.put(uid, url);
//		trace("-----uid :"+uid);
//		trace("-----url :"+url);
//		trace("token :"+token);
		Verify verify = new Verify();
		boolean result = verify.sendGet(url, token, uid);
		if (!result){
//			resParams.putBool("success", false );
		     // 创建要发送给客户端的错误代码
		     SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
		     errData.addParameter(name);
		     
		     // 抛出登录异常
		     throw new SFSLoginException("vertify failed", errData);
		}
	}
}
