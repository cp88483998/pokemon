package pocket.pvp.Controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.service.HurtCom;
import pocket.total.entity.Share;
import pocket.total.util.StaticClass;
/**
 * 请求获取伤害值
 * ReqGetAttackHitValue
 * @author 陈鹏
 */
public class GetAttackHitValueEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("Infor");
		String roomId = params.getUtfString("roomId");
		JSONObject jsonObject = JSONObject.fromObject(infor);
		String[] attackKey = jsonObject.getString("attackKey").split("_");
		String[] beAttackKey = jsonObject.getString("beAttackKey").split("_");
		int skillId1 = Integer.parseInt(jsonObject.getString("skillId"));
		
		int elveId1 = Integer.parseInt(attackKey[0]);
		int gender1 = Integer.parseInt(attackKey[1]);
		int character1 = Integer.parseInt(attackKey[2]);
		int sameIndex1 = Integer.parseInt(attackKey[3]);
		
		int elveId2 = Integer.parseInt(beAttackKey[0]);
		int gender2 = Integer.parseInt(beAttackKey[1]);
		int character2 = Integer.parseInt(beAttackKey[2]);
		int sameIndex2 = Integer.parseInt(beAttackKey[3]);
		
		
		List<Player> list = Share.matchMap.get(roomId);
		Player player1 = null;
	    Elve elve1 = null;
	    Player player2 = null;
	    Elve elve2 = null;
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
//				elve1 = player1.getElves2().get(String.valueOf(elveId1));
				Map<String, RoleElves> map = player1.getRoleElves();
				for(String key : map.keySet()){
					Elve value = map.get(key).getElve();
					if(elveId1==Integer.parseInt(key) && gender1==value.getGender() && character1==value.getCharacter() && sameIndex1==value.getSameIndex()){
						roleElves1 = map.get(key);
						elve1 = map.get(key).getElve();
					}
					if(elveId2==Integer.parseInt(key) && gender2==value.getGender() && character2==value.getCharacter() && sameIndex2==value.getSameIndex()){
						roleElves2 = map.get(key);
						elve2 = map.get(key).getElve();
					}
				}
			    
			}else{
				player2 = list.get(i);
//				elve2 = player2.getElves2().get(String.valueOf(elveId2));
				Map<String, RoleElves> map = player2.getRoleElves();
				for(String key : map.keySet()){
					Elve value = map.get(key).getElve();
					if(elveId2==Integer.parseInt(key) && gender2==value.getGender() && character2==value.getCharacter() && sameIndex2==value.getSameIndex()){
						roleElves2 = map.get(key);
						elve2 = map.get(key).getElve();
					}
					if(elveId1==Integer.parseInt(key) && gender1==value.getGender() && character1==value.getCharacter() && sameIndex1==value.getSameIndex()){
						roleElves1 = map.get(key);
						elve1 = map.get(key).getElve();
					}
				}
				
			}
		}
		
//		Find find = new Find();
//		//根据user,找出玩家
//	    Player player1 = find.findPlayer(user);
//	    Player player2 = find.findMatch(user);
//	    Elve elve1 = player1.getElves2().get(String.valueOf(elveId1));
//	    Elve elve2 = player2.getElves2().get(String.valueOf(elveId2));
//		trace("elve1 :"+elve1.toString());
//	    
	    SkillDataVo skill1 =  roleElves1.getStudySkills().get(String.valueOf(skillId1));
		
		HurtCom hurtCom = new HurtCom();
		int hurtValue = hurtCom.hurt(roleElves1, roleElves2, skill1);
		
		String skill_ability1 = skill1.getSkill_ability();
		//相克倍数(系数)
		double pgm = hurtCom.pgm(skill_ability1, elve2);
//		trace("pgm :"+pgm);
		
		int c = 0;
		for(int i=0;i<StaticClass.skill_data_js.size();i++){
			JSONObject jsonObject2 = StaticClass.skill_data_js.getJSONObject(i);
			if(jsonObject2.getInt("Skill_number") == skillId1){
				//要害值
				c = jsonObject2.getInt("vital");
			}
		}
		if(c > 4){
			c = 4;
		}
		Random random = new Random();
		double ran = random.nextDouble();
		//是否是要害攻击
		boolean isVital = false;
		
		switch (c) {
		case 0:
			isVital = ran < 0.0625;
			break;
		case 1:
			isVital = ran < 0.125;
			break;
		case 2:
			isVital = ran < 0.25;
			break;
		case 3:
			isVital = ran < 0.333;
			break;
		case 4:
			isVital = ran < 0.5;
			break;
		default:
			break;
		}
		
		//对手是否死亡（被攻击方）
		boolean isRivalDie = false;
		int newHp = elve2.getHp() - hurtValue;
		if(newHp >= 0){
			elve2.setHp(newHp);
		}else{
			elve2.setHp(0);
			elve2.setDie(true);
//			trace(elve2.geteLvesID()+" isDie :"+elve2.isDie());
			isRivalDie = true;
		}
		
		//我方是否死亡（攻击方）
		boolean isOwnDie = elve1.getIsDie();
//		trace("isOwnDie :"+isOwnDie);
		//伤害值
		if(isVital){
			hurtValue *= 2;
		}
		
		JSONObject jo = new JSONObject();
		jo.put("hitValue", hurtValue);
		jo.put("xkMultiple", pgm);
		jo.put("isVital", isVital);
		jo.put("isRivalDie", isRivalDie);
		jo.put("isOwnDie", isOwnDie);
		
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", jo.toString());
		send("ResGetAttackHitValue", resParams, user);
		
//		trace("GetAttackHitValueEventHandler ok!");
	}

}
