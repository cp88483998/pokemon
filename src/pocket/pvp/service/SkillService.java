package pocket.pvp.service;

import java.util.Map;

import net.sf.json.JSONObject;
import pocket.pvp.entity.CurRound;
import pocket.pvp.entity.Player;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.skill_species.Species;
import pocket.pvp.skill_species.SpeciesType;
import pocket.pvp.skill_species.Species_Factory;

/**
 * 请求使用技能处理类调用的方法
 * <p>Title: SkillService<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class SkillService {
	/**
	 * 返回给客户端的技能数据
	 * @param player1
	 * @param player2
	 * @param skillId
	 * @return 需要返回给客户端的技能数据格式(见md文件)
	 */
	public static JSONObject getBaseSkill(Player player1, Player player2, SkillDataVo skill1){
		/*
		 * 判断技能类型
		 */
		CurRound curRound1 = player1.getCurRound();
		RoleElves attackRoleElves = curRound1.getNowRoleElves();
		
		int resultType1 = 1;
		curRound1.setResultType(resultType1);
		curRound1.setNowSkill(skill1);
		
		RoleElves beAttackRoleElves = player2.getCurRound().getNowRoleElves();
		
		String speciesName = skill1.getSpecies();
		Species species = Species_Factory.getInstance().create(skill1.getSkill_id(), skill1.getSkill_name(), speciesName);
//		boolean isBiSha = false;
//		int hitCount = 1;//攻击次数
//		int roundCount = 1;//持续回合数
		JSONObject jo = new JSONObject();
		if(speciesName.equals(SpeciesType.Diehit)){
			species.hitDie(attackRoleElves.getElve(), beAttackRoleElves.getElve());
		}
		if(speciesName.equals(SpeciesType.MultipleHit)){
			species.count(skill1);
		}
		if(speciesName.equals(SpeciesType.MultipleRoundHit)){
			species.roundsCount();
		}
		if(speciesName.equals(SpeciesType.PowerHit)){
			
		}
		if(speciesName.equals(SpeciesType.YingZhiHit)){
			
		}
		if(speciesName.equals(SpeciesType.PowerBackHit)){

		}
		
		
		//其余都属于"普通技能"
		jo = species.getUseSkill(skill1);
		
		
		//设置当前回合的技能类型
		curRound1.setSpecies(species);
		return jo;
	}
	/**
	 * 另一种方法返回给客户端的技能数据
	 * @param curRound1
	 * @param curRound2
	 * @param skillId
	 * @return
	 */
	public static JSONObject getBaseSkill2(CurRound curRound1, CurRound curRound2, int skillId){
		/*
		 * 判断技能类型
		 */
		RoleElves attackRoleElves = curRound1.getBeChangeRoleElves();
		int resultType1 = 1;
		curRound1.setResultType(resultType1);
		SkillDataVo skill1 = attackRoleElves.getStudySkills().get(String.valueOf(skillId));
		curRound1.setNowSkill(skill1);
		
		RoleElves beAttackRoleElves = curRound2.getNowRoleElves();
		
		String speciesName = skill1.getSpecies();
		Species species = Species_Factory.getInstance().create(skill1.getSkill_id(), skill1.getSkill_name(), speciesName);
//		boolean isBiSha = false;
//		int hitCount = 1;//攻击次数
//		int roundCount = 1;//持续回合数
		JSONObject jo = new JSONObject();
		if(speciesName.equals(SpeciesType.Diehit)){
			species.hitDie(attackRoleElves.getElve(), beAttackRoleElves.getElve());
		}
		if(speciesName.equals(SpeciesType.MultipleHit)){
			species.count(skill1);
		}
		if(speciesName.equals(SpeciesType.MultipleRoundHit)){
			species.roundsCount();
		}
		if(speciesName.equals(SpeciesType.PowerHit)){
			
		}
		if(speciesName.equals(SpeciesType.YingZhiHit)){
			
		}
		if(speciesName.equals(SpeciesType.PowerBackHit)){

		}
		
		//其余都属于"普通技能"
		jo = species.getUseSkill(skill1);
		
		
		//设置当前回合的技能类型
		curRound1.setSpecies(species);
		return jo;
	}
	/**
	 * 自动选择技能（断线时用到）
	 * @param player1
	 * @param player2
	 * @return 技能数据
	 */
	public static JSONObject autoChooseSkill(Player player1, Player player2){
		player1.getCurRound().setResultType(1);
		//若没死，直接自动选择技能
		Map<String, SkillDataVo> skillMap = player1.getCurRound().getNowRoleElves().getStudySkills();
		
		SkillDataVo autoSkill = null;
		
		//随机一个有pp值的伤害性技能
		for(String key : skillMap.keySet()){
			if(judgeSkillAbility(skillMap.get(key), player2.getCurRound().getNowRoleElves())){
				continue;
			}
			if(skillMap.get(key).getPP() > 0 && skillMap.get(key).getSkill_power() > 0){
				autoSkill = skillMap.get(key);
				break;
			}
		}
		if(autoSkill == null){
			for(String key : skillMap.keySet()){
				if(skillMap.get(key).getPP() > 0){
					autoSkill = skillMap.get(key);
					break;
				}
			}
		}
		
		player1.getCurRound().setNowSkill(autoSkill);
		player1.getCurRound().setResultType(1);
		JSONObject jo = SkillService.getBaseSkill(player1, player2, autoSkill);
		return jo;
	}
	/**
	 * 自动选择精灵（断线时用到）
	 * @param player1
	 * @param player2
	 * @return 
	 */
	public static void autoChooseRoleElves(Player player){
		RoleElves roleElves = null;
		CurRound curRound2 = player.getCurRound();
		for(String key : player.getRoleElves().keySet()){
			if(!player.getRoleElves().get(key).getElve().getIsDie() 
					&& curRound2.getNowRoleElves().getElve().geteLvesID()!=player.getRoleElves().get(key).getElve().geteLvesID()){
				roleElves = player.getRoleElves().get(key);
				break;
			}
		}
		
		curRound2.setBeChangeRoleElves(curRound2.getNowRoleElves());
		curRound2.setNowRoleElves(roleElves);
	}
	
	/**
	 * 属性克制关系表,判断是否技能无效
	 * @return
	 */
	public static boolean judgeSkillAbility(SkillDataVo skillDataVo, RoleElves roleElves){
		String skill_ability = skillDataVo.getSkill_ability();
		String elves_type = roleElves.getElve().getElves_type();
		String[] elves_types = elves_type.split(",");
		if(elves_types.length > 0){
			String invalid = invalidSkillAbility(skill_ability);
			if(invalid != null){
				for(int i=0;i<elves_types.length;i++){
					if(invalid.equals(elves_types[i])){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 无效属性
	 * @param skill_ability
	 * @return
	 */
	public static String invalidSkillAbility(String skill_ability){
		switch (skill_ability) {
		case "格斗":
			return "鬼";
		case "毒":
			return "鬼";
		case "地":
			return "钢";
		case "飞行":
			return "飞行";
		case "虫":
			return null;
		case "岩":
			return null;	
		case "鬼":
			return "普通";
		case "钢":
			return null;
		case "火":
			return null;
		case "木":
			return null;
		case "电":
			return "地";
		case "草":
			return null;
		case "冰":
			return null;
		case "超能":
			return "恶";
		case "龙":
			return "妖精";
		case "恶":
			return null;
		case "妖精":
			return null;
		default:
			return "鬼";
		}
	}
}
