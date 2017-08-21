package pocket.pvp.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 玩家类
 * <p>Title: Player<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class Player {
	private String country;
	//机器人
	private boolean isRobot;
	//是否断线
	private boolean isOffline;
	//是否正在断线重连
	private boolean isConBack;
	//是否处于BattleReconData，断线玩家重连后，发送了ReqBattleReconData请求后，才能返回ResReconBattleRoundResult
	private boolean isBattleReconData;
	//是否处于ReconBattleRoundResult
	private boolean isReconBattleRoundResult;
	
	private JSONObject mustLeaveJO;
	//mustLeave播放完毕
	private boolean isMustLeaveOver;
	
	//战斗刚开始,用来判断是否已经加载完毕
	private boolean isBattleReady;
	
	//是否动画播放完成（用来判断是否直接发送bttleRoundResult）
	private boolean isPlayOver;
	
	//开始匹配时间(秒)
	private long startTime;
	//回合开始时间
	private long roundTime; 
	
	//是否非同段位匹配过
	private boolean hasSameRankMatch3s;
	private boolean hasOneRankMatch;
	private boolean hasTwoRankMatch;
	//是否正在匹配
	private boolean isRankMatch = false;
	
	//1.正在匹配  2.准备  3.逃跑  4.战斗开始 
	private int status;
	private User user;
	private CurRound curRound;//当前回合是使用技能？还是更换精灵？
//	private boolean isUseSkill;//当前回合是否可用技能
	private boolean freeChangeElve;//免费更换精灵的机会，当对手死亡时
	
	//battleRoundResult
	private JSONArray battleRoundResult;
	
	//当前回合数据roundOverResult
	private JSONObject joRound;
	
	private Map<Integer, List<RoundData>> roundDatas = new LinkedHashMap<Integer, List<RoundData>>();
	//当前回合
	private int round = 1;
	//断线回合，超过2回合视为逃跑
	private int disConRound;
		
	private int level;
	private long uid;
	private String username;
	private String nickname;
	private int gender;
	private int title_count;
	private int trainer_head_id;
	private int elves_head_id;
	private int fight;
	
	private int winPoint;
	private double winRate;
	private int winRank;
	private String serverName;

	//携带的精灵
//	private Map<String, RoleElves> RoleElves = new LinkedHashMap<String, RoleElves>();
	private Map<String, RoleElves> RoleElves;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean isRobot() {
		return isRobot;
	}
	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}
	public long getRoundTime() {
		return roundTime;
	}
	public void setRoundTime(long roundTime) {
		this.roundTime = roundTime;
	}
	public boolean isRankMatch() {
		return isRankMatch;
	}
	public void setRankMatch(boolean isRankMatch) {
		this.isRankMatch = isRankMatch;
	}
	public int getDisConRound() {
		return disConRound;
	}
	public void setDisConRound(int disConRound) {
		this.disConRound = disConRound;
	}
	public boolean isBattleReady() {
		return isBattleReady;
	}
	public void setBattleReady(boolean isBattleReady) {
		this.isBattleReady = isBattleReady;
	}
	public boolean isHasSameRankMatch3s() {
		return hasSameRankMatch3s;
	}
	public void setHasSameRankMatch3s(boolean hasSameRankMatch3s) {
		this.hasSameRankMatch3s = hasSameRankMatch3s;
	}
	public boolean isHasOneRankMatch() {
		return hasOneRankMatch;
	}
	public void setHasOneRankMatch(boolean hasOneRankMatch) {
		this.hasOneRankMatch = hasOneRankMatch;
	}
	public boolean isHasTwoRankMatch() {
		return hasTwoRankMatch;
	}
	public void setHasTwoRankMatch(boolean hasTwoRankMatch) {
		this.hasTwoRankMatch = hasTwoRankMatch;
	}
	public boolean isMustLeaveOver() {
		return isMustLeaveOver;
	}
	public void setMustLeaveOver(boolean isMustLeaveOver) {
		this.isMustLeaveOver = isMustLeaveOver;
	}
	public JSONObject getMustLeaveJO() {
		return mustLeaveJO;
	}
	public void setMustLeaveJO(JSONObject mustLeaveJO) {
		this.mustLeaveJO = mustLeaveJO;
	}
	public boolean isBattleReconData() {
		return isBattleReconData;
	}
	public void setBattleReconData(boolean isBattleReconData) {
		this.isBattleReconData = isBattleReconData;
	}
	public boolean isReconBattleRoundResult() {
		return isReconBattleRoundResult;
	}
	public void setReconBattleRoundResult(boolean isReconBattleRoundResult) {
		this.isReconBattleRoundResult = isReconBattleRoundResult;
	}
	public boolean isPlayOver() {
		return isPlayOver;
	}
	public void setPlayOver(boolean isPlayOver) {
		this.isPlayOver = isPlayOver;
	}
	public boolean isConBack() {
		return isConBack;
	}
	public void setConBack(boolean isConBack) {
		this.isConBack = isConBack;
	}
	public JSONArray getBattleRoundResult() {
		return battleRoundResult;
	}
	public void setBattleRoundResult(JSONArray battleRoundResult) {
		this.battleRoundResult = battleRoundResult;
	}
	public CurRound getCurRound() {
		return curRound;
	}
	public boolean isOffline() {
		return isOffline;
	}
	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}
	public boolean isFreeChangeElve() {
		return freeChangeElve;
	}
	public void setFreeChangeElve(boolean freeChangeElve) {
		this.freeChangeElve = freeChangeElve;
	}
	public void setCurRound(CurRound curRound) {
		this.curRound = curRound;
	}
	public JSONObject getJoRound() {
		return joRound;
	}
	public void setJoRound(JSONObject joRound) {
		this.joRound = joRound;
	}
	public Map<String, RoleElves> getRoleElves() {
		return RoleElves;
	}
	public void setRoleElves(Map<String, RoleElves> roleElves) {
		RoleElves = roleElves;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public synchronized Map<Integer, List<RoundData>> getRoundDatas() {
		return roundDatas;
	}
	public void setRoundDatas(Map<Integer, List<RoundData>> roundDatas) {
		this.roundDatas = roundDatas;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTrainer_head_id() {
		return trainer_head_id;
	}
	public void setTrainer_head_id(int trainer_head_id) {
		this.trainer_head_id = trainer_head_id;
	}
	public int getElves_head_id() {
		return elves_head_id;
	}
	public void setElves_head_id(int elves_head_id) {
		this.elves_head_id = elves_head_id;
	}
	public int getFight() {
		return fight;
	}
	public void setFight(int fight) {
		this.fight = fight;
	}
	public int getWinPoint() {
		return winPoint;
	}
	public void setWinPoint(int winPoint) {
		this.winPoint = winPoint;
	}
	public int getTitle_count() {
		return title_count;
	}
	public void setTitle_count(int title_count) {
		this.title_count = title_count;
	}
	public double getWinRate() {
		return winRate;
	}
	public void setWinRate(double winRate) {
		this.winRate = winRate;
	}
	public int getWinRank() {
		return winRank;
	}
	public void setWinRank(int winRank) {
		this.winRank = winRank;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	@Override
	public String toString() {
		return "Player [status=" + status + ", user=" + user 
				+ ", curRound=" + curRound + ", joRound=" + joRound + ", roundDatas=" + roundDatas + ", round=" + round
				+ ", startTime=" + startTime + ", uid=" + uid + ", username=" + username + ", nickname=" + nickname
				+ ", gender=" + gender + ", level=" + level + ", trainer_head_id=" + trainer_head_id
				+ ", elves_head_id=" + elves_head_id + ", fight=" + fight + ", wins_points=" + winPoint
				+ ", title_count=" + title_count + ", winRate=" + winRate + ", dan=" + winRank + ", serverName="
				+ serverName + ", RoleElves=" + RoleElves + "]";
	}
}
