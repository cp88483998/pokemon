package pocket.pvp.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;

public interface JsonService {
	Player parsePlayer(JSONObject jsonObject);
	Map<String, RoleElves> parseElve(JSONArray jaElves);
//	List<Skill> parseSkill(JSONArray jaSkills);
	Map<String, SkillDataVo> parseSkill(List<Integer> list);
}
