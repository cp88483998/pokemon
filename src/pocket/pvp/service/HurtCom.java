package pocket.pvp.service;

import java.util.Random;

import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.child_effect.Child_Effect_Factory;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.total.util.Ability;
import pocket.total.util.PGMultiple;
import pocket.total.util.SkillConstValue;
import pocket.total.util.StaticClass;
/**
 * 伤害计算类
 * <p>Title: HurtCom<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月18日
 */
public class HurtCom {
	/*
	 * 伤害计算 = 
	 * [(攻击方的LV×0.4＋2)×技巧威力×（本系加成+道具倍数）×攻击方的攻击（或特攻）能力值÷防御方的防御（或特防）能力值÷50＋2]×相克倍数×25*（0.92~1.08）
	 */
	public int hurt(RoleElves roleElves1, RoleElves roleElves2, SkillDataVo skill1){
		Elve elve1 = roleElves1.getElve();
		Elve elve2 = roleElves2.getElve();
		//攻击方lv
		int elveLv = elve1.getLevel();
		//技巧威力
		int power = skill1.getSkill_power();
		//本系加成：当技能属性与施法精灵属性一致时，*1.5
		double depAdd = 0;
		String skill_ability1 = skill1.getSkill_ability();//技能属性
		String elves_type1 = elve1.getElves_type();//施法精灵属性
//		System.out.println(elves_type1);
		if(elves_type1 != null){
			String[] types1 = elves_type1.split(",");
			for(int i=0;i<types1.length;i++){		
//				System.out.println(types[i]);
				if(skill_ability1.equals(types1[i])){
					depAdd = 1.5;
				}
			}
		}
		//道具倍数：根据道具的效果，可以提升对应的消耗
		//技能属性需要跟道具属性一致
		double item = 0;
		String skill_ability = skill1.getSkill_ability();
		Child_Effect child_Effect;
		Child_Effect_Factory factory = new Child_Effect_Factory();
		String equip = elve1.getCarryequip();
		JSONObject jsonObject;
		if(equip != null){
			int carryequip = Integer.parseInt(equip);
			for(int i=0; i<StaticClass.prop_effect_js.size(); i++){
				jsonObject = StaticClass.prop_effect_js.getJSONObject(i);
				if(carryequip == jsonObject.getInt("item_effect")){
					//若技能属性跟道具属性一致
					if(SkillConstValue.AttributeList.indexOf(skill_ability) == jsonObject.getInt("value")){
						String[] effect_str = jsonObject.getString("effect").split(",");
						String[] value2_str = jsonObject.getString("value2").split(",");
						for(int j=0; j<effect_str.length; j++){
							child_Effect = factory.create(effect_str[j], 0, value2_str[j]);
							if(child_Effect.GetPowerPer() > 0){
								item = 1 + child_Effect.GetPowerPer();
							}else{
								item = 0 - child_Effect.GetPowerPer();
							}
						}
					}
				}
			}
		}
		//判断伤害值类型
		String type1 = skill1.getSkill_type();
		Ability ability = new Ability();
		//计算攻击方能力值
		double AtkAbility = 1;
		//计算防守方能力值
		double DefAbility = 1;
		if(type1.equals("物理")){
			AtkAbility = ability.ATKComp(roleElves1);
			DefAbility = ability.DEFComp(roleElves2);
		}
		if(type1.equals("特殊")){
			AtkAbility = ability.S_ATKComp(roleElves1);
			DefAbility = ability.S_DEFComp(roleElves2);
		}
		
		//相克倍数
		double pgm = pgm(skill_ability1, elve2);
		
		//随机数
		Random random = new Random();
		double ran = (random.nextInt(17)+92)/100;
		//伤害计算
		// [(攻击方的LV×0.4＋2)×技巧威力×（本系加成+道具倍数）×攻击方的攻击（或特攻）能力值÷防御方的防御（或特防）能力值÷50＋2]×相克倍数×25*（0.92~1.08）
		double hurt = ((elveLv*0.4+2)*power*(depAdd+item)*AtkAbility/DefAbility/50+2)*pgm*25*ran;
		System.out.println("elveLv:"+elveLv+",power:"+power+",depAdd:"+depAdd+",item:"+item+",AtkAbility:"+AtkAbility+",DefAbility:"+DefAbility+",pgm:"+pgm+",ran:"+ran);
		
		return (int)Math.round(hurt);//四舍五入
	}
	
	/* 
	 * 相克倍数计算
	 */
	public double pgm(String skill_ability1, Elve elve2){
		
		PGMultiple pgMultiple = new PGMultiple();
		String elves_type2 = elve2.getElves_type();
		double a = 0;
		double b = 0;
		double pgm = 1;
		if(elves_type2 != null){
			String[] types2 = elves_type2.split(",");
			if(types2.length == 1){
				pgm = pgMultiple.mul_com(skill_ability1, types2[0]);
			}
			if(types2.length == 2){
				a = pgMultiple.mul_com(skill_ability1, types2[0]);
				b = pgMultiple.mul_com(skill_ability1, types2[1]);
				if(a == b){
					pgm = a;
				}else{
					pgm = a * b;
				}
			}
		}
		return pgm;
	}
	/*
	 * 判定要害攻击
	 */
	public boolean isVital(SkillDataVo skill1){
		int c = 0;
		for(int i=0;i<StaticClass.skill_data_js.size();i++){
			JSONObject jsonObject2 = StaticClass.skill_data_js.getJSONObject(i);
			if(jsonObject2.getInt("Skill_number") == skill1.getSkill_id()){
				//要害值
				c = jsonObject2.getInt("vital");
			}
		}
		if(c > 4){
			c = 4;
		}
		Random random = new Random();
		double ran = random.nextDouble();
		//是否是要害攻击
		boolean isVital = false;
		
		switch (c) {
		case 0:
			isVital = ran < 0.0625;
		case 1:
			isVital = ran < 0.125;
		case 2:
			isVital = ran < 0.25;
		case 3:
			isVital = ran < 0.333;
		case 4:
			isVital = ran < 0.5;
		default:
			break;
		}
		return isVital;
	}
}
