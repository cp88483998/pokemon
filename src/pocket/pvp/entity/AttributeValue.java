package pocket.pvp.entity;

import java.util.List;

import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.service.Judge;

public class AttributeValue {
	//绑定的角色
	private RoleElves roleElves;
	
	
	private Elve elvesDataInforVo;
	
	public AttributeValue(RoleElves _roleElves){
        if (_roleElves==null){
            System.out.println("AttributeValue数据有误");
            return;
        }
        roleElves = _roleElves;
        elvesDataInforVo = roleElves.getElve();;
    }
	
	//获取威力值
	public double powerValue(){
        Child_Effect ce = roleElves.GetChildEffectById(182);
        if (ce == null){
            return Double.MAX_VALUE;
        }else{
//            BattleCalculate.curBattleData.AddAttackEffect("182," + ce.GetPowerValue());
            return ce.GetPowerValue();
        }
    }
	
	//获取物攻系数
	private double wuAttackPer;

	public double getWuAttackPer() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        double tmpPer = 1;
        int[] arr = {103, 112};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetWuAttackLevel();
        	}
            tmpPer *= child_Effect.GetWuAttackPer();
        }
        double per = Judge.getLevelPer(level);
        wuAttackPer = per * tmpPer;
		return wuAttackPer;
	}

	//获取物防系数
	private double wuDefensePer;

	public double getWuDefensePer() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        double tmpPer = 1;
        int[] arr = {104,114};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetWuDefenseLevel();
        	}
            tmpPer *= child_Effect.GetWuDefensePer();
        }
        double per = Judge.getLevelPer(level);
        wuDefensePer = per * tmpPer;
		return wuDefensePer;
	}
	
	//获取特攻系数
	private double teAttackPer;

	public double getTeAttackPer() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        double tmpPer = 1;
        int[] arr = {105,113};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetTeAttackPer();
        	}
            tmpPer *= child_Effect.GetTeAttackPer();
        }
        double per = Judge.getLevelPer(level);
        teAttackPer = per * tmpPer;
		return teAttackPer;
	}
	
	//获取特防系数
	private double teDefensePer;

	public double getTeDefensePer() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        double tmpPer = 1;
        int[] arr = {106,115};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetTeDefenseLevel();
        	}
            tmpPer *= child_Effect.GetTeDefensePer();
        }
        double per = Judge.getLevelPer(level);
        teDefensePer = per * tmpPer;
		return teDefensePer;
	}
	
	//获取速度系数
	private double speedPer;

	public double getSpeedPer() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        double tmpPer = 1;
        int[] arr = {107,116};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetSpeedLevel();
        	}
            tmpPer *= child_Effect.GetSpeedPer();
        }
        double per = Judge.getLevelPer(level);
        speedPer = per * tmpPer;
		return speedPer;
	}
	
	//获取要害等级（不属于7个属性）
	private int vitalLevel;
	
	public int getVitalLevel() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        int[] arr = {110};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetVitalLevel();
        	}
        }
        vitalLevel = level;
		return vitalLevel;
	}
	
	//命中等级
	private int hitLevel;
	
	public int getHitLevel() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        int[] arr = {117};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
        	if (!roleElves.GetIsCanntChangeLevel()){
        		level += child_Effect.GetHitLevel();
        	}
        }
        hitLevel = level;
		return hitLevel;
	}
	//命中系数
	private double hitPer;
	
	public double getHitPer() {
		//获取最新的系数
		double per = 0;
        int[] arr = {108};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
    		per *= child_Effect.GetHitPer();
        }
        hitPer = per;
		return hitPer;
	}
	
	//回避等级
	private int dodgeLevel;
	
	public int getDodgeLevel() {
		//获取最新的等级
        int level = 0;//elvesDataInforVo.wuDefenseLevel;//貌似应该是0
        int[] arr = {111};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
    		level += child_Effect.GetDodgeLevel();
        }
        dodgeLevel = level;
		return dodgeLevel;
	}
	
	//威力系数
	private double powPer;
	
	public double getPowPer() {
		//获取最新的系数
		double per = 0;
        int[] arr = {109};
        List<Child_Effect> list = roleElves.GetAttributeChildEffectList(arr);
        for(Child_Effect child_Effect : list){
    		per *= child_Effect.GetPowerPer();
        }
        powPer = per;
		return powPer;
	}
	
	
}
