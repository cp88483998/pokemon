package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.service.Judge;
import pocket.total.entity.Share;

/**
 * 请求获取技能的先后手
 * ReqBattleRoundOrder
 * @author 陈鹏
 *
 */
public class BattleRoundOrderEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    SkillDataVo skill1 = null;
	    Player player2 = null;
	    SkillDataVo skill2 = null;
	    RoleElves roleElves1 = null;
	    RoleElves roleElves2 = null;
		for(int i=0;i<list.size();i++){
			if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
				 //找出玩家1精灵
				roleElves1 = player1.getCurRound().getNowRoleElves();
			    skill1 = player1.getCurRound().getNowSkill();
			    
			}else{
				player2 = list.get(i);
				roleElves2 = player2.getCurRound().getNowRoleElves();
			    skill2 = player2.getCurRound().getNowSkill();
			}
		}
		//判断先手
		Elve elveFirst = Judge.judgeFirst(roleElves1, roleElves2, skill1, skill2);
		String elvesId = elveFirst.geteLvesID()+"_"+elveFirst.getGender()+"_"+elveFirst.getCharacter()+"_"+elveFirst.getSameIndex();
		JSONObject jo = new JSONObject();
		jo.put("elvesId", elvesId);
		String joStr = jo.toString();
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", joStr);
		send("ResBattleRoundOrder", resParams, user);
//		trace("BattleRoundOrderEventHandler ok!");
		
	}
	
}
