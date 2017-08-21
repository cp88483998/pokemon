package pocket.pvp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pocket.pvp.entity.Player;
import pocket.total.entity.Share;

/**
 * 匹配类
 * <p>Title: PVPMatch<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class PVPMatchOld{
	
	/**
	 * 同段位匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player sameRankMatch(Player player){
		
//		if(Share.playerList.size() >= 1){
			for(int i=0;i<Share.playerList.size();i++){
				if(player.getUid() == Share.playerList.get(i).getUid()){
					//先将player从Share.playerList中移除，以防被别的玩家匹配到
					Share.playerList.remove(i);
					break;
				}
			}
//		}
		
		Player playerRankMatch = null;
		if(Share.playerList.size() != 0 && !Share.playerList.isEmpty()){
			//System.out.println("同段位匹配！");
			//same_ranks是存放同段位player的集合
			List<Player> same_ranks = findPlayers(0, player);
			playerRankMatch = matchRule1(player, same_ranks);
			if(playerRankMatch != null){
//				System.out.println(Share.get(player)+" 同段位匹配到了："+Share.get(playerRankMatch));
				for(int i=0;i<Share.playerList.size();i++){
					if(playerRankMatch.getUid() == Share.playerList.get(i).getUid()){
						Share.playerList.remove(i);
						break;
					}
				}
			}
		}else{
			Share.playerList.add(player);
		}
		return playerRankMatch;
	}
	
	/**
	 * 上下一个段位匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player oneRankMatch(Player player){
		//若同段位匹配不到，则进行非同段位匹配
		//System.out.println("上下一个段位匹配！");
		List<Player> all_players1 = findPlayers(1, player);
		System.out.println("all_players1.size "+all_players1.size());
		Player playerOtherMatch = null;
		if(all_players1.size() > 0){
			playerOtherMatch = matchRule2(player, all_players1);
		}
		if(playerOtherMatch != null){
			for(int i=0;i<Share.playerList.size();i++){
				if(playerOtherMatch.getUid() == Share.playerList.get(i).getUid()){
					Share.playerList.remove(i);
					break;
				}
			}
		}else{
			//上下一个段位匹配不到，就匹配上下两个段位
			playerOtherMatch = twoRankMatch(player);
		}
		return playerOtherMatch;
	}
	/**
	 * 上下二个段位匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	private static Player twoRankMatch(Player player){
		//System.out.println("上下二个段位匹配！");
		Player playerOtherMatch = null;
		List<Player> all_players2 = findPlayers(2, player);
		if(all_players2.size() > 0){
			playerOtherMatch = matchRule2(player, all_players2);
		}
		return playerOtherMatch;
	}
	/**
	 * 找出符合段位要求的玩家
	 * @param rankDiffer
	 * @param player
	 * @return 玩家集合
	 */
	private static List<Player> findPlayers(int rankDiffer, Player player){
		List<Player> all_players2 = new ArrayList<Player>();
		for(int j=0;j<Share.playerList.size();j++){
			Player player1 = Share.playerList.get(j);
			//再先确定双方的状态
//			if(player.getStatus()!=1){
//				break;
//			}
//			if(player1.getStatus()!=1){
//				continue;
//			}
//			if(player1.getStatus()==1){
				if(Math.abs(player.getWinRank() - player1.getWinRank()) == rankDiffer){
					all_players2.add(player1);
				}
//			}
		}
		return all_players2;
	}
	/**
	 * 同段位使用到的匹配方法
	 * @param player
	 * @param players
	 * @return 匹配到的玩家
	 */
	private static Player matchRule1(Player player, List<Player> players){
		//胜点
		int wins_points = player.getWinPoint();
		
		Map<Player, Integer> wins_points_map = new HashMap<Player, Integer>();
		for(int i=0;i<players.size();i++){
			//筛选胜点相差小于0.05的
			int differ_wins_points = wins_points - players.get(i).getWinPoint();
			if(Math.abs((double)differ_wins_points/wins_points) <= 0.05){
				wins_points_map.put(players.get(i), differ_wins_points);
			}
		}
		if(wins_points_map.size() >= 1){
			return MaxByValue(wins_points_map);
		}
		return null;
	}
	/**
	 * 非同段位使用到的匹配方法
	 * @param player
	 * @param players
	 * @return 匹配到的玩家
	 */
	private static Player matchRule2(Player player, List<Player> players){
		//胜点
		int wins_points = player.getWinPoint();
		//等级
		int level = player.getLevel();
		
		Map<Player, Integer> ranks_map = new HashMap<Player, Integer>();
		for(int i=0;i<players.size();i++){
			//筛选等级相差小于8的
			int differ_rank = level - players.get(i).getLevel();
			if(Math.abs(differ_rank) <= 8){
				ranks_map.put(players.get(i), differ_rank);
			}
		}
		if(ranks_map.size() >= 1){
			return MaxByValue(ranks_map);
		}
		Map<Player, Integer> wins_points_map = new HashMap<Player, Integer>();
		for(int i=0;i<players.size();i++){
			//筛选胜点相差小于0.05的
			int differ_wins_points = wins_points - players.get(i).getWinPoint();
			if(Math.abs((double)differ_wins_points/wins_points) <= 0.05){
				wins_points_map.put(players.get(i), differ_wins_points);
			}
		}
		if(wins_points_map.size() >= 1){
			return MaxByValue(wins_points_map);
		}
		return null;
	}
	
	/**
	 * 求Map中最大value值对应的key
	 * @param map
	 * @return 匹配到的玩家
	 */
	private static Player MaxByValue(Map<Player, Integer> map) {
		if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<Player, Integer> sortedMap = new LinkedHashMap<Player, Integer>();  
        List<Map.Entry<Player, Integer>> entryList = new ArrayList<Map.Entry<Player, Integer>>(map.entrySet());
        Collections.sort(entryList, new MapValueComparator3());  
        Iterator<Map.Entry<Player, Integer>> iter = entryList.iterator();  
        Map.Entry<Player, Integer> tmpEntry;  
        while (iter.hasNext()) {  
            tmpEntry = iter.next();  
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
        }
        for (Map.Entry<Player, Integer> entry : sortedMap.entrySet()) {  
            System.out.println(entry.getKey() + " ：" + entry.getValue());
            return entry.getKey();  
        }  
        return null;
    }
}

/**
 * 比较器类  
 * <p>Title: MapValueComparator2<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
class MapValueComparator3 implements Comparator<Map.Entry<Player, Integer>> {  
    public int compare(Entry<Player, Integer> me1, Entry<Player, Integer> me2) {  
        return me2.getValue().compareTo(me1.getValue());  
    }  
} 
	
	

