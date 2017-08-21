package pocket.pvp.Controller;

import java.util.List;
import java.util.Map;

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
 * 请求是否命中
 * ReqIsHit
 * @author 陈鹏
 *
 */
public class IsHitEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("Infor");
//		trace("Infor :"+infor);
		String roomId = params.getUtfString("roomId");
//		trace("roomId :"+roomId);
		JSONObject jsonObject = JSONObject.fromObject(infor);
		//精灵id_性别_性格_索引
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
//		trace("list0 :"+list.get(0));
//		trace("list1 :"+list.get(1));
		Player player1 = null;
		RoleElves roleElves1 = null;
	    Elve elve1 = null;
	    Player player2 = null;
	    RoleElves roleElves2 = null;
	    Elve elve2 = null;
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
						elve1 = roleElves1.getElve();
					}
					if(elveId2==Integer.parseInt(key) && gender2==value.getGender() && character2==value.getCharacter() && sameIndex2==value.getSameIndex()){
						roleElves2 = map.get(key);
						elve2 = roleElves2.getElve();
					}
				}
			    
			}else{
				player2 = list.get(i);
//				elve2 = player2.getElves2().get(String.valueOf(elveId2));
				Map<String, RoleElves> map = player2.getRoleElves();
				for(String key : map.keySet()){
					Elve value = map.get(key).getElve();
					if(elveId1==Integer.parseInt(key) && gender1==value.getGender() && character1==value.getCharacter() && sameIndex1==value.getSameIndex()){
						roleElves1 = map.get(key);
						elve1 = roleElves1.getElve();
					}
					if(elveId2==Integer.parseInt(key) && gender2==value.getGender() && character2==value.getCharacter() && sameIndex2==value.getSameIndex()){
						roleElves2 = map.get(key);
						elve2 = roleElves2.getElve();
					}
				}
				
			}
		}
		
	    SkillDataVo skill1 =  roleElves1.getStudySkills().get(String.valueOf(skillId1));
//	    trace("skill1 :"+skill1.toString());
	    
    	boolean isHit = Judge.judgeHit(elve1, elve2, skill1);
//    	trace("isHit :"+isHit);
    	JSONObject jo = new JSONObject();
    	jo.put("isHit", isHit);
    	ISFSObject resParams = new SFSObject();
    	resParams.putUtfString("Infor", jo.toString());
    	send("ResIsHit", resParams, user);
    	
//    	trace("isHit ok!");
    	
	}

}
