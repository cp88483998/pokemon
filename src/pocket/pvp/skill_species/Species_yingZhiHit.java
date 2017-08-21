package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.SkillDataVo;

/**
 * 硬直
 * @author 陈鹏
 */
public class Species_yingZhiHit extends Species{

	public Species_yingZhiHit(int _skill_id, String _skill_name, String _species) {
		super(_skill_id, _skill_name, _species);
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		joUseSkill.put("curRoundCount", super.getCurRoundCount());
		jo.put("BaseSkill", joUseSkill);
		
		super.setRoundCount(2);
		return jo;
	}

}
