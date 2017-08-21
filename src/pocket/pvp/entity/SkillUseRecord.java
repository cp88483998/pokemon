package pocket.pvp.entity;

public class SkillUseRecord {
	private boolean isHit;
	private boolean isEffect;
	private SkillDataVo skillDataVo;
	
	
	public SkillUseRecord(boolean isHit, boolean isEffect, SkillDataVo skillDataVo) {
		this.isHit = isHit;
		this.isEffect = isEffect;
		this.skillDataVo = skillDataVo;
	}
	public SkillUseRecord() {

	}
	public boolean isHit() {
		return isHit;
	}
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	public boolean isEffect() {
		return isEffect;
	}
	public void setEffect(boolean isEffect) {
		this.isEffect = isEffect;
	}
	public SkillDataVo getSkillDataVo() {
		return skillDataVo;
	}
	public void setSkillDataVo(SkillDataVo skillDataVo) {
		this.skillDataVo = skillDataVo;
	}
}
