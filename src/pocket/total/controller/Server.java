package pocket.total.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.chat.Controller.CachePrivateMsgEventHandler;
import pocket.chat.Controller.CachePublicMsgEventHandler;
import pocket.chat.Controller.PublicMsgEventHandler;
import pocket.chat.Controller.StorageOfflineMsgEventHandler;
import pocket.pvp.Controller.AddChildEffectBySkillEventHandler;
import pocket.pvp.Controller.BattleEventHandler;
import pocket.pvp.Controller.BattleReadyEventHandler;
import pocket.pvp.Controller.BattleReconDataEventHandler;
import pocket.pvp.Controller.BattleReconEventHandler;
import pocket.pvp.Controller.BattleRoundOrderEventHandler;
import pocket.pvp.Controller.BattleRoundResultEventHandler;
import pocket.pvp.Controller.BattleRunEventHandler;
import pocket.pvp.Controller.CancelMatchEventHandler;
import pocket.pvp.Controller.CanelChangeElvesEventHandler;
import pocket.pvp.Controller.ChangeElvesEventHandler_hasAuto;
import pocket.pvp.Controller.ChangeElvesSquadEventHandler;
import pocket.pvp.Controller.DisconnectEventHandler_hasAuto;
import pocket.pvp.Controller.GetAttackHitValueEventHandler;
import pocket.pvp.Controller.HBEventHandler;
import pocket.pvp.Controller.IsHitEventHandler;
import pocket.pvp.Controller.MatchRivalEventHandler;
import pocket.pvp.Controller.ReadyEventHandler;
import pocket.pvp.Controller.ReconBattleRoundResultEventHandler;
import pocket.pvp.Controller.RivalElvesExitEventHandler;
import pocket.pvp.Controller.RoundOverResultEventHandler_hasAuto;
import pocket.pvp.Controller.RunEventHandler;
import pocket.pvp.Controller.UseSkillEventHandler_hasAuto;
import pocket.pvp.dao.MongoPVPDao;
import pocket.pvp.dao.MongoPVPDaoImpl;
import pocket.pvp.entity.Player;
import pocket.pvp.service.DisconService;
import pocket.pvp.service.FightingDataService;
import pocket.pvp.service.PVPMatch;
import pocket.pvp.service.RobotMatch;
import pocket.pvp.service.RoundResultService;
import pocket.pvp.service.SkillService;
import pocket.timeSpace.Controller.BattleOverEventHandler;
import pocket.timeSpace.Controller.EnterTimeSpaceBattleEventHandler;
import pocket.timeSpace.Controller.EnterTimeSpaceEventHandler;
import pocket.timeSpace.Controller.ExitTimeSpaceEventHandler;
import pocket.total.entity.Share;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;
import pocket.total.util.SysMsgUtil;

public class Server extends SFSExtension {
	
	private final static String Heart_Client = "Heart_Client";
	private final static String ReqMatchRival = "ReqMatchRival";
	private final static String ReqChangeElvesSquad = "ReqChangeElvesSquad";
	private final static String ReqReady = "ReqReady";
	private final static String ReqRun = "ReqRun";
	private final static String ReqCanelMatch = "ReqCanelMatch";
	private final static String ReqBattle = "ReqBattle";
	private final static String ReqUseSkill = "ReqUseSkill";
	private final static String ReqBattleRoundOrder = "ReqBattleRoundOrder";
	private final static String ReqIsHit = "ReqIsHit";
	private final static String ReqGetAttackHitValue = "ReqGetAttackHitValue";
	private final static String ReqRoundOverResult = "ReqRoundOverResult";
	private final static String ReqChangeElves= "ReqChangeElves";
	private final static String ReqBattleRun = "ReqBattleRun";
	private final static String WorldHistory_Client = "WorldHistory_Client";
	private final static String ReqBattleRoundResult = "ReqBattleRoundResult";
	private final static String ReqAddChildEffectBySkill = "ReqAddChildEffectBySkill";
	private final static String ReqCanelChangeElves = "ReqCanelChangeElves";
	private final static String ReqBattleReconData = "ReqBattleReconData";
	private final static String ReqBattleRecon = "ReqBattleRecon";
	private final static String ReqReconBattleRoundResult = "ReqReconBattleRoundResult";
	private final static String ReqRivalElvesExit = "ReqRivalElvesExit";
	private final static String ReqBattleReady = "ReqBattleReady";
//	private final static String ReqElvesShowOver = "ReqElvesShowOver";
	private final static String ReqStorageOfflineMsg = "ReqStorageOfflineMsg";
	private final static String ReqCachePrivateMsg = "ReqCachePrivateMsg";
	private final static String ReqEnterTimeSpace = "ReqEnterTimeSpace";
	private final static String ReqEnterTimeSpaceBattle = "ReqEnterTimeSpaceBattle";
	private final static String ReqBattleOver = "ReqBattleOver";
	private final static String ReqExitTimeSpace = "ReqExitTimeSpace";
	private final static String ReqCatchElevs = "ReqCatchElevs";
	
