package pocket.total.util;

import java.util.List;

import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.RoleElves;

/**
 * 能力值计算
 * <p>Title: Ability<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月18日
 */
public class Ability {
	/*
	 * HP能力值=24×((种族值×特训值+突破值+个体值等级/2+100)×等级/100+10)
	 */
	public static int hpComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
//		System.out.println(elve.toString());
		int HP = roleElves.getElves_value().getHP();//种族值
		double trValue = trainValue_Com(elve.getTrainedLevel());//特训值
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());//突破值
		int hpLv = elve.getHpLevel();//个体值等级
		int lv = elve.getLevel();//等级
		double HP_Ability = 24*((HP*trValue+btValue+hpLv/2+100)*lv/100+10);
//		System.out.println(elve.geteLvesID()+" HP能力值："+HP+"*"+trValue+"+"+btValue+"+"+hpLv+"/2+100)*"+lv+"/100+10"+"="+HP_Ability);
		return (int)Math.round(HP_Ability);//四舍五入
	}
	/*
	 * 非HP能力值=24×((种族值×特训值+突破值+个体值等级/2)×等级/100+5)×性格系数×公会等级
	 */
	//物攻能力值
	public int ATKComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
		int ATK = roleElves.getElves_value().getATK();
		double trValue = trainValue_Com(elve.getTrainedLevel());
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());
		int atkLv = elve.getWuAttackLevel();
		int lv = elve.getLevel();
		if(roleElves.getChildEffectList() != null){
			List<Child_Effect> list = roleElves.getChildEffectList();
			Child_Effect child_Effect;
			for(int i=0;i<list.size();i++){
				child_Effect = list.get(i);
				if(child_Effect.getId()==103){//物理攻击%+-
					if(child_Effect.GetWuAttackPer() < 0){
						ATK *= (0 - child_Effect.GetWuAttackPer());
					}else{
						ATK *= child_Effect.GetWuAttackPer();
					}
				}
				if(child_Effect.getId()==112){//物攻等级+-
					atkLv += child_Effect.GetWuAttackLevel();
				}
			}
		}
//		System.out.println("character:"+elve.getCharacter());
		//性格系数
		double ATKCha = ATKChaCom(elve.getCharacter());
		//物攻能力值
		double wuAttack_Ability = 24*((ATK*trValue+btValue+atkLv/2)*lv/100+5)*ATKCha;
		System.out.println(elve.geteLvesID()+"物攻能力值："+ATK+"*"+trValue+"+"+btValue+"+"+atkLv+"/2)*"+lv+"/100+5)*"+ATKCha+"="+wuAttack_Ability);
		return (int)Math.round(wuAttack_Ability);
	}
	//物防能力值
	public int DEFComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
		int DEF = roleElves.getElves_value().getDEF();
		double trValue = trainValue_Com(elve.getTrainedLevel());
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());
		int wdLv = elve.getWuDefenseLevel();
		int lv = elve.getLevel();
		if(roleElves.getChildEffectList() != null){
			List<Child_Effect> list = roleElves.getChildEffectList();
			Child_Effect child_Effect;
			for(int i=0;i<list.size();i++){
				child_Effect = list.get(i);
				if(child_Effect.getId()==104){//物理防御%+-
					if(child_Effect.GetWuDefensePer() < 0 ){
						DEF *= (0 - child_Effect.GetWuDefensePer());
					}else{
						DEF *= child_Effect.GetWuDefensePer();
					}
				}
				if(child_Effect.getId()==114){//物防等级+-
					wdLv += child_Effect.GetWuDefenseLevel();
				}
			}
		}
		double DEFCha = DEFChaCom(elve.getCharacter());
		double wuDefens_Ability = 24*((DEF*trValue+btValue+wdLv/2)*lv/100+5)*DEFCha;
//		System.out.println(elve.geteLvesID()+"物防能力值："+DEF+"*"+trValue+"+"+btValue+"+"+wdLv+"/2)*"+lv+"/100+5)*"+DEFCha+"="+wuDefens_Ability);
		return (int)Math.round(wuDefens_Ability);
	}
	//特攻能力值
	public int S_ATKComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
		int S_ATK = roleElves.getElves_value().getINT();
		double trValue = trainValue_Com(elve.getTrainedLevel());
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());
		int taLv = elve.getTeAttackLevel();
		int lv = elve.getLevel();
		if(roleElves.getChildEffectList() != null){
			List<Child_Effect> list = roleElves.getChildEffectList();
			Child_Effect child_Effect;
			for(int i=0;i<list.size();i++){
				child_Effect = list.get(i);
				if(child_Effect.getId()==105){//特殊攻击%+-
					if(child_Effect.GetTeAttackPer() < 0 ){
						S_ATK *= (0 - child_Effect.GetTeAttackPer());
					}else{
						S_ATK *= child_Effect.GetTeAttackPer();
					}
				}
				if(child_Effect.getId()==113){//特攻等级+-
					taLv += child_Effect.GetTeAttackLevel();
				}
			}
		}
		double INTCha = INTChaCom(elve.getCharacter());
		double teAttack_Ability = 24*((S_ATK*trValue+btValue+taLv/2)*lv/100+5)*INTCha;
