package pocket.pvp.service;

import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.total.util.Ability;
import pocket.total.util.StaticClass;
public class Judge {
	/*
	 * 判断先手
	 */
	public static Elve judgeFirst(RoleElves roleElves1, RoleElves roleElves2, SkillDataVo skill1, SkillDataVo skill2){
		int carryequip1 = 0;
		int carryequip2 = 0;
		Elve elve1 = roleElves1.getElve();
		Elve elve2 = roleElves2.getElve();
		if(elve1.getCarryequip() != null){
			carryequip1= Integer.parseInt(elve1.getCarryequip());
		}
		if(elve2.getCarryequip() != null){
			carryequip2= Integer.parseInt(elve2.getCarryequip());
		}
	    int effect1 = 0;
	    int effect2 = 0;
	    String[] effect_str;
		for(int i=0;i<StaticClass.prop_effect_js.size();i++){
			JSONObject jsonObject = StaticClass.prop_effect_js.getJSONObject(i);
			if(jsonObject.getInt("item_effect") == carryequip1){
				effect_str = jsonObject.getString("effect").split(",");
				for(int j=0;j<effect_str.length;j++){
					if(Integer.parseInt(effect_str[j]) == 164){//优先出手
						effect1 = 164;
						break;
					}
				}
			}
			if(jsonObject.getInt("item_effect") == carryequip2){
				effect_str = jsonObject.getString("effect").split(",");
				for(int j=0;j<effect_str.length;j++){
					if(Integer.parseInt(effect_str[j]) == 164){//优先出手
						effect2 = 164;
						break;
					}
				}
			}
		}
//		if(skill1.getSkill_id() == 10097){
//			//每使用一次高速移动，速度等级加2级，最多加6级
//			if(elve1.getAddSpeedLv() < 6){
//				elve1.setAddSpeedLv(elve1.getAddSpeedLv()+2);
//				elve1.setSpeedLevel(elve1.getSpeedLevel()+2);
//			}
//		}
//		if(skill2.getSkill_id() == 10097){
//			if(elve2.getAddSpeedLv() < 6){
//				elve2.setAddSpeedLv(elve2.getAddSpeedLv()+2);
//				elve2.setSpeedLevel(elve2.getSpeedLevel()+2);
//			}
//		}
		int speedAb1 = Ability.speedComp(roleElves1);
		int speedAb2 = Ability.speedComp(roleElves2);
		
    	if(skill1.getPriority() > skill2.getPriority()){
    		//技能1优先值高
    		return elve1;
    	}else if(skill1.getPriority() < skill2.getPriority()){
    		//技能2优先值高
    		return elve2;
    	}else if(effect1 == 164 && effect2 != 164){
    		//精灵1携带先手装备
    		return elve1;
    	}else if(effect1 != 164 && effect2 == 164){
    		//精灵2携带先手装备
    		return elve2;
    	}else if(speedAb1 > speedAb2){
    		//精灵1速度快
    		return elve1;
    	}
		return elve2;
	}
	
	
	/*
	 * 判断命中
	 */
	public static boolean judgeHit(Elve elve1, Elve elve2, SkillDataVo skill1){
		int a = 50;
		Random random = new Random();
		int b = random.nextInt(100);
		if(a > b){
			return true;
		}
		return true;
	}
	public static boolean judgeHit1(RoleElves roleElves1, RoleElves roleElves2, SkillDataVo skill1){
		//命中率
		int hit = 1;
		if(skill1.getHit()!=0){
			hit = skill1.getHit();
		}
		
		//等级修正
		Elve elve1 = roleElves1.getElve();
		Elve elve2 = roleElves2.getElve();
		int hit_lv = elve1.getHit_level();
		int dod_lv = elve2.getDodge_level();
		List<Child_Effect> list1 = roleElves1.getChildEffectList();
		Child_Effect child_Effect1;
		for(int i=0;i<list1.size();i++){
			child_Effect1 = list1.get(i);
			if(child_Effect1.getId()==108){//命中率
				hit *= (1 + child_Effect1.GetHitPer());
			}
			if(child_Effect1.getId()==117){//命中等级
				hit_lv += child_Effect1.GetHitLevel();
			}
		}
		List<Child_Effect> list2 = roleElves2.getChildEffectList();
		Child_Effect child_Effect2;
		for(int i=0;i<list2.size();i++){
			child_Effect2 = list2.get(i);
			if(child_Effect2.getId()==111){//回避等级
				dod_lv += child_Effect2.GetDodgeLevel();
				break;
			}
		}
		double levelPer = getLevelPer(hit_lv - dod_lv);
		
		
		//特性修正
		//获取elve1的命中率
		double cha1 = 1;
		String features_id1 = elve1.getFeatures();
		String[] effect_str;
		String[] effect_value_str;
		int addHitLv = 0;
		if(!features_id1.equals("")){
			for(int i=0;i<StaticClass.features_effect_js.size();i++){
				JSONObject jsonObject = StaticClass.features_effect_js.getJSONObject(i);
				if(jsonObject.getInt("features_id") == Integer.parseInt(features_id1)){
					effect_str = jsonObject.getString("effect").split(",");
					effect_value_str = jsonObject.getString("effect_value").split(",");
					for(int j=0;j<effect_str.length;j++){
						if(Integer.parseInt(effect_str[j]) == 108){//命中%
							int value = Integer.parseInt(effect_value_str[j]);
							if(value > 0){
								cha1 = (100 + value) / 100;
							}else{
								cha1 = (0 - value) / 100;
							}
						}
						if(Integer.parseInt(effect_str[j]) == 117){//命中等级
							int value = Integer.parseInt(effect_value_str[j]);
							addHitLv += value;
						}
					}
					break;
				}
			}
		}
		//获取elve2的闪避率
		int addDodLv = 0;
		String features_id2 = elve1.getFeatures();
		if(!features_id2.equals("")){
			for(int i=0;i<StaticClass.features_effect_js.size();i++){
				JSONObject jsonObject = StaticClass.features_effect_js.getJSONObject(i);
				if(jsonObject.getInt("features_id") == Integer.parseInt(features_id2)){
					effect_str = jsonObject.getString("effect").split(",");
					effect_value_str = jsonObject.getString("effect_value").split(",");
					for(int j=0;j<effect_str.length;j++){
						if(Integer.parseInt(effect_str[j]) == 111){//闪避等级
							int value = Integer.parseInt(effect_value_str[j]);
							addDodLv += value;
							break;
						}
					}
					break;
				}
			}
		}
		double a = 1;
		double b = 1;
		if(addHitLv != 0){
			a = getLevelPer(addHitLv);
		}
		if(addDodLv != 0){
			b = getLevelPer(addDodLv);
		}
		double cha_cor = cha1 * a * b;
		
		
		//道具修正
//		double prop1 = 1;
//		String item_effect1 = elve1.getCarryequip();
//		if(!item_effect1.equals("")){
//			for(int i=0;i<StaticClass.prop_effect_js.size();i++){
//				JSONObject jsonObject = StaticClass.prop_effect_js.getJSONObject(i);
//				if(jsonObject.getInt("item_effect") == Integer.parseInt(item_effect1)){
//					prop1 = jsonObject.getInt("item_effect");
//				}
//			}
//		}
		double prop2 = 1;
		int addDodLv2 = 0;
		String item_effect2 = elve1.getCarryequip();
		if(!item_effect2.equals("")){
			for(int i=0;i<StaticClass.prop_effect_js.size();i++){
				JSONObject jsonObject = StaticClass.prop_effect_js.getJSONObject(i);
				if(jsonObject.getInt("item_effect") == Integer.parseInt(item_effect2)){
					effect_str = jsonObject.getString("effect").split(",");
					effect_value_str = jsonObject.getString("value2").split(",");
					for(int j=0;j<effect_str.length;j++){
						if(Integer.parseInt(effect_str[j]) == 111){//闪避等级
							int value = Integer.parseInt(effect_value_str[j]);
							addDodLv2 += value;
							break;
						}
					}
					break;
				}
			}
		}
		if(addDodLv != 0){
			prop2 = getLevelPer(addDodLv2);
		}
		double prop_cor = prop2;
		
		double A = hit * levelPer * cha_cor * prop_cor;
		
		Random random = new Random();
		int i = random.nextInt(100)+1;
		
		if(A > i){
			return true;
		}
		return false;
	}
	
	/*
	 * 修正概率
	 */
	public static double getLevelPer(int i){
		if(i < -6){
			return 0.33;
		}
		if(i == -6){
			return 0.33;
		}
		if(i == -5){
			return 0.38;
		}
		if(i == -4){
			return 0.43;	
		}
		if(i == -3){
			return 0.5;		
		}
		if(i == -2){
			return 0.6;
		}
		if(i == -1){
			return 0.75;
		}
		if(i == 0){
			return 1.0;
		}
		if(i == 1){
			return 1.33;
		}
		if(i == 2){
			return 1.67;
		}
		if(i == 3){
			return 2.0;
		}
		if(i == 4){
			return 2.33;
		}
		if(i == 5){
			return 2.67;
		}
		if(i == 6){
			return 3.0;
		}
		return 1.0;
	}
}
