package pocket.pvp.controller;

import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.service.JsonService;
import pocket.pvp.service.JsonServiceImpl;
import pocket.total.entity.Share;
/**
 * 请求更换精灵阵容
 * ReqChangeElvesSquad
 * @author 陈鹏
 */
public class ChangeElvesSquadEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("userId :"+user.getId());
		String jsonString = params.getUtfString("Infor");
		String roomId = params.getUtfString("roomId");
//		trace("change_str:"+jsonString.length());
		JSONObject jSONObject = JSONObject.fromObject(jsonString);
		JSONArray jaElves = jSONObject.getJSONArray("elves");
		JsonService jsonService = new JsonServiceImpl();
		Map<String, RoleElves> roleElves = jsonService.parseElve(jaElves);
//		trace("change elves Squad:"+jaElves.toString());
		Player player1 = null;
		Player player2 = null;
		List<Player> list = Share.matchMap.get(roomId);
		for(int i=0;i<list.size();i++){
			if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
				player1.setRoleElves(roleElves);
				ISFSObject resParams = new SFSObject();
				resParams.putUtfString("Infor", "Change OK");
				//发给自己
				send("ResChangeElvesSquad", resParams, user);
			}else{
				player2 = list.get(i);
				//发给对手
				send("ResRivalChangeElvesSquad", params, player2.getUser());
			}
		}
		
	}

}
