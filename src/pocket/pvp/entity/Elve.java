package pocket.pvp.entity;

//Elves_data配置表
public class Elve {
	
	private int eLvesID;
	private int rarity;
	private String eLvesname;
	private String studySkillList;
//	private Map<String, SkillDataVo> studySkills;
	//子效果列表
//	private List<Child_Effect> Child_Effect_List;
	
	//精灵属性
	private String elves_type;
    //性格 从1开始
    private int character;
    //经验值
    private int exp;
    //血条
    private int hp;
    
    //当id，性格，性别都相同时，最后通过该值区分
    private int sameIndex;

    //6个个体值等级 从0开始
    //生命值等级
    private int hpLevel;
    //物攻等级
    private int wuAttackLevel;
    //物防等级
    private int wuDefenseLevel;
    //物攻等级
    private int teAttackLevel;
    //物防等级
    private int teDefenseLevel;
    //速度等级
    private int speedLevel;
    //技能引发的增加速度等级
    private int addSpeedLv;

    //携带的树果（只会有一个）
    private String carryitem;
    //携带的装备（携带物）（只会有一个）
    private String carryequip;
    //携带的特性效果(只会有一个)
    private String features;
    //特训id 从1开始(特训级别)
    private int trainedLevel;
    //突破id 从1开始(突破级别)
    private int breakthroughLevel;
    //等级 从开始
    private int level;
    
    //命中等级
    private int hit_level;
    //闪避等级
    private int dodge_level;
    
    private int upgradeLv;
    private boolean isTraining;
    private boolean isBag;
    private boolean isSpirteLeague;
    private boolean isMining;
    private boolean isLock;
    private boolean isDie;
    private String mega;
    private int gender;
    private String own;
    
	public int getAddSpeedLv() {
		return addSpeedLv;
	}
	public void setAddSpeedLv(int addSpeedLv) {
		this.addSpeedLv = addSpeedLv;
	}
	public boolean getIsDie() {
		return isDie;
	}
	public void setDie(boolean isDie) {
		this.isDie = isDie;
	}
	public String getElves_type() {
		return elves_type;
	}
	public void setElves_type(String elves_type) {
		this.elves_type = elves_type;
	}
	public int getUpgradeLv() {
		return upgradeLv;
	}
	public void setUpgradeLv(int upgradeLv) {
		this.upgradeLv = upgradeLv;
	}
	public boolean getIsTraining() {
		return isTraining;
	}
	public void setTraining(boolean isTraining) {
		this.isTraining = isTraining;
	}
	public boolean getIsBag() {
		return isBag;
	}
	public void setBag(boolean isBag) {
		this.isBag = isBag;
	}
	public boolean getIsSpirteLeague() {
		return isSpirteLeague;
	}
	public void setSpirteLeague(boolean isSpirteLeague) {
		this.isSpirteLeague = isSpirteLeague;
	}
	public boolean getIsMining() {
		return isMining;
	}
	public void setMining(boolean isMining) {
		this.isMining = isMining;
	}
	public boolean getIsLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public String getMega() {
		return mega;
	}
	public void setMega(String mega) {
		this.mega = mega;
	}
	public String getOwn() {
		return own;
	}
	public void setOwn(String own) {
		this.own = own;
	}
	public String getStudySkillList() {
		return studySkillList;
	}
	public void setStudySkillList(String studySkillList) {
		this.studySkillList = studySkillList;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
		if(hp == 0){
			setDie(true);
		}else{
			setDie(false);
		}
	}
	public int geteLvesID() {
		return eLvesID;
	}
	public void seteLvesID(int eLvesID) {
		this.eLvesID = eLvesID;
	}
	public int getRarity() {
		return rarity;
	}
	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String geteLvesname() {
		return eLvesname;
	}
	public void seteLvesname(String eLvesname) {
		this.eLvesname = eLvesname;
	}
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getSameIndex() {
		return sameIndex;
	}
	public void setSameIndex(int sameIndex) {
		this.sameIndex = sameIndex;
	}
	public int getHpLevel() {
		return hpLevel;
	}
	public void setHpLevel(int hpLevel) {
		this.hpLevel = hpLevel;
	}
	public int getWuAttackLevel() {
		return wuAttackLevel;
	}
	public void setWuAttackLevel(int wuAttackLevel) {
		this.wuAttackLevel = wuAttackLevel;
	}
	public int getWuDefenseLevel() {
		return wuDefenseLevel;
	}
	public void setWuDefenseLevel(int wuDefenseLevel) {
		this.wuDefenseLevel = wuDefenseLevel;
	}
	public int getTeAttackLevel() {
		return teAttackLevel;
	}
	public void setTeAttackLevel(int teAttackLevel) {
		this.teAttackLevel = teAttackLevel;
	}
	public int getTeDefenseLevel() {
		return teDefenseLevel;
	}
	public void setTeDefenseLevel(int teDefenseLevel) {
		this.teDefenseLevel = teDefenseLevel;
	}
	public int getSpeedLevel() {
		return speedLevel;
	}
	public void setSpeedLevel(int speedLevel) {
		this.speedLevel = speedLevel;
	}
	public String getCarryitem() {
		return carryitem;
	}
	public void setCarryitem(String carryitem) {
		this.carryitem = carryitem;
	}
	public String getCarryequip() {
		return carryequip;
	}
	public void setCarryequip(String carryequip) {
		this.carryequip = carryequip;
	}
	public int getTrainedLevel() {
		return trainedLevel;
	}
	public void setTrainedLevel(int trainedLevel) {
		this.trainedLevel = trainedLevel;
	}
	public int getBreakthroughLevel() {
		return breakthroughLevel;
	}
	public void setBreakthroughLevel(int breakthroughLevel) {
		this.breakthroughLevel = breakthroughLevel;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public int getHit_level() {
		return hit_level;
	}
	public void setHit_level(int hit_level) {
		this.hit_level = hit_level;
	}
	public int getDodge_level() {
		return dodge_level;
	}
	public void setDodge_level(int dodge_level) {
		this.dodge_level = dodge_level;
	}
	@Override
	public String toString() {
		return "Elve [eLvesID=" + eLvesID + ", rarity=" + rarity + ", eLvesname=" + eLvesname + ", studySkillList="
				+ studySkillList + ", elves_type=" + elves_type + ", character=" + character + ", exp=" + exp + ", hp="
				+ hp + ", sameIndex=" + sameIndex + ", hpLevel=" + hpLevel + ", wuAttackLevel=" + wuAttackLevel
				+ ", wuDefenseLevel=" + wuDefenseLevel + ", teAttackLevel=" + teAttackLevel + ", teDefenseLevel="
				+ teDefenseLevel + ", speedLevel=" + speedLevel + ", addSpeedLv=" + addSpeedLv + ", carryitem="
				+ carryitem + ", carryequip=" + carryequip + ", features=" + features + ", trainedLevel=" + trainedLevel
				+ ", breakthroughLevel=" + breakthroughLevel + ", level=" + level + ", hit_level=" + hit_level
				+ ", dodge_level=" + dodge_level + ", upgradeLv=" + upgradeLv + ", isTraining=" + isTraining
				+ ", isBag=" + isBag + ", isSpirteLeague=" + isSpirteLeague + ", isMining=" + isMining + ", isLock="
				+ isLock + ", isDie=" + isDie + ", mega=" + mega + ", gender=" + gender + ", own=" + own + "]";
	}
	
}
