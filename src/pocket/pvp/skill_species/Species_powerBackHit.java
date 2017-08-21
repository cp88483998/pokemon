package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.SkillDataVo;

public class Species_powerBackHit extends Species{

	public Species_powerBackHit(int skill_id, String skill_name, String speciesType) {
		super(skill_id, skill_name, speciesType);
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		joUseSkill.put("curRoundCount", super.getCurRoundCount());
		joUseSkill.put("roundCount", 3);
		jo.put("BaseSkill", joUseSkill);
		
		super.setRoundCount(3);
		return jo;
	}

	
}
