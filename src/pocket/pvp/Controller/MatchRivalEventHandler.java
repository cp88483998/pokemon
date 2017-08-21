package pocket.pvp.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.dao.MongoPVPDao;
import pocket.pvp.dao.MongoPVPDaoImpl;
import pocket.pvp.entity.Player;
import pocket.pvp.service.FightingDataService;
import pocket.pvp.service.JsonService;
import pocket.pvp.service.JsonServiceImpl;
import pocket.pvp.service.PVPMatch;
import pocket.total.entity.Share;
import pocket.total.util.IpInfoUtil;
/**
 * 匹配请求处理类
 * <p>Title: MatchRivalEventHandler<／p>
 * <p>Description: 需要防止玩家发送多次匹配请求<／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */

public class MatchRivalEventHandler extends BaseClientRequestHandler {
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
//		trace("start match event handler :"+user.getId());
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if((hour>=12 && hour<=13) || (hour>=19 && hour<=20)){
			String infor = params.getUtfString("Infor");
			JSONObject jsonObject = JSONObject.fromObject(infor);
			
			ISFSObject resParams = new SFSObject();
			
	//		int winRank = player.getWinRank();
			int winRank = jsonObject.getJSONObject("player").getInt("dan");
			boolean isInList = false;//是否已经处于匹配池子中
			List<Player> playerList = null;
			if(Share.matchPool.get(winRank) == null){
				//若该段位为空
				playerList = new ArrayList<Player>();
				Share.matchPool.put(winRank, playerList);
				
			}
			//判断玩家是否处于池子中
			playerList = Share.matchPool.get(winRank);
			for(int i=0;i<playerList.size();i++){
				if(user.getName().equals(playerList.get(i).getUser().getName())){
					isInList = true;
					trace(user.getName()+" isInList");
					break;
				}
			}
			//判断玩家是否处于匹配表中
	//		List<Player> team = null;
	//		for(String key : Share.matchMap.keySet()){
	//			team = Share.matchMap.get(key);
	//			for(int i=0;i<team.size();i++){
	//				if(team.get(i).isOffline()){
	//					continue;
	//				}
	//				if(user.getName().equals(team.get(i).getUser().getName())){
	//					isInList = true;
	//					trace("isInList2 :"+isInList);
	//					break;
	//				}
	//			}
	//		}
//			trace(user.getName()+" isInList :"+isInList);
			
			if(!isInList){
				MongoPVPDao dao = MongoPVPDaoImpl.getInstance();
				JsonService jsonService = JsonServiceImpl.getInstance();
				Player player = jsonService.parsePlayer(jsonObject);
	//			trace(player.toString());
				player.setStatus(1);
				player.setUser(user);
				long startTime = System.currentTimeMillis()/1000;
//				trace("-----startTime:"+startTime+"-----");
				//设置开始时间（秒）
				player.setStartTime(startTime);
				send("ResMatchRival", null, user);
				
				//设置IP信息
				String country = IpInfoUtil.getIpInfo(user.getIpAddress());
				player.setCountry(country);
				
				dao.updatePVPData(country, "reqPVPCount");
				
//				Player playerMatch = null;
//				if(playerList.size()>0){
//					player.setRankMatch(true);
//					//同段位同等级匹配
//					playerMatch = PVPMatch.sameRankMatch(player);
//					if(playerMatch != null){
//						
//						playerMatch.setRankMatch(true);
//						int roomId = Share.roomId;
//						
//						List<Player> list = new ArrayList<Player>();
//						list.add(player);
//						list.add(playerMatch);
//						Share.matchMap.put(String.valueOf(roomId), list);
//						
//						//自己信息发给对手
//						jsonObject.put("roomId", roomId);
//						resParams.putUtfString("Infor", jsonObject.toString());
//						send("ResGetMatchRival", resParams, playerMatch.getUser());
//						
//						//对手信息发给自己
//						JSONObject jo1 = FightingDataService.playerToJo(playerMatch, roomId);
//						resParams.putUtfString("Infor", jo1.toString());
//						send("ResGetMatchRival", resParams, user);
//						
//						//将对手从匹配池中删除
//						Iterator<Player> iter = playerList.iterator();
//						while(iter.hasNext()){
//							Player p = iter.next();
//							if(playerMatch.getUid() == p.getUid()){
//								iter.remove();
//								break;
//							}
//						}
//						
//						// 更新PVP数据
//						dao.insertOneRecord(player, playerMatch, false);
//						dao.insertOneRecord(playerMatch, player, false);
//						
//						Share.roomId++;
//					}
//				}
//				if(playerMatch == null){
//					player.setRankMatch(false);
					Share.matchPool.get(winRank).add(player);
					trace("-----Add to " + winRank + " matchPool :"+ Share.matchPool.get(winRank).size()+"-----");
//				}
			}
		}else{
			trace("-----hour:"+hour+"-----");
			send("ResMatchRival", null, user);
		}
	}
}
