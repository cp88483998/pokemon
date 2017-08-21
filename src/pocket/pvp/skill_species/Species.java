package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.SkillDataVo;

public class Species {
	private int skill_id;//技能ID
	private String skill_name;//技能名
	private String speciesType;//技能类型
	
	
	private boolean isBiSha;//是否为必杀技能
	private int hitCount;//一回合多次击杀
	private int roundCount;//持续多回合
	private int curRoundCount = 1;//当前技能回合数
	
	public Species(int skill_id, String skill_name, String speciesType) {
		this.skill_id = skill_id;
		this.skill_name = skill_name;
		this.speciesType = speciesType;
	}
	
	//是否为必杀技能
	public boolean hitDie(Elve _attackElvesDataInforVo, Elve _beAttackElvesDataInforVo){
		return false;
	}
	//一回合多次击杀
	public int count(SkillDataVo _battleSkillData) {
		return 1;
	}
	//持续多回合
	public int roundsCount() {
		return 1;
	}
	//蓄气（第一回合不出手，第二回合出手）
	//硬直（第一回合出手，第二回合不出手）
	
	//BaseSkill
	public JSONObject getUseSkill(SkillDataVo skill){
		return null;
	}
	
	public boolean isBiSha() {
		return isBiSha;
	}

	public void setBiSha(boolean isBiSha) {
		this.isBiSha = isBiSha;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

	public int MultipleRoundHitSkill () {
		return curRoundCount;
	}

	public void setCurRoundCount(int curRoundCount) {
		this.curRoundCount = curRoundCount;
	}

	public int getCurRoundCount() {
		return curRoundCount;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public int getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}

	public String getSkill_name() {
		return skill_name;
	}

	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}
	public String getSpeciesType() {
		return speciesType;
	}
	public void setSpeciesType(String speciesType) {
		this.speciesType = speciesType;
	}

	@Override
	public String toString() {
		return "Species [skill_id=" + skill_id + ", skill_name=" + skill_name + ", speciesType=" + speciesType
				+ ", isBiSha=" + isBiSha + ", hitCount=" + hitCount + ", curRoundCount=" + curRoundCount
				+ ", roundCount=" + roundCount + "]";
	}

}