//		System.out.println(elve.geteLvesID()+"特攻能力值："+S_ATK+"*"+trValue+"+"+btValue+"+"+taLv+"/2)*"+lv+"/100+5)*"+INTCha+"="+teAttack_Ability);
		return (int)Math.round(teAttack_Ability);
	}
	//特防能力值
	public int S_DEFComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
		int S_DEF = roleElves.getElves_value().getMDF();
		double trValue = trainValue_Com(elve.getTrainedLevel());
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());
		int tdLv = elve.getTeDefenseLevel();
		int lv = elve.getLevel();
		if(roleElves.getChildEffectList() != null){
			List<Child_Effect> list = roleElves.getChildEffectList();
			Child_Effect child_Effect;
			for(int i=0;i<list.size();i++){
				child_Effect = list.get(i);
				if(child_Effect.getId()==106){//特殊防御%+-
					if(child_Effect.GetTeDefensePer() < 0 ){
						S_DEF *= (0 - child_Effect.GetTeDefensePer());
					}else{
						S_DEF *= child_Effect.GetTeDefensePer();
					}
				}
				if(child_Effect.getId()==115){//特防等级+-
					tdLv += child_Effect.GetTeDefenseLevel();
				}
			}
		}
		double MDFCha = MDFChaCom(elve.getCharacter());
		double teDefense_Ability = 24*((S_DEF*trValue+btValue+tdLv/2)*lv/100+5)*MDFCha;
//		System.out.println(elve.geteLvesID()+"特攻能力值："+S_DEF+"*"+trValue+"+"+btValue+"+"+tdLv+"/2)*"+lv+"/100+5)*"+MDFCha+"="+teDefense_Ability);
		return (int)Math.round(teDefense_Ability);
	}
	//速度能力值
	public static int speedComp(RoleElves roleElves){
		Elve elve = roleElves.getElve();
		int speed = roleElves.getElves_value().getACT();
		double trValue = trainValue_Com(elve.getTrainedLevel());
		double btValue = btVlaue_Com(elve.getBreakthroughLevel());
		int spLv = elve.getSpeedLevel();
		int lv = elve.getLevel();
		if(roleElves.getChildEffectList() != null){
			List<Child_Effect> list = roleElves.getChildEffectList();
			Child_Effect child_Effect;
			for(int i=0;i<list.size();i++){
				child_Effect = list.get(i);
				if(child_Effect.getId()==107){//速度%+-
					if(child_Effect.GetSpeedPer() < 0 ){
						speed *= (0 - child_Effect.GetSpeedPer());
					}else{
						speed *= child_Effect.GetSpeedPer();
					}
				}
				if(child_Effect.getId()==116){//速度等级+-
					spLv += child_Effect.GetSpeedLevel();
				}
			}
		}
		double ACTCha = ACTChaCom(elve.getCharacter());
		double speed_Ability = 24*((speed*trValue+btValue+spLv/2)*lv/100+5)*ACTCha;
//		System.out.println("速度能力值："+speed+"*"+trValue+"+"+btValue+"+"+spLv+"/2)*"+lv+"/100+5)*"+ACTCha+"="+speed_Ability);
		return (int)Math.round(speed_Ability);
	}
	
	/*
	 * 性格系数计算
	 */
	//物攻系数
	private double ATKChaCom(int id){
		for(int i=0;i<StaticClass.character_str_js.size();i++){
			JSONObject jsonObject = StaticClass.character_str_js.getJSONObject(i);
			if(jsonObject.getInt("ID") == id){
				return jsonObject.getDouble("ATK");
			}
		}
		return 1;
	}
	//物防系数
	private double DEFChaCom(int id){
		for(int i=0;i<StaticClass.character_str_js.size();i++){
			JSONObject jsonObject = StaticClass.character_str_js.getJSONObject(i);
			if(jsonObject.getInt("ID") == id){
				return jsonObject.getDouble("DEF");
			}
		}
		return 1;
	}
	//特攻系数
	private double INTChaCom(int id){
		for(int i=0;i<StaticClass.character_str_js.size();i++){
			JSONObject jsonObject = StaticClass.character_str_js.getJSONObject(i);
			if(jsonObject.getInt("ID") == id){
				return jsonObject.getDouble("INT");
			}
		}
		return 1;
	}
	//特防系数
	private double MDFChaCom(int id){
		for(int i=0;i<StaticClass.character_str_js.size();i++){
			JSONObject jsonObject = StaticClass.character_str_js.getJSONObject(i);
			if(jsonObject.getInt("ID") == id){
				return jsonObject.getDouble("MDF");
			}
		}
		return 1;
	}
	//速度系数
	private static double ACTChaCom(int id){
		for(int i=0;i<StaticClass.character_str_js.size();i++){
			JSONObject jsonObject = StaticClass.character_str_js.getJSONObject(i);
			if(jsonObject.getInt("ID") == id){
				return jsonObject.getDouble("ACT");
			}
		}
		return 1;
	}
	//特训值计算
	public static double trainValue_Com(int lv){
		double trainValue = 1;
		switch (lv) {
		case 2:
			trainValue = 1.24;
			break;
		case 3:
			trainValue = 1.44;
			break;
		case 4:
			trainValue = 1.7;
			break;
		case 5:
			trainValue = 2;
			break;
		default:
			trainValue = 1;
		}
		return trainValue;
	}
	//突破值计算
	public static double btVlaue_Com(int lv){
		double btVlaue = 0;
		switch (lv) {
		case 2:
			btVlaue = 4.9;
			break;
		case 3:
			btVlaue = 9.8;
			break;
		case 4:
			btVlaue = 14.7;
			break;
		case 5:
			btVlaue = 19.6;
			break;
		case 6:
			btVlaue = 24.5;
			break;
		case 7:
			btVlaue = 29.4;
			break;
		case 8:
			btVlaue = 34.3;
			break;
		case 9:
			btVlaue = 39.2;
			break;
		case 10:
			btVlaue = 44.1;
			break;
		case 11:
			btVlaue = 49;
			break;
		case 12:
			btVlaue = 53.9;
			break;
		case 13:
			btVlaue = 58.8;
			break;
		case 14:
			btVlaue = 63.7;
			break;
		default:
			btVlaue = 0;
		}
		return btVlaue;
	}
}
