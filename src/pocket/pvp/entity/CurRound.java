package pocket.pvp.entity;

import net.sf.json.JSONObject;
import pocket.pvp.skill_species.Species;

/**
 * <p>Title: CurRound<／p>
 * <p>Description: 当前回合数据<／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class CurRound {
	private int resultType = 10;//0 更换精灵 1 使用技能
	private SkillDataVo nowSkill;
	private RoleElves nowRoleElves;
	private RoleElves beChangeRoleElves;
	private int skillTime;//使用技能次数
	private boolean isUseSkill;
	private Species species;
	private JSONObject roundChildEffect;
	private JSONObject weatherInfor;//天气
	private boolean isHit;
	
	
	public JSONObject getWeatherInfor() {
		return weatherInfor;
	}
	public void setWeatherInfor(JSONObject weatherInfor) {
		this.weatherInfor = weatherInfor;
	}
	public boolean isHit() {
		return isHit;
	}
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	public JSONObject getRoundChildEffect() {
		return roundChildEffect;
	}
	public void setRoundChildEffect(JSONObject roundChildEffect) {
		this.roundChildEffect = roundChildEffect;
	}
	public Species getSpecies() {
		return species;
	}
	public void setSpecies(Species species) {
		this.species = species;
	}
	public boolean isUseSkill() {
		return isUseSkill;
	}
	public void setUseSkill(boolean isUseSkill) {
		this.isUseSkill = isUseSkill;
	}
	public int getSkillTime() {
		return skillTime;
	}
	public void setSkillTime(int skillTime) {
		this.skillTime = skillTime;
		if(skillTime == 0){
			setUseSkill(true);
		}
	}
	public RoleElves getBeChangeRoleElves() {
		return beChangeRoleElves;
	}
	public void setBeChangeRoleElves(RoleElves beChangeRoleElves) {
		this.beChangeRoleElves = beChangeRoleElves;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	public SkillDataVo getNowSkill() {
		return nowSkill;
	}
	public void setNowSkill(SkillDataVo nowSkill) {
		this.nowSkill = nowSkill;
	}
	public RoleElves getNowRoleElves() {
		return nowRoleElves;
	}
	public void setNowRoleElves(RoleElves nowRoleElves) {
		this.nowRoleElves = nowRoleElves;
	}
	
}
