package pocket.pvp.entity;


import net.sf.json.JSONArray;

public class RoundData {
	private int round;//第几回合
	private boolean isHit;//是否命中
	private String attackKey;//攻击方精灵唯一ID
	private String beAttackKey;//被攻击方精灵唯一ID
	private String skillId;//攻击方使用的技能
	private int hitValue;//伤害值
	private double xkMultiple;//精灵与技能的相克系数
	private boolean isVital;//是否是要害攻击
	private boolean isRivalDie;//对手是否死亡（被攻击方）
	private boolean isOwnDie;//我方是否死亡（攻击方）
	//100个随机float
//	private float[] attackRandomArr;
//	private float[] beAttackRandomArr;
	private JSONArray attackRandomArr;
	private JSONArray beAttackRandomArr;
	
	//0 更换精灵 1 使用技能
	private int resultType;
	private String beChangeElvesId;
	private String changeElvesId;
	
	
	public String getBeChangeElvesId() {
		return beChangeElvesId;
	}
	public void setBeChangeElvesId(String beChangeElvesId) {
		this.beChangeElvesId = beChangeElvesId;
	}
	public String getChangeElvesId() {
		return changeElvesId;
	}
	public void setChangeElvesId(String changeElvesId) {
		this.changeElvesId = changeElvesId;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	public JSONArray getAttackRandomArr() {
		return attackRandomArr;
	}
	public void setAttackRandomArr(JSONArray attackRandomArr) {
		this.attackRandomArr = attackRandomArr;
	}
	public JSONArray getBeAttackRandomArr() {
		return beAttackRandomArr;
	}
	public void setBeAttackRandomArr(JSONArray beAttackRandomArr) {
		this.beAttackRandomArr = beAttackRandomArr;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public boolean getIsHit() {
		return isHit;
	}
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	public String getAttackKey() {
		return attackKey;
	}
	public void setAttackKey(String attackKey) {
		this.attackKey = attackKey;
	}
	public String getBeAttackKey() {
		return beAttackKey;
	}
	public void setBeAttackKey(String beAttackKey) {
		this.beAttackKey = beAttackKey;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public int getHitValue() {
		return hitValue;
	}
	public void setHitValue(int hitValue) {
		this.hitValue = hitValue;
	}
	public double getXkMultiple() {
		return xkMultiple;
	}
	public void setXkMultiple(double xkMultiple) {
		this.xkMultiple = xkMultiple;
	}
	public boolean getIsVital() {
		return isVital;
	}
	public void setVital(boolean isVital) {
		this.isVital = isVital;
	}
	public boolean getIsRivalDie() {
		return isRivalDie;
	}
	public void setRivalDie(boolean isRivalDie) {
		this.isRivalDie = isRivalDie;
	}
	public boolean getIsOwnDie() {
		return isOwnDie;
	}
	public void setOwnDie(boolean isOwnDie) {
		this.isOwnDie = isOwnDie;
	}
	@Override
	public String toString() {
		return "RoundData [isHit=" + isHit + ", attackKey=" + attackKey + ", beAttackKey=" + beAttackKey + ", skillId="
				+ skillId + ", hitValue=" + hitValue + ", xkMultiple=" + xkMultiple + ", isVital=" + isVital
				+ ", isRivalDie=" + isRivalDie + ", isOwnDie=" + isOwnDie + "]";
	}
	
}
