package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.SkillDataVo;

public class Species_normal extends Species{

	public Species_normal(int skill_id, String skill_name, String species) {
		super(skill_id, skill_name, species);
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		jo.put("BaseSkill", joUseSkill);
		return jo;
	}
	
}
