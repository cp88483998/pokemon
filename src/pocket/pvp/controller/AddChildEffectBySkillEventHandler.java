package pocket.pvp.controller;

import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.child_effect.Child_Effect_Factory;
import pocket.pvp.entity.ChildEffectParent;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.total.entity.Share;

public class AddChildEffectBySkillEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
		String roomId = params.getUtfString("roomId");
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
		Player player2 = null;
		RoleElves roleElves1 = null;
		RoleElves roleElves2 = null;
		Elve elve1 = null;
		Elve elve2 = null;
		for(int i=0;i<list.size();i++){
			if(list.get(i).isOffline()){
				player2 = list.get(i);
				continue;
			}
			if(list.get(i).getUser().getId() == user.getId()){
				player1 = list.get(i);
				roleElves1 = player1.getCurRound().getNowRoleElves();
				elve1 = roleElves1.getElve();
			}else{
				player2 = list.get(i);
				roleElves2 = player2.getCurRound().getNowRoleElves();
				elve2 = roleElves2.getElve();
			}
		}
		
		
		String jsonString = params.getUtfString("Infor");
		trace("Infor :"+jsonString);
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONObject jsonRoundCE = jsonObject.getJSONObject("roundChildEffect");
		JSONArray jsonList = jsonRoundCE.getJSONArray("skillList");
		String[] attackElves = jsonRoundCE.getString("attackElves").split("_");
		for(int i=0;i<jsonList.size();i++){
			JSONObject jo = jsonList.getJSONObject(i);
//			JSONObject jsonCEffect = jo.getJSONObject("childEffect");
//			String[] applyElvesKey = jo.getString("applyElvesKey").split("_");
			int elveId = Integer.parseInt(attackElves[0]);	
			int gender = Integer.parseInt(attackElves[1]);
			int character = Integer.parseInt(attackElves[2]);
			int sameIndex = Integer.parseInt(attackElves[3]);
			//1.判断添加条件是否满足
			boolean isSatisfy = true;
			trace("isSatisfy :"+isSatisfy);
			//2.添加子效果
			//若满足
			if(isSatisfy){
				if(elveId==elve1.geteLvesID() && gender==elve1.getGender() && character==elve1.getCharacter() && sameIndex==elve1.getSameIndex()){
					Child_Effect child_effect1 = setChild_Effect(jo);
					child_effect1.setApplyElvesKey(jo.getString("applyElvesKey"));
					List<Child_Effect> effets1 = roleElves1.getChildEffectList();
					trace("effects1 :"+effets1.toString());
					effets1.add(child_effect1);
				}
				if(elveId==elve2.geteLvesID() && gender==elve2.getGender() && character==elve2.getCharacter() && sameIndex==elve2.getSameIndex()){
					Child_Effect child_effect2 = setChild_Effect(jo);
					child_effect2.setApplyElvesKey(jo.getString("applyElvesKey"));
					List<Child_Effect> effets2 = roleElves2.getChildEffectList();
					trace("effects2 :"+effets2.toString());
					effets2.add(child_effect2);
				}
			}else{
				//若不满足，将其从childEffectInforList中移除
				jsonList.remove(i);
			}
		}
		
		//发给自己
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jsonList.toString());
		send("ResAddChildEffectBySkill", resParams, user);
		//发给对手
		send("ResAddChildEffectBySkill", resParams, player2.getUser());
	}
	
	//创建子效果
	private Child_Effect setChild_Effect(JSONObject jsonObject){
		String name = jsonObject.getString("name");
		double effectRate = jsonObject.getDouble("effectRate");
//		String applyElvesKey = jsonObject.getString("applyElvesKey");
		int childEffectParentID = jsonObject.getInt("childEffectParent");
		String targetValue = jsonObject.getString("targetValue");
		int target = jsonObject.getInt("target");
		String parentObjId = jsonObject.getString("parentObjId");
		int effectiveTimeId = jsonObject.getInt("effectiveTimeId");
		int take = jsonObject.getInt("take");
		String takeValue = jsonObject.getString("takeValue");
		boolean isOnce = jsonObject.getBoolean("isOnce");
		boolean isTriggerLineOne = jsonObject.getBoolean("isTriggerLineOne");
		String triggerFailLine = jsonObject.getString("triggerFailLine");
		int id = jsonObject.getInt("id");
		String effectId = jsonObject.getString("effectId");
		
		boolean isCanntAction = jsonObject.getBoolean("isCanntAction");
		boolean isSkillFail = jsonObject.getBoolean("isSkillFail");
		boolean isCantChangeElves = jsonObject.getBoolean("isCantChangeElves");
		boolean isCantLeave = jsonObject.getBoolean("isCantLeave");
		boolean isMustLeave = jsonObject.getBoolean("isMustLeave");
		boolean isMustHit = jsonObject.getBoolean("isMustHit");
		boolean isMustBeHit = jsonObject.getBoolean("isMustBeHit");
		boolean isCanntResetHp = jsonObject.getBoolean("isCanntResetHp");
		boolean isCannotFeatures = jsonObject.getBoolean("isCannotFeatures");
		boolean isCanntChangeLevel = jsonObject.getBoolean("isCanntChangeLevel");
		boolean isElvesAttributeFail = jsonObject.getBoolean("isElvesAttributeFail");
		boolean isKeepOneHp = jsonObject.getBoolean("isKeepOneHp");
		boolean isCanntChangeSkill = jsonObject.getBoolean("isCanntChangeSkill");
		boolean isCanntVital = jsonObject.getBoolean("isCanntVital");
		boolean isStopOneAtk = jsonObject.getBoolean("isStopOneAtk");
		boolean isCouSameSkill = jsonObject.getBoolean("isCouSameSkill");
		boolean isOnlyLastSkill = jsonObject.getBoolean("isOnlyLastSkill");
		boolean isChangeTarget = jsonObject.getBoolean("isChangeTarget");
		
		Child_Effect_Factory factory = new Child_Effect_Factory();
		Child_Effect child_effect = factory.create(effectId, target, targetValue);
		child_effect.setName(name);
		child_effect.setEffectRate(effectRate);
//		child_effect.setApplyElvesKey(applyElvesKey);
		child_effect.setParentObjId(parentObjId);
		child_effect.setEffectiveTimeId(effectiveTimeId);
		child_effect.setTake(take);
		child_effect.setTakeValue(takeValue);
		child_effect.setOnce(isOnce);
		child_effect.setId(id);
		child_effect.setTargetValue(targetValue);
		child_effect.setChildEffectParent(getEnum(childEffectParentID));
		child_effect.setTriggerLineOne(isTriggerLineOne);
		child_effect.setTriggerFailLine(triggerFailLine);
		
		child_effect.setCanntAction(isCanntAction);
		child_effect.setSkillFail(isSkillFail);
		child_effect.setCantChangeElves(isCantChangeElves);
		child_effect.setCantLeave(isCantLeave);
		child_effect.setMustLeave(isMustLeave);
		child_effect.setMustHit(isMustHit);
		child_effect.setMustBeHit(isMustBeHit);
		child_effect.setCanntResetHp(isCanntResetHp);
		child_effect.setCannotFeatures(isCannotFeatures);
		child_effect.setCanntChangeLevel(isCanntChangeLevel);
		child_effect.setElvesAttributeFail(isElvesAttributeFail);
		child_effect.setKeepOneHp(isKeepOneHp);
		child_effect.setCanntChangeSkill(isCanntChangeSkill);
		child_effect.setCanntVital(isCanntVital);
		child_effect.setStopOneAtk(isStopOneAtk);
		child_effect.setCouSameSkill(isCouSameSkill);
		child_effect.setOnlyLastSkill(isOnlyLastSkill);
		child_effect.setChangeTarget(isChangeTarget);
		
		return child_effect;
		
	}
	
	public ChildEffectParent getEnum(int i){
		switch (i) {
		case 1:
			return ChildEffectParent.Buff;
		case 2:
			return ChildEffectParent.Features;
		case 3:
			return ChildEffectParent.Effect;
		case 4:
			return ChildEffectParent.Equip;
		case 5:
			return ChildEffectParent.Weather;
		case 6:
			return ChildEffectParent.Skill;
		default:
			break;
		}
		return ChildEffectParent.None;
	}
}
