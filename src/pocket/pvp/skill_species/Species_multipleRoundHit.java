package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.SkillDataVo;
import scala.util.Random;
/**
 * 多回合技能
 * @author 陈鹏
 */
public class Species_multipleRoundHit extends Species{

	public Species_multipleRoundHit(int _skill_id, String _skill_name, String _species) {
		super(_skill_id, _skill_name, _species);
	}

	@Override
	public int roundsCount() {
		Random ran = new Random();
		int roundCount = ran.nextInt(2)+2;
		super.setRoundCount(roundCount);
		return roundCount;
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		joUseSkill.put("curRoundCount", super.getCurRoundCount());
		joUseSkill.put("roundCount", super.getRoundCount());
		jo.put("BaseSkill", joUseSkill);
		return jo;
	}
	
	
}