	MongoPVPDao dao = MongoPVPDaoImpl.getInstance();
	
	@Override
	public void init() {
		
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.PUBLIC_MESSAGE, PublicMsgEventHandler.class);
//		addEventHandler(SFSEventType.PRIVATE_MESSAGE, PrivateMsgEventHandler.class);
		addEventHandler(SFSEventType.USER_DISCONNECT, DisconnectEventHandler_hasAuto.class);
		addRequestHandler(WorldHistory_Client, CachePublicMsgEventHandler.class);
		
		addRequestHandler(Heart_Client, HBEventHandler.class);
		//请求同段位匹配
		addRequestHandler(ReqMatchRival, MatchRivalEventHandler.class);
		//请求更换精灵阵容
		addRequestHandler(ReqChangeElvesSquad, ChangeElvesSquadEventHandler.class);
		//请求战斗准备
		addRequestHandler(ReqReady, ReadyEventHandler.class);
		//请求逃跑
		addRequestHandler(ReqRun, RunEventHandler.class);
		//取消匹配
		addRequestHandler(ReqCanelMatch, CancelMatchEventHandler.class);
		//战斗
		addRequestHandler(ReqBattle, BattleEventHandler.class);
		//请求战斗准备，战斗刚开始时若对手还没加载进来，这时我方使用技能，对手就会报错，需要双方都发送了ReqBattleReady，这时服务端发送响应给双方，客户端才能操作。
		addRequestHandler(ReqBattleReady, BattleReadyEventHandler.class);
		
		//请求使用技能
		addRequestHandler(ReqUseSkill, UseSkillEventHandler_hasAuto.class);
		//请求获取技能的先后手
		addRequestHandler(ReqBattleRoundOrder, BattleRoundOrderEventHandler.class);
		//请求是否命中
		addRequestHandler(ReqIsHit, IsHitEventHandler.class);
		//请求获取伤害值
		addRequestHandler(ReqGetAttackHitValue , GetAttackHitValueEventHandler.class);
		//请求当前回合结束
		addRequestHandler(ReqRoundOverResult, RoundOverResultEventHandler_hasAuto.class);
		//请求更换精灵
		addRequestHandler(ReqChangeElves, ChangeElvesEventHandler_hasAuto.class);
		//请求战斗中逃跑
		addRequestHandler(ReqBattleRun, BattleRunEventHandler.class);
		//请求取消精灵选择
		addRequestHandler(ReqCanelChangeElves, CanelChangeElvesEventHandler.class);
		//请求重连
		addRequestHandler(ReqBattleRecon, BattleReconEventHandler.class);
		//请求重连战斗数据
		addRequestHandler(ReqBattleReconData, BattleReconDataEventHandler.class);
		//重连战斗回合数据
		addRequestHandler(ReqReconBattleRoundResult, ReconBattleRoundResultEventHandler.class);
		//请求对手必须下场
		addRequestHandler(ReqRivalElvesExit, RivalElvesExitEventHandler.class);
		//是否动画播放完成（用来判断是否直接发送bttleRoundResult）
//		addRequestHandler(ReqElvesShowOver, ElvesShowOverEventHandler.class);
		
