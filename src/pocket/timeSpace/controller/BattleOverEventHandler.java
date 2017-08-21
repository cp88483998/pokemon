package pocket.timeSpace.controller;

import java.util.Iterator;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.timeSpace.entity.BossPlayer;
import pocket.total.entity.Share;
import pocket.total.util.BossUtil;
/**
 * 时空缝隙战斗结束请求处理类
 * <p>Title: BattleOverEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年6月9日
 */
public class BattleOverEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("Infor");
		boolean result = true;
		int level = 1;
		boolean isBossDie = false;
		JSONObject verifyJo = null;
		
		JSONObject jo = JSONObject.fromObject(infor);
		String battleId = jo.getString("battleId");
		Map<String, BossPlayer> inBossPlayer2 = Share.inBossPlayer2;
		BossPlayer bossPlayer = inBossPlayer2.get(battleId);
		
		//判断有没有这个battleId玩家
		if(bossPlayer == null){
			trace("bossPlayer is null");
			return;
		}else{
			//判断时间有没有超过10分钟
			long nowTime = System.currentTimeMillis();
			if(nowTime - bossPlayer.getStartTime() > 600000){
				result = false;
			}
		}
		
		ISFSObject resParams = new SFSObject();
		JSONObject resJo = new JSONObject();
		
		//调用游戏服务器接口，1.校验数据， 2.判断boss是否死亡
		if(result){
			jo.put("uid", user.getName());
//			trace(jo.toString());
			String url = Share.serverURL.get(user.getName());
			String returnResult = BossUtil.verifyBossData(jo.toString(), url);
//			trace("returnResult :"+returnResult);
			if(returnResult != null){
				verifyJo = JSONObject.fromObject(returnResult);
				boolean verifyResult = verifyJo.getBoolean("result");
				if(!verifyResult){
					result = false;
				}else{
					isBossDie = verifyJo.getBoolean("isBossDead");
					resJo.put("hitValue", verifyJo.getInt("hitValue"));
				}
			}
		}
		
		resJo.put("result", result);
		
		resParams.putUtfString("Infor", resJo.toString());
		//战斗结束ResBattleOver
		send("ResBattleOver", resParams, user);
		
		if(isBossDie){
//			trace("-----boss die-----");
			level = verifyJo.getInt("level");
			JSONObject endJo = new JSONObject();
			endJo.put("level", level);
			endJo.put("uid", user.getName());
			endJo.put("isDie", isBossDie);
			
			ISFSObject endParams = new SFSObject();
			endParams.putUtfString("Infor", endJo.toString());
			
			Iterator<User> iter = Share.inBossPlayer.iterator();
			while(iter.hasNext()){
				//挑战的boss已经死亡ResBattleEnd
				try {
					send("ResBattleEnd", endParams, iter.next());
				} catch (Exception e) {
					trace("inBossPlayer send fail!");
					iter.remove();
				}
			}
			
			//初始化
			Share.inBossPlayer2.clear();
			Share.battleId = 0;
		}else{
			//若boss没死亡，就将玩家从战斗集合中移除
			Share.inBossPlayer2.remove(battleId);
		}
	}

}
