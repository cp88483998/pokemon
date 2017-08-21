package pocket.pvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class PVPMatch{
	
	/**
	 * 同段位匹配，同等级匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player sameRankMatch(Player player){
		List<Player> playerList = Share.matchPool.get(player.getWinRank());
		
		Player playerRankMatch = null;
		if(playerList.size() > 0 && !playerList.isEmpty()){
			//System.out.println("同段位匹配！");
			playerRankMatch = matchRule(player, playerList);
		}
//		if(playerRankMatch != null){
//			playerRankMatch.setRankMatch(true);
//			System.out.println(Share.get(player)+" 同段位匹配到了："+Share.get(playerRankMatch));
//			for(int i=0;i<playerList.size();i++){
//				if(playerRankMatch.getUid() == playerList.get(i).getUid()){
//					playerList.remove(i);
//					break;
//				}
//			}
//			for(int i=0;i<playerList.size();i++){
//				if(player.getUid() == playerList.get(i).getUid()){
//					//先将player从playerList中移除，以防被别的玩家匹配到
//					playerList.remove(i);
//					break;
//				}
//			}
//		}
		return playerRankMatch;
	}
	/**
	 * 同段位匹配，不同等级匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player sameRankMatch3s(Player player){
		List<Player> playerList = Share.matchPool.get(player.getWinRank());

		Player playerRankMatch = null;
		if(playerList.size() > 0 && !playerList.isEmpty()){
			//System.out.println("同段位匹配！");
			playerRankMatch = matchRule1(player, playerList);
		}
//		if(playerRankMatch != null){
//			playerRankMatch.setRankMatch(true);
//			System.out.println(Share.get(player)+" 同段位匹配到了："+Share.get(playerRankMatch));
//			for(int i=0;i<playerList.size();i++){
//				if(playerRankMatch.getUid() == playerList.get(i).getUid()){
//					playerList.remove(i);
//					break;
//				}
//			}
//			for(int i=0;i<playerList.size();i++){
//				if(player.getUid() == playerList.get(i).getUid()){
//					//将player从playerList中移除，以防被别的玩家匹配到
//					playerList.remove(i);
//					break;
//				}
//			}
//		}
		return playerRankMatch;
	}
	
	/**
	 * 上下一个段位匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player oneRankMatch(Player player){
		//若同段位匹配不到，则进行非同段位匹配
		
		List<Player> all_players1 = findPlayers(1, player);
		Player playerOtherMatch = null;
		if(all_players1.size() > 0){
			playerOtherMatch = matchRule2(player, all_players1);
		}
//		if(playerOtherMatch != null){
//			playerOtherMatch.setRankMatch(true);
//			List<Player> playerList = Share.matchPool.get(playerOtherMatch.getWinRank());
//			for(int i=0;i<playerList.size();i++){
//				if(playerOtherMatch.getUid() == playerList.get(i).getUid()){
//					playerList.remove(i);
//					break;
//				}
//			}
//			List<Player> playerList1 = Share.matchPool.get(player.getWinRank());
//			for(int i=0;i<playerList1.size();i++){
//				if(player.getUid() == playerList1.get(i).getUid()){
//					//先将player从playerList中移除，以防被别的玩家匹配到
//					playerList1.remove(i);
//					break;
//				}
//			}
//		}
		return playerOtherMatch;
	}
	/**
	 * 上下二个段位匹配
	 * @param player
	 * @return 匹配到的玩家
	 */
	public static Player twoRankMatch(Player player){
		//System.out.println("上下二个段位匹配！");
		
		Player playerOtherMatch = null;
		List<Player> all_players2 = findPlayers(2, player);
		if(all_players2.size() > 0){
			playerOtherMatch = matchRule2(player, all_players2);
		}
//		if(playerOtherMatch != null){
//			List<Player> playerList = Share.matchPool.get(playerOtherMatch.getWinRank());
//			for(int i=0;i<playerList.size();i++){
//				if(playerOtherMatch.getUid() == playerList.get(i).getUid()){
//					playerList.remove(i);
//					break;
//				}
//			}
//			List<Player> playerList1 = Share.matchPool.get(player.getWinRank());
//			for(int i=0;i<playerList1.size();i++){
//				if(player.getUid() == playerList1.get(i).getUid()){
//					//先将player从playerList中移除，以防被别的玩家匹配到
//					playerList1.remove(i);
//					break;
//				}
//			}
//		}
		return playerOtherMatch;
	}
	/**
	 * 若同段位匹配不到，则进行非同段位匹配
	 * @param player
	 * @return player
	 */
	public static Player otherRankMatch(Player player){
		
		Player playerOtherMatch = null;
		List<Player> all_players = null;
		List<Player> playerList = null;
		for(int i=1;i<9;i++){
			all_players = findPlayers(i, player);
//			System.out.println("all_players1.size "+all_players.size());
			if(all_players.size() > 0){
				playerOtherMatch = matchRule2(player, all_players);
			}
			if(playerOtherMatch != null){
				playerList = Share.matchPool.get(playerOtherMatch.getWinRank());
				for(int j=0;j<playerList.size();j++){
					if(playerOtherMatch.getUid() == playerList.get(j).getUid()){
						playerList.remove(j);
						break;
					}
				}
				break;
			}
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
		int winsRank = player.getWinRank();
		//上下段位
		List<Player> letterRankList = Share.matchPool.get(winsRank-rankDiffer);
		List<Player> bigerRankList = Share.matchPool.get(winsRank+rankDiffer);
		
		Player player1 = null;
		if(letterRankList != null){
			for(int j=0;j<letterRankList.size();j++){
				player1 = letterRankList.get(j);
				//再先确定双方的状态
//				if(player.getStatus()!=1){
//					break;
//				}
//				if(player1.getStatus()!=1){
//					continue;
//				}
//				if(player1.getStatus()==1){
					if(player.getWinRank() - player1.getWinRank() == rankDiffer){
						all_players2.add(player1);
					}
//				}
			}
		}
		if(bigerRankList != null){
			for(int j=0;j<bigerRankList.size();j++){
				player1 = bigerRankList.get(j);
				//再先确定双方的状态
//				if(player.getStatus()!=1){
//					break;
//				}
//				if(player1.getStatus()!=1){
//					continue;
//				}
//				if(player1.getStatus()==1){
					if(player1.getWinRank() - player.getWinRank() == rankDiffer){
						all_players2.add(player1);
					}
//				}
			}
		}
		
		return all_players2;
	}
	
	/**
	 * 同段位使用到的匹配方法1
	 * 3秒钟内，同段位，等级相等的优先匹配
	 * @param player
	 * @param players
	 * @return 匹配到的玩家
	 */
	private static Player matchRule(Player player, List<Player> players){
		
		Player playerMatch = null;
		for(int i=0;i<players.size();i++){
			if(players.get(i).getLevel() == player.getLevel() 
					&& players.get(i).getUid() != player.getUid() 
					&& !players.get(i).isRankMatch()){
				playerMatch = players.get(i);
				playerMatch.setRankMatch(true);
				break;
			}
		}
		return playerMatch;
	}
	/**
	 * 同段位使用到的匹配方法2
	 * 超出3秒，同段位，等级相差5级以内的优先匹配
	 * @param player
	 * @param players
	 * @return 匹配到的玩家
	 */
	private static Player matchRule1(Player player, List<Player> players){
		
		Map<Player, Integer> level_map = new HashMap<Player, Integer>();
		int levelDiffer = 0;
		int level = player.getLevel();
		for(int i=0;i<players.size();i++){
			//计算等级差
			levelDiffer = Math.abs(level - players.get(i).getLevel());
			if(levelDiffer <= 5 
					&& player.getUid() != players.get(i).getUid()
					&& !players.get(i).isRankMatch()){
				level_map.put(players.get(i), levelDiffer);
			}
		}
		Player playerMatch = null;
		if(level_map.size() >= 1){
			playerMatch =  MinIntegerByValue(level_map);
			playerMatch.setRankMatch(true);
			
		}
		return playerMatch;
	}
	
	/**
	 * 非同段位使用到的匹配方法
	 * @param player
	 * @param players
	 * @return 匹配到的玩家
	 */
	private static Player matchRule2(Player player, List<Player> players){

		Map<Player, Integer> level_map = new HashMap<Player, Integer>();
		int levelDiffer = 0;
		int level = player.getLevel();
		for(int i=0;i<players.size();i++){
			levelDiffer = Math.abs(level - players.get(i).getLevel());
			if(levelDiffer <= 5 && player.getUid() != players.get(i).getUid()){
				level_map.put(players.get(i), levelDiffer);
			}	
		}
		Player playerMatch = null;
		if(level_map.size() >= 1){
			playerMatch = MinIntegerByValue(level_map);
			playerMatch.setRankMatch(true);
		}
		return playerMatch;
	}
	
	/*
	 * 求最小value对应的key值(Integer)
	 */
	public static Player MinIntegerByValue(Map<Player, Integer> map){
		if (map == null || map.isEmpty()) {  
            return null;  
        }  
		int a = 0;
		int b = 0;
		Player player = null;
        for(Player key : map.keySet()){
        	player = key;
        	a = map.get(key);
        }
    	for(Player key : map.keySet()){
    		b = map.get(key);
    		if(a >= b){
    			a = b;
    			player = key;
    		}
    	}
        return player;
	}
	
	/*
	 * 求最小value对应的key值(Double)
	 */
	public static Player MinDoubleByValue(Map<Player, Double> map){
		if (map == null || map.isEmpty()) {  
            return null;  
        }  
		double a = 0;
		double b = 0;
		Player player = null;
        for(Player key : map.keySet()){
        	player = key;
        	a = map.get(key);
        }
    	for(Player key : map.keySet()){
    		b = map.get(key);
    		if(a > b){
    			a = b;
    			player = key;
    		}
    	}
        return player;
	}
}	
	