		//请求战斗回合数据
		addRequestHandler(ReqBattleRoundResult, BattleRoundResultEventHandler.class);
		//请求增加子效果
		addRequestHandler(ReqAddChildEffectBySkill, AddChildEffectBySkillEventHandler.class);
		//请求存储离线消息
		addRequestHandler(ReqStorageOfflineMsg, StorageOfflineMsgEventHandler.class);
		//请求获取离线消息
		addRequestHandler(ReqCachePrivateMsg, CachePrivateMsgEventHandler.class);
		
		//请求进入时空缝隙
		addRequestHandler(ReqEnterTimeSpace, EnterTimeSpaceEventHandler.class);
		//请求进入时空缝隙战斗
		addRequestHandler(ReqEnterTimeSpaceBattle, EnterTimeSpaceBattleEventHandler.class);
		//请求时空缝隙战斗结束
		addRequestHandler(ReqBattleOver, BattleOverEventHandler.class);
		//请求退出时空缝隙
		addRequestHandler(ReqExitTimeSpace, ExitTimeSpaceEventHandler.class);
		
		
		//请求抓捕
		addRequestHandler(ReqCatchElevs, CatchElevsEventHandler.class);
		
		//超时监听
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		sfs.getTaskScheduler().scheduleAtFixedRate(new MatchRunner(), 0, 2, TimeUnit.SECONDS);
		sfs.getTaskScheduler().scheduleAtFixedRate(new TimeMonitorRunner(), 0, 2, TimeUnit.SECONDS);
		
	}

	/*
	 * HTTP请求消息处理
	 * 
	 * @see
	 * com.smartfoxserver.v2.extensions.BaseSFSExtension#handleInternalMessage(
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public Object handleInternalMessage(String cmdName, Object httpParams) {
//		trace(String.format("Called by: %s, CMD: %s, Params: %s", Thread.currentThread().getName(), cmdName, httpParams));
		boolean result = false;
		//1.发送系统消息
		if (cmdName.equals("message")) {
			String[] info = ((String) httpParams).split("&");
			String message = "4|F_G|"+info[0];
			int roomId = Integer.parseInt(info[1]);
			ISFSObject params = new SFSObject();
			params.putUtfString("SystemMsg", message);
			
			Collection<ISession> sessions = getParentZone().getRoomById(roomId).getSessionList();
			if(sessions.size()!= 0){
				 sfsApi.sendAdminMessage(null, message, params, sessions);
				 result = true;
			}
		}
		//2.发送当前房间人数
		if(cmdName.equals("count")){
			int roomId = Integer.parseInt(httpParams.toString()) ;
			int currentCount = getParentZone().getRoomById(roomId).getUserList().size();
			return currentCount;
		}

		return result;
	}
	
	public static void main(String[] args) {
		new Server();
	}
	
	/**
	 * 时间监听线程
	 * <p>Title: MatchRunner<／p>
	 * <p>Description:  3秒钟内，同段位，同等级的优先匹配；超出3秒扩大匹配范围，同时在等待过程中有满足优先条件的进入匹配队伍还是优先选择；
	  					超出3秒，同段位，等级相差5级以内的优先匹配
						超出8秒，上下1个段位，等级相差5级以内的优先匹配
						超出12秒，上下2个段位，等级相差5级以内的优先匹配
						<／p>
	 * <p>Company: LTGames<／p>	
	 * @author 陈鹏
	 * @date 2017年4月11日
	 */
	class MatchRunner implements Runnable{
		@Override
		public void run() {
			
			try {
				long nowTime = System.currentTimeMillis()/1000;
				List<Player> playerList = null;
				boolean isRobot = false;
//				trace("-----nowTime:"+nowTime);
				
				/*
				 * 1.遍历每个段位集合，根据时间做不同的匹配处理
				 */
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if((hour>=12 && hour<=14) || (hour>=19 && hour<=21)){
				
					Iterator<Integer> keys = Share.matchPool.keySet().iterator();
					while(keys.hasNext()){
						Integer key = keys.next();
						playerList = Share.matchPool.get(key);
						//遍历集合内各个玩家，改成迭代器(iterator)
						//因为若用for循环，当一个元素被处理时，该List的大小(size)就会缩减，同时也改变了索引的指向。所以，在迭代的过程中使用索引，将无法从List中正确地处理多个指定的元素。
//						for(int i=0;i<playerList.size();i++){
						Iterator<Player> iter = playerList.iterator();
						while(iter.hasNext()){	
//							Player player = playerList.get(i);
							Player player = iter.next();
							long timeDiffer = nowTime - player.getStartTime();
							
							Player playerMatch = null;
							//三秒内，同段位，同等级匹配
							if(timeDiffer <= 3 && !player.isRankMatch()){
								playerMatch = PVPMatch.sameRankMatch(player);
							}
							//超出3秒，同段位，等级相差5级以内的优先匹配
							if(timeDiffer > 3 && timeDiffer <= 8 && !player.isRankMatch()){
//								trace(player.getUid()+" sameRankMatch time over "+timeDiffer+"s, start otherLevelMatch!");
								playerMatch = PVPMatch.sameRankMatch3s(player);
							}
							//若时间大于8秒，则开始上下一个段位匹配
							if(timeDiffer > 8 && timeDiffer <= 12 && !player.isRankMatch()){
//								trace(player.getUid()+" sameRankMatch time over "+timeDiffer+"s, start oneRankMatch!");
								playerMatch = PVPMatch.oneRankMatch(player);
							}
							//若时间大于12秒，则开始上下两个段位匹配
							if(timeDiffer > 12 && timeDiffer <= 35 && !player.isRankMatch()){
//								trace(player.getUid()+" oneRankMatch time over "+timeDiffer+"s, start twoRankMatch!");
								playerMatch = PVPMatch.twoRankMatch(player);
							}
							//若时间大于35秒，则匹配机器人
							if(timeDiffer > 35 && timeDiffer <= 60 && !player.isRankMatch()){
								trace(player.getUid()+" twoRankMatch time over "+timeDiffer+"s, start robotMatch!");
								playerMatch = RobotMatch.getRobot(player);
								isRobot = true;
							}
							//若时间大于60秒，则删除该玩家
							if(timeDiffer > 60){
								trace(player.getUid()+" oneRankMatch time over 60s, remove player!");
//								playerList.remove(i);
								iter.remove();
							}
							if(playerMatch != null){
								
								//修改玩家匹配状态
								player.setRankMatch(true);
								playerMatch.setRankMatch(true);
								
								// 更新PVP数据记录
								if(!isRobot){
									dao.insertOneRecord(player, playerMatch, false);
									dao.insertOneRecord(playerMatch, player, false);
								}else{
									dao.insertOneRecord(player, playerMatch, true);
								}
								
								int roomId = Share.roomId;
								
								List<Player> list = new ArrayList<Player>();
								list.add(player);
								list.add(playerMatch);
								Share.matchMap.put(String.valueOf(roomId), list);
								
								//将对手信息解析到JSONObject,发给自己
								JSONObject jo1 = FightingDataService.playerToJo(playerMatch, roomId);
								ISFSObject resParams = new SFSObject();
								resParams.putUtfString("Infor", jo1.toString());
								send("ResGetMatchRival", resParams, player.getUser());
								
								//若对手不是机器人
								if(!isRobot){
									//将自己信息解析到JSONObject,发给对手
									JSONObject jo2 = FightingDataService.playerToJo(player, roomId);
									resParams.putUtfString("Infor", jo2.toString());
									send("ResGetMatchRival", resParams, playerMatch.getUser());
								}
								
								//移除匹配池中的玩家
//								playerList.remove(i);
								iter.remove();
								if(!isRobot){
									List<Player> playerList1 = Share.matchPool.get(playerMatch.getWinRank());
									Iterator<Player> iter1 = playerList1.iterator();
									while(iter1.hasNext()){
										Player p = iter1.next();
										if(playerMatch.getUid() == p.getUid()){
											iter1.remove();
											trace("-----remove matcher from pool-----");
											break;
										}
									}
								}
								
								//递增匹配房间号
								Share.roomId++;
//							}else{
//								player.setRankMatch(false);
							}
						}
					}
				}
			} catch (Exception e) {
				//异常处理
				e.printStackTrace();
			}
		}	
	}
	
	class TimeMonitorRunner implements Runnable{

		@Override
		public void run() {
			try {
				/*
				 * 2.超时处理,遍历匹配成功池matchMap中的玩家
				 */
				long nowTime = System.currentTimeMillis()/1000;
				List<Player> list = null;
				Player player1 = null;
				Player player2 = null;
//					trace("server nowTime :"+nowTime);
				Iterator<String> keys = Share.matchMap.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					list = Share.matchMap.get(key);
					//若回合时间大于60秒，就自动选技能，同理断线处理
					player1 = list.get(0);
					player2 = list.get(1);
					if(nowTime - player1.getRoundTime() >= 60 && player1.getStatus()==4 && !player1.isOffline()){
						roundTimeOverHandle(player1, player2);
					}
					if(nowTime - player2.getRoundTime() >= 60 && player2.getStatus()==4 && !player2.isOffline()){
						roundTimeOverHandle(player2, player1);
					}
				}
				/*
				 * 3.自动发送系统消息:
				 * 在线人数超过50时：60s~180s随机CD，50%概率连发3次，50%概率发1次,
				 * 在线人数20~50时：120s~300s随机CD，发送1次,
				 * 在线人数20以下时：300s~600s随机CD，发送1次.
				 * 先获取当前时间
				 * 获取当前人数
				 * 根据人数随机出发送CD时间
				 * 计算发送次数
				 */
				int intervalTime = Share.intervalTime;
				if(nowTime - Share.startTime > intervalTime){
					int sendCount = Share.sendCount;
					for(int i=0;i<sendCount;i++){
						sendSysMsg();
					}
					getTntervalTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 发送系统消息
	 */
	public void sendSysMsg(){
		//玩家名
		List<String> robotNames = StaticClass.robotNames;
		int i = MyUtil.random(robotNames.size());
		String robotName = robotNames.get(i);
		//精灵名
		JSONArray ja = StaticClass.Elves_data_js;
		i = MyUtil.random(ja.size());
		JSONObject jo = ja.getJSONObject(i);
		while(!jo.getString("Elves_rarity").equals("史诗")){
			i = MyUtil.random(ja.size());
			jo = ja.getJSONObject(i);
		}
		String elveName = jo.getString("Elves_name");
		System.out.println("elveName :"+elveName);
		Collection<ISession> sessions = getParentZone().getSessionList();
		
		SysMsgUtil instance = SysMsgUtil.getInstance();
		String param1 = instance.getPreperty("param1");
		String param2 = instance.getPreperty("param2");
		String param3 = instance.getPreperty("param3");
		String message = param1 + robotName +" "+ param2 + elveName + param3;
		ISFSObject params = new SFSObject();
		params.putUtfString("SystemMsg", message);
		if(sessions.size()!= 0){
			sfsApi.sendAdminMessage(null, message, params, sessions);
		}
	}
	
	/*
	 * 随机系统消息发送CD时间
	 */
	public void getTntervalTime(){
		int num = getParentZone().getSessionList().size();
		int intervalTime = 0;
		int sendCount = 1;
		if(num > 50){
			intervalTime = MyUtil.random(600, 900);
			if(MyUtil.random(100) > 50){
				sendCount = 3;
			}
		}
		if(num >20 && num <=50){
			intervalTime = MyUtil.random(600, 900);
		}
		if(num <= 20){
			intervalTime = MyUtil.random(600, 900);
		}
		Share.intervalTime = intervalTime;
		Share.sendCount = sendCount;
		Share.startTime = System.currentTimeMillis()/1000;
	}
	
	/*
	 * 回合时间超时处理
	 */
	public void roundTimeOverHandle(Player player1, Player player2){
		//视player1为断线
		player1.setOffline(true);
		//并踢出
		sfsApi.kickUser(player1.getUser(), null, "disConnect", 0);
//		sfsApi.sendExtensionResponse("", new SFSObject(), player1.getUser(), player1.getUser().getLastJoinedRoom(), true);
		
		ISFSObject resParams = new SFSObject();
		if(!player2.isOffline() && player1.getStatus()==4){
			/*
			 * 若player2在线,判断我方需要做什么？选技能，更换精灵，取消更换。
			 */
			if(!player1.isBattleReady()){
				player1.setBattleReady(true);
				send("ResBattleReady", null, player2.getUser());
			}
			
			if(player2.getJoRound() != null && player1.getJoRound() == null){
//				trace("disConnect update!");
//				trace("player2 :"+player2.getJoRound());
				JSONObject jo = DisconService.roundOverResult(player2, player1);
				resParams.putUtfString("Infor", jo.toString());
				send("ResRoundOverResult", resParams, player2.getUser());
				
			}
			
			//1.判断对手是否已经使用了技能或者更换了精灵
			JSONArray joAll = null;
			
			//获取当前的精灵
			boolean isOwnDie = player1.getCurRound().getNowRoleElves().getElve().getIsDie();
			//判断当前精灵是否死亡
			if(isOwnDie){
//				trace("player1 now elve isDie!");
				JSONObject jo = DisconService.dieChangeElve(player1, player2);
  				resParams.putUtfString("Infor", jo.toString());
  				send("ResRivalChangeElves", resParams, player2.getUser());
  				
			}
			//若对手已经使用了技能或者更换了精灵，那我得自动选精灵或技能
			if(player2.getCurRound().getResultType() != 10 && player2.isPlayOver()){
//				trace("player2 has use skill or change elve!");
				//2.自动选技能
				JSONObject jo = SkillService.autoChooseSkill(player1, player2);
				resParams.putUtfString("Infor", jo.toString());
				send("ResRivalUseSkill", resParams, player2.getUser());
				
				//若对手上回合使用多回合技能，就直接计算
				if(player2.getCurRound().getResultType() == 1){
					joAll = RoundResultService.SpeciesJudgeSkill(player1, player2);
				}
				if(player2.getCurRound().getResultType() == 0){
					joAll = RoundResultService.SpeciesJudgeCha(player2, player1);
				}
    			
    			if(joAll != null){
    				player1.setBattleRoundResult(joAll);
    				player2.setBattleRoundResult(joAll);
    				//理论上joAll != null
    				JSONObject joObject = new JSONObject();
//    				joObject.put("roundChildEffect", player1.getCurRound().getRoundChildEffect());
    				joObject.put("battleRoundResult", joAll);
    				resParams.putUtfString("Infor", joObject.toString());
    				send("ResBattleRoundResult", resParams, player2.getUser());
//    				trace("ResBattleRoundResult send to vital!");
    			}
			}
			//2.mustLeave播放完毕
			if(player2.isMustLeaveOver()){
				SkillService.autoChooseRoleElves(player1);
//				ChangeElvesEventHandler_hasAuto.updateOldRoleElves(rivalElvesJO, player2);
				
				//发给对手
				JSONObject jo2 = new JSONObject();
				jo2.put("elves", player1.getCurRound().getNowRoleElves().getElve());
				jo2.put("isUseSkill", true);
				jo2.put("isFreeChangeElve", false);
				jo2.put("randomArr", player1.getCurRound().getNowRoleElves().getRandomList());
				resParams.putUtfString("Infor", jo2.toString());
				send("ResRivalChangeElves", resParams, player2.getUser());
				
				player2.setMustLeaveOver(false);
			}
			//3.免费更换进会
			if(player1.isFreeChangeElve()){
				send("ResRivalCanelChangeElves", null, player2.getUser());
				player1.setFreeChangeElve(false);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		
	}
	
	
}



