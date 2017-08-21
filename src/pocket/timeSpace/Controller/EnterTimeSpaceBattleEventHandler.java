package pocket.timeSpace.Controller;

import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.timeSpace.entity.BossPlayer;
import pocket.total.entity.Share;
/**
 * 请求进入时空缝隙战斗
 * <p>Title: EnterTimeSpaceEventHandler<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年6月9日
 */
public class EnterTimeSpaceBattleEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		
//		String infor = params.getUtfString("Infor");
//		JSONObject jo = JSONObject.fromObject(infor);
//		trace("level :"+jo.getInt("level"));
		String battleId = Share.battleId+"";
		
		BossPlayer bossPlayer = new BossPlayer();
		bossPlayer.setStartTime(System.currentTimeMillis());
		bossPlayer.setUser(user);
		
		Map<String, BossPlayer> inBossPlayer2 = Share.inBossPlayer2;
		inBossPlayer2.put(battleId, bossPlayer);
		
		JSONObject resJo = new JSONObject();
		resJo.put("battleId", battleId);
		ISFSObject resParams = new SFSObject();
		resParams.putUtfString("Infor", resJo.toString());
		send("ResEnterTimeSpaceBattle", resParams, user);

		Share.battleId++;
	}

}
