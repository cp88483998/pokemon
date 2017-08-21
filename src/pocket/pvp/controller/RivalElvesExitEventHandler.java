package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;

/**
 * 请求对手必须下场
 * <p>Title: RivalElvesExitEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class RivalElvesExitEventHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
//		trace("start RivalElvesExitEventHandler!");
		String roomId = params.getUtfString("roomId");
		JSONObject rivalElvesJO = JSONObject.fromObject(params.getUtfString("Infor")).getJSONObject("rivalElvesInfor");
		
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    Player player2 = null;
	    for(int i=0;i<list.size();i++){
	    	if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
			}else{
				player2 = list.get(i);
			}
		}
	    
	    //这个字段用来防止player2提前发送mustLeave更换精灵请求
	    //情况1：player1还没播放完旋风技能动画，而player2已经播放完，并且更换了精灵
	    //情况1：player1播放完旋风技能动画，而player2还没更换精灵
	    //只有player1.isMustLeaveOver==true，player2才能发送更换精灵给对手
	    player1.setMustLeaveOver(true);
	    
	    //若使用的是旋风技能，就自动更换精灵
	    ISFSObject resParams = new SFSObject();
	    JSONArray ranFloatArr = player2.getCurRound().getNowRoleElves().getRandomList();
	    
	    //若对手断线或对手重连第一回合或处于BattleReconData（断线玩家重连后，发送了ReqBattleReconData请求后，才能返回ResReconBattleRoundResult）
		if(player2.isOffline() || player2.isConBack() || player2.isBattleReconData()){
			SkillService.autoChooseRoleElves(player2);
			ChangeElvesEventHandler_hasAuto.updateOldRoleElves(rivalElvesJO, player2);
			
			//发给对手
			JSONObject jo2 = new JSONObject();
//			trace("rival change roleElve :"+player2.getCurRound().getNowRoleElves().getElve().toString());
			jo2.put("elves", player2.getCurRound().getNowRoleElves().getElve());
			jo2.put("isUseSkill", true);
			jo2.put("isFreeChangeElve", false);
			jo2.put("randomArr", ranFloatArr);
			resParams.putUtfString("Infor", jo2.toString());
			send("ResRivalChangeElves", resParams, user);
			
			player1.setMustLeaveOver(false);
		}
		else if(!player2.isOffline() && player2.getCurRound().getBeChangeRoleElves()!=null){
			//发给对手
			RoleElves roleElve = player2.getCurRound().getNowRoleElves();
			JSONObject jo2 = new JSONObject();
//			trace("rival change roleElve :"+roleElve.getElve().toString());
			jo2.put("elves", roleElve.getElve());
			jo2.put("isUseSkill", true);
			jo2.put("isFreeChangeElve", false);
			jo2.put("randomArr", ranFloatArr);
			resParams.putUtfString("Infor", jo2.toString());
			send("ResRivalChangeElves", resParams, user);
			
			player1.setMustLeaveOver(false);
		}
	}

}
