package pocket.pvp.Controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.service.FightingDataService;
import pocket.pvp.service.RoundResultService;
import pocket.pvp.service.SkillService;
import pocket.total.entity.Share;
import pocket.total.util.JsonRead;
import pocket.total.util.StaticClass;

/**
 * 请求使用技能
 * <p>Title: UseSkillEventHandler_hasAuto<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
 
public class UseSkillEventHandler_hasAuto extends BaseClientRequestHandler{
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("UseSkillEventHandler start!");
		String jsonString = params.getUtfString("Infor");
		
		String roomId = params.getUtfString("roomId");
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		int skillId = jsonObject.getInt("skillId");
		
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
		
		CurRound curRound1 = player1.getCurRound();
		RoleElves attackRoleElves = curRound1.getNowRoleElves();
		int resultType1 = curRound1.getResultType();
		ISFSObject resParams = new SFSObject();
		
		//若resultType != 10，说明player1已经用过技能，不能再选技能。
		if(resultType1 == 10){
			
			curRound1.setResultType(1);
			SkillDataVo skill1 = null;
			if(skillId == 10165){//10165：挣扎
				skill1 = JsonRead.skillFind(10165, StaticClass.skill_data_js);
			}else{
				skill1 = attackRoleElves.getStudySkills().get(String.valueOf(skillId));
			}
			/*
			 * 添加子效果
			 */
//			JSONObject jsonRoundCE = jsonObject.getJSONObject("roundChildEffect");
//			curRound1.setRoundChildEffect(jsonRoundCE);
			
	//		Com_Child_Effect.addChildEffect(jsonRoundCE, attackRoleElves, beAttackRoleElves, skill1, skill2);
			
			// 判断技能类型
			JSONObject jo = SkillService.getBaseSkill(player1, player2, skill1);
			resParams.putUtfString("Infor", jo.toString());
			//返回给自己
			send("ResUseSkill", resParams, user);
			
			// 计算当前回合数据
			JSONArray joAll = null;
			if(player2.getCurRound().getResultType() != 10){
				joAll = RoundResultService.SpeciesJudgeSkill(player1, player2);
			}
			
			// 对手在线状态
//			trace(player2.getUser().getName()+" isOffline :"+player2.isOffline());
			
			if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
				//将数据发给对手
				send("ResRivalUseSkill", resParams, player2.getUser());
				
				if(joAll == null){
//					trace("Rival not yet use skill or change roleElves!");
				}
				
			}else if(player2.isOffline() || player2.isConBack() || player2.isBattleReconData()){
				// 对手不在线
//				trace("player2.isOffline "+player2.isOffline());
//				trace("player2.isConBack "+player2.isConBack());
//				trace("player2.isBattleReconData  "+player2.isBattleReconData());
				
				if(joAll == null){
					//对手自动选技能，不需要判断更换精灵，因为在roundOverResult那里已经更换了
					jo = SkillService.autoChooseSkill(player2, player1);
					resParams.putUtfString("Infor", jo.toString());
					
					//将数据发给在线的玩家
					send("ResRivalUseSkill", resParams, user);
					
					//计算战斗数据
					joAll = RoundResultService.SpeciesJudgeSkill(player1, player2);
				}
				
			}
			if(joAll != null){
				player1.setBattleRoundResult(joAll);
				player2.setBattleRoundResult(joAll);
				
				player1.setPlayOver(false);
				player2.setPlayOver(false);
				
				//走到这里，理论上joAll不可能出现为null的情况
				JSONObject joObject = new JSONObject();
//				joObject.put("roundChildEffect", jsonRoundCE);
				joObject.put("battleRoundResult", joAll);
				resParams.putUtfString("Infor", joObject.toString());
				send("ResBattleRoundResult", resParams, user);
				
				if(!player2.isOffline() && !player2.isConBack() && !player2.isBattleReconData()){
					send("ResBattleRoundResult", resParams, player2.getUser());
				}
				
				//对手断线重连
				if(!player2.isOffline() && player2.isConBack() && !player2.isBattleReconData()){
//					trace("player2 is connect back!");
					jo = FightingDataService.conBattleRoundResult(player2, player1);
					
					resParams.putUtfString("Infor", jo.toString());
					send("ResReconBattleRoundResult", resParams, player2.getUser());
					
					player2.setConBack(false);
				}
				player1.getCurRound().setBeChangeRoleElves(null);
				player2.getCurRound().setBeChangeRoleElves(null);
			}	
		}	
	}
}
