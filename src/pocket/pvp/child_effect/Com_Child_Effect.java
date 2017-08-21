package pocket.pvp.child_effect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.BaseSkill;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;

public class Com_Child_Effect {
	public static void addChildEffect(JSONObject jsonRoundCE, RoleElves attackRoleElves, RoleElves beAttackRoleElves, SkillDataVo skill1, SkillDataVo skill2){
		
		JSONArray jaSkillList = jsonRoundCE.getJSONArray("skillList");
//		trace("jaSkillList :"+jaSkillList);3
		String[] attackElves = jsonRoundCE.getString("attackElves").split("_");
		int elveId = Integer.parseInt(attackElves[0]);	
		int gender = Integer.parseInt(attackElves[1]);
		int character = Integer.parseInt(attackElves[2]);
		int sameIndex = Integer.parseInt(attackElves[3]);
		
		Elve elve1 = attackRoleElves.getElve();
		Elve elve2 = beAttackRoleElves.getElve();
		
		if(elveId==elve1.geteLvesID() && gender==elve1.getGender() && character==elve1.getCharacter() && sameIndex==elve1.getSameIndex()){
			BaseSkill baseSkill = new BaseSkill(attackRoleElves, beAttackRoleElves, elve1, elve2, skill1, true);
			baseSkill.AddChildEffect(jaSkillList);
		}
		if(elveId==elve2.geteLvesID() && gender==elve2.getGender() && character==elve2.getCharacter() && sameIndex==elve2.getSameIndex()){
			BaseSkill baseSkill = new BaseSkill(beAttackRoleElves, attackRoleElves, elve2, elve1, skill2, true);
			baseSkill.AddChildEffect(jaSkillList);
		}
		
	}
}
