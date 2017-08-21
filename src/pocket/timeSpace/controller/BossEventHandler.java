package pocket.timeSpace.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import pocket.timeSpace.entity.Boss;
import pocket.total.entity.Share;

/**
 * 获取boss信息类
 * <p>Title: BossEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年6月6日
 */
public class BossEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		Map<String, Boss> bossMap = Share.bossMap;
		ISFSObject resParams = new SFSObject();
		List<Boss> list = new ArrayList<>();
		for(String key:bossMap.keySet()){
			list.add(bossMap.get(key));
		}
		
		JSONArray ja = JSONArray.fromObject(list);
		resParams.putUtfString("Infor", ja.toString());
		
		send("ResBossInfor", resParams, user);
	}
	
}
