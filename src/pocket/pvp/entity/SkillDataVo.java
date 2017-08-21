package pocket.pvp.entity;

import java.util.List;

public class SkillDataVo {
	private int Skill_number;
	private String Skill_name;
	private int Skill_id;//这个才是技能ID
	private String Skill_ability;//技能属性
	private String Skill_type;//判断技能是物攻还是特攻
	private String ATK_tpye;
	private String species;//技能类型，重要
	private int Skill_power;//技能威力，重要
	private int PP;//技能点数，重要，无点数无法释放，每次使用消耗扣除
	private int Hit;//基础命中率
	private int Priority;//技能优先值，两个技能比较，优先值高的必定先手
	private int Target;//目标（1敌人，0自身，2场地，3天气）
	private int Target_number;//技能攻击的目标数量
	private String ATK_time;//攻击次数
	private String Ratio;//伤害倍率，对部分技能类型有效；（1固定值，2乘以等级的系数,3受到物理伤害乘以倍数，4受到特殊伤害系数乘以倍数，5当前HP乘以系数，6固定倍数）
	private int Death;//是否致死，对应一击必杀类型，命中即死
	private int Vital;//是否要害，计算要害判断时，要害等级增加对应值
	private String Skill_text;
	
	//buff施加概率
	private int Rate;
	
	//特殊条件
	private String take;
	private List<Integer> takeArr;
	
	//对应值
	private String take_value;
	private List<String> takeValueArr;
	
	private List<String > textArr;
	private long round;
	
	
	public List<Integer> getTakeArr() {
		return takeArr;
	}
	public void setTakeArr(List<Integer> takeArr) {
		this.takeArr = takeArr;
	}
	public List<String> getTakeValueArr() {
		return takeValueArr;
	}
	public void setTakeValueArr(List<String> takeValueArr) {
		this.takeValueArr = takeValueArr;
	}
	public List<String> getTextArr() {
		return textArr;
	}
	public void setTextArr(List<String> textArr) {
		this.textArr = textArr;
	}
	public long getRound() {
		return round;
	}
	public void setRound(long round) {
		this.round = round;
	}
	public int getRate() {
		return Rate;
	}
	public void setRate(int rate) {
		Rate = rate;
	}
	public String getTake() {
		return take;
	}
	public void setTake(String take) {
		this.take = take;
	}
	public String getTake_value() {
		return take_value;
	}
	public void setTake_value(String take_value) {
		this.take_value = take_value;
	}
	public int getSkill_number() {
		return Skill_number;
	}
	public void setSkill_number(int skill_number) {
		this.Skill_number = skill_number;
	}
	public String getSkill_name() {
		return Skill_name;
	}
	public void setSkill_name(String Skill_name) {
		this.Skill_name = Skill_name;
	}
	public int getSkill_id() {
		return Skill_id;
	}
	public void setSkill_id(int Skill_id) {
		this.Skill_id = Skill_id;
	}
	public String getSkill_ability() {
		return Skill_ability;
	}
	public void setSkill_ability(String Skill_ability) {
		this.Skill_ability = Skill_ability;
	}
	public String getSkill_type() {
		return Skill_type;
	}
	public void setSkill_type(String skill_type) {
		this.Skill_type = skill_type;
	}
	public String getATK_tpye() {
		return ATK_tpye;
	}
	public void setATK_tpye(String aTK_tpye) {
		ATK_tpye = aTK_tpye;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public int getSkill_power() {
		return Skill_power;
	}
	public void setSkill_power(int skill_power) {
		Skill_power = skill_power;
	}
	public int getPP() {
		return PP;
	}
	public void setPP(int pP) {
		PP = pP;
	}
	public int getHit() {
		return Hit;
	}
	public void setHit(int hit) {
		Hit = hit;
	}
	public int getPriority() {
		return Priority;
	}
	public void setPriority(int priority) {
		Priority = priority;
	}
	public int getTarget() {
		return Target;
	}
	public void setTarget(int target) {
		Target = target;
	}
	public int getTarget_number() {
		return Target_number;
	}
	public void setTarget_number(int target_number) {
		Target_number = target_number;
	}
	public String getATK_time() {
		return ATK_time;
	}
	public void setATK_time(String aTK_time) {
		ATK_time = aTK_time;
	}
	public String getRatio() {
		return Ratio;
	}
	public void setRatio(String ratio) {
		Ratio = ratio;
	}
	public int getDeath() {
		return Death;
	}
	public void setDeath(int death) {
		Death = death;
	}
	public int getVital() {
		return Vital;
	}
	public void setVital(int vital) {
		Vital = vital;
	}
	public String getSkill_text() {
		return Skill_text;
	}
	public void setSkill_text(String skill_text) {
		Skill_text = skill_text;
	}
	@Override
	public String toString() {
		return "SkillDataVo [Skill_number=" + Skill_number + ", Skill_name=" + Skill_name + ", Skill_id=" + Skill_id
				+ ", Skill_ability=" + Skill_ability + ", Skill_type=" + Skill_type + ", ATK_tpye=" + ATK_tpye
				+ ", species=" + species + ", Skill_power=" + Skill_power + ", PP=" + PP + ", Hit=" + Hit
				+ ", Priority=" + Priority + ", Target=" + Target + ", Target_number=" + Target_number + ", ATK_time="
				+ ATK_time + ", Ratio=" + Ratio + ", Death=" + Death + ", Vital=" + Vital + ", Skill_text=" + Skill_text
				+ ", Rate=" + Rate + ", take=" + take + ", takeArr=" + takeArr + ", take_value=" + take_value
				+ ", takeValueArr=" + takeValueArr + "]";
	}
	
}
