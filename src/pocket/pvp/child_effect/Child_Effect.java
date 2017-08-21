package pocket.pvp.child_effect;

import java.util.ArrayList;
import java.util.List;

import pocket.pvp.entity.BuffInforVo;
import pocket.pvp.entity.ChildEffectParent;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.RoleElves;
import pocket.pvp.entity.SkillDataVo;
import pocket.total.util.SkillConstValue;

/**
 * 子效果
 * 子效果，由异常状态，装备，特性触发，子效果需要保存由什么触发的
 * 子效果在时间上，分为执行完就结束和保存下来（比如改变属性值的属性）
 * 由于子效果的执行依赖于产生该效果的对象的条件，所有对于保存下来的子效果在执行完就会清理掉
 * 
 * 附加给释放的目标身上，在谁身上谁就受到该效果
 * @author 陈鹏
 */
public class Child_Effect {
	
	private boolean isEnable;
	/// <summary>
    /// 该子效果是否可用，由于引发该子效果的宿主存在引发条件，所以需要判断在决定是否可用
    /// </summary>
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
		if(!isEnable){
			Reset();
		}
	}
	
	//类名
	public String name;

	//效果概率 用于buff引发的效果
	public double effectRate;
	//由什么引发的子效果
	public ChildEffectParent childEffectParent;
	
	//作用目标，有可能是null ,当作用目标不是精灵的时候就是null
	//如果需要被攻击方可以在需要的时候通过事件传递
	protected RoleElves targetRoleElves;
	
	//施加方的精灵key (不一定存在)
	protected String applyElvesKey;
	
    //效果值
    public String targetValue;

    //作用目标 给自己 还是 对手
    public int target;

    //检测宿主的实际方法，由于对象不固定，所以这里设置了方法的传递
    //参数根据是buff,还是特性,传递相应的id
//   public CheckFunc checkFunc;

    //宿主的id，依赖的父级，如特性， buff，技能id
    public String parentObjId;

    //子效果的附加参数
//    public String[] parameterArr;
    public List<String> parameterArr;

    //生效时机
    public int effectiveTimeId;

    //生效条件
    public int take;

    //生效条件对应值 
    public String takeValue;

    //是否是只存在一回合的
    public boolean isOnce;
    
    //触发台词是否只播放一次
    public boolean isTriggerLineOne;
    private String triggerLine;
    // 触发失败的台词
    public String triggerFailLine;
    
    //子效果id
    public int id;

    public Child_Effect(int _target, String _targetValue, int _id)
    {
    	targetValue = _targetValue;
        target = _target;
        id = _id;
        
        name = this.getClass().getName();
    }
    public Child_Effect(){
    	name = this.getClass().getName();
    }
    
    //检测该子效果是否就可以正常使用
    //比如特性引发的子效果,当条件不满足时是不能正常使用的
    public boolean checkIsUse(Object[] args){
    	
    	if(childEffectParent == ChildEffectParent.Features){
    		ChildEffectParent.valueOf("Features");
    		//特性的 arr[0]为普通条件, arr[1]为技能条件
    		String[] arr = takeValue.split("/");
    		
    		//检测技能属性
    		if(args.length>0 && args[0] instanceof SkillDataVo && !isNullOrEmpty(arr[0])){
    			SkillDataVo skill = (SkillDataVo) args[0];
    			//先判断技能属性是否满足
    			if(SkillConstValue.AttributeList.indexOf(skill.getSkill_ability()) != Integer.parseInt(arr[1])){
    				//不满足无法使用
                    return false;
    			}
    		}
    		
//    		 if (take == 1)
//             {
//                 //天气
//                 //this.PRINT("===   " + arr[0] + " " + id);
//                 int[] weather = MyUtil.ArrayToInt(arr[0].Split(':'));
//                 for (int i = 0; i < weather.Length; i++)
// 			    {
//                     bool b=WeatherMgr.Instance.IsHaveWeather((WeatherType)weather[i]);
//                     if(b)
// 			            return true;
// 			        ////arr[1]可能是多个技能属性
// 			        //List<string> tmpList = Util.ArrayToListToString(arr[1].Split(':'));
// 			        //if (b && tmpList.Contains(vo.Skill_type))
// 			        //{
// 			        //    //条件都满足
// 			        //    return true;
// 			        //}
// 			    }
//                 return false;
//             }
    		else if (take == 2){
                //hp不足
                if (targetRoleElves.getElves_value().getHP() < Integer.parseInt(arr[0])){
                    return true;
                }
                return false;
            }else if (take == 3){
                //buff
               String[] tmp = arr[0].split(":");
               for (int i = 0; i < tmp.length; i++){
                   //获取角色山上有的buff
                   List<BuffInforVo> list = targetRoleElves.getStateSkillEffectList();
                   for (int j=0;j<list.size();j++){
                       if (list.get(j).getBuffDataVo().getBuff_id() == Integer.parseInt(tmp[i])){
                           return true;
                       }
                   }
               }
                return false;
            }else{
                //没有条件是true
                return true;
            }
    		
    	}else if (childEffectParent == ChildEffectParent.Buff){
            if (take == 1){
                //技能属性检测
                SkillDataVo skill = (SkillDataVo)args[0];
                if (skill.getSkill_ability() == takeValue){
                    return true;
                }else{
                    return false;
                }
            }else if (take == 4){
                if (args.length == 0 || isNullOrEmpty(takeValue)){
                    return true;
                }
                //检测攻击方使用的技能 是设置的则无效 如睡眠引发的子效果在使用打鼾的时候是无效的
                SkillDataVo skill = (SkillDataVo)args[0];
                String[] arr = takeValue.split(",");
                for (int i = 0; i < arr.length; i++){
                    if (Integer.parseInt(arr[i]) == skill.getSkill_id()){
                        return false;
                    }
                }
                return true;
            }else{
                return true;
            }
            
    	}else if (childEffectParent == ChildEffectParent.Equip){
            //装备
            if(take == 1){
                //技能属性检测
                SkillDataVo skill = (SkillDataVo)args[0];
                int index = SkillConstValue.AttributeList.indexOf(skill.getSkill_ability());
                if (String.valueOf(index).equals(takeValue)){
                    return true;
                }else{
                    return false;
                }

            }else if (take == 3){
                //是否是第一次使用技能
             //   RoleElves attack = args[2] as RoleElves;
                RoleElves attack = (RoleElves)args[1];
                return attack.getSkillUseRecord().size() == 0;

            }else if (take == 4){
                //身上的hp为满血状态下
                
                int hitValue = Integer.parseInt(args[1].toString());//伤害值

                if (targetRoleElves.getElve().getHp() == targetRoleElves.getMaxHp() && hitValue >= targetRoleElves.getElve().getHp()){
                    return true;
                }else{
                    return false;
                }
            }else if (take == 5){
                //特定的攻击方
                Elve attack = ((RoleElves)args[1]).getElve();
                String[] str = takeValue.split(":");
                List<String> list = arrayToListToString(str);
                return list.contains(attack.geteLvesID());
            }
        
        }else if (childEffectParent == ChildEffectParent.Weather){ 
            //天气  0是技能属性  满足可以使用  1是精灵属性  满足可以免除

            //无实际条件
            if (targetValue.trim() == "/"){
            	return true;
            }

            String[] takeValueArr = takeValue.split("/");
            int index = 0;

          //  Debug.LogWarning("takeValueArr  " + takeValue+"  "+takeValueArr.Length);

            //检测技能属性
            if (!isNullOrEmpty(takeValueArr[0])){
                SkillDataVo skill = (SkillDataVo)args[0];
                String[] str = takeValueArr[0].split(",");
                List<Integer> skillList = arrayToListToInt(str);   
                index  = SkillConstValue.AttributeList.indexOf(skill.getSkill_ability());
                if (!skillList.contains(index))
                {
                    return false;
                }
            }

            //检测精灵属性
            if(!isNullOrEmpty(takeValueArr[1])){
                if (targetRoleElves == null){
                    return true;
                }
                String[] types = targetRoleElves.getElve().getElves_type().split(",");
                List<Integer> attributeList = arrayToListToInt((takeValueArr[1].split(",")));
                
                for (int i = 0; i < types.length; i++){
                    index = SkillConstValue.AttributeList.indexOf(types[i]);
                    if (attributeList.contains(index)){
                        //免疫
                        //1.替换[Elves_type]
//                        if (!isNullOrEmpty(triggerFailLine))
//                            triggerFailLine = triggerFailLine.Replace("[Elves_type]", elvesDataInforVo.elvesDataVo.ElvesTypeArr[i]);

                        return false;
                    }
                }
            }
            return true;
        }
    	//默认满足
        return true;
    }
    
    
    public boolean isNullOrEmpty(String takeValue){
    	return takeValue==null || takeValue.isEmpty(); 
    }
    
    public List<String> arrayToListToString(String[] str){
    	List<String> list = new ArrayList<String>();
    	for(int i=0;i<str.length;i++){
    		list.add(str[i]);
    	}
		return list;
    }
    public List<Integer> arrayToListToInt(String[] str){
    	List<Integer> list = new ArrayList<Integer>();
    	for(int i=0;i<str.length;i++){
    		list.add(Integer.parseInt(str[i]));
    	}
		return list;
    }
    
    //部分子效果的效果由调用init来实现，如减少hp4
    public void init(Object arges){
    	
    }
    
    //--------------------------------------其余部分由属性值或方法实现  start---------------------------------

    public double GetPowerValue()
    {
        return Double.MAX_VALUE;
    }

     //添加buff的概率的倍数
    public double GetBuffAddRate()
    {
        return 1;
    }

    //物攻系数
    public double GetWuAttackPer()
    {
        return 1;

    }

    //物防系数
    public double GetWuDefensePer()
    {
        return 1;

    }
    //特攻系数
    public double GetTeAttackPer()
    {
        return 1;

    }

    //特防系数
    public double GetTeDefensePer()
    {
        return 1;

    }

     //速度系数
    public double GetSpeedPer()
    {
        return 1;
    }

    //命中系数
    public double GetHitPer()
    {
        return 1;
    }

    //威力系数
    public double GetPowerPer()
    {
        return 1;
    }

     //要害等级
    public int GetVitalLevel()
     {
         return 0;
     }

     //闪避等级
    public int GetDodgeLevel()
     {
         return 0;
     }


     //物攻等级
    public int GetWuAttackLevel()
    {
         return 0;
        
    }

     //物防等级
    public int GetWuDefenseLevel()
    {
        return 0;

    }

    //特攻等级
    public int GetTeAttackLevel()
    {
        return 0;

    }

    //特防等级
    public int GetTeDefenseLevel()
    {
        return 0;

    }

     //速度等级
    public int GetSpeedLevel()
    {
        return 0;

    }

    //命中等级
    public int GetHitLevel()
    {
        return 0;

    }

     //PP值消耗
    public int GetPPValue()
    {
        return 0;
    }


     //物理伤害系数
    public double WuDamagePer()
    {
        return 1;
    }

    //特殊伤害系数
    public double TeDamagePer()
    {
        return 1;
    }

    //--------------------------------------其余部分由属性值或方法实现  end---------------------------------
    
    //是否可以行动  精灵跳过行动
    public boolean isCanntAction;


    //技能使用失败（可以点击但是会提示失败）
    public boolean isSkillFail;


     //是否更换精灵
    public boolean isCantChangeElves;
   
    //无法离场
    public boolean isCantLeave;


    //是否强制离场
    public boolean isMustLeave;

     //是否必中
    public boolean isMustHit;


    //是否被必中
    public boolean isMustBeHit;

     //是否可以恢复hp
    public boolean isCanntResetHp;


    //特性是否处于失效状态
    public boolean isCannotFeatures;


     //是否处于不可改变等级值
    public boolean isCanntChangeLevel;

    //精灵属性失效
    public boolean isElvesAttributeFail;



     //在濒死的时候是否保留1点hp
    public boolean isKeepOneHp;


    //变化技能无法使用
    public boolean isCanntChangeSkill;

     //是否免疫要害
    public boolean isCanntVital;


     //阻止一次攻击
    public boolean isStopOneAtk;

     //是否可以连续使用同一个技能
    public boolean isCouSameSkill;

     //是不是只能使用上一个技能
    public boolean isOnlyLastSkill;

     //是否改变攻击目标
    public boolean isChangeTarget;

    private void Reset()
    {

        isCanntAction = isSkillFail  = isCantLeave = isMustHit = isMustBeHit = isCanntResetHp = false;
        isCannotFeatures = isCanntChangeLevel =isStopOneAtk= isCanntChangeSkill = isCanntVital=false;
        isCantChangeElves =isKeepOneHp= false;
        isCouSameSkill = isOnlyLastSkill =isChangeTarget=isElvesAttributeFail= false;

    }

    //在使用百分比时需要将值转换下
    protected double GetPer()
    {
    	//double value = (double)Math.Round(Math.Abs(targetValuedouble/100), 2);
        double value = (double)Math.round(Math.abs(targetValueDouble()/100));

        if (targetValueDouble() < 0)
        {
            return value;
        }
        else
        {
            return 1 + value;
        }

    }

     //转换为小数值 用于hp计算
    protected double GetPerInit()
    {
        return  (double)Math.round(targetValueDouble() / 100);
    }

     //false为不能添加，被免疫了
	public boolean CheckAddBuff(String buffId)
	{
		return true;
	}

     //将效果值转换为double值
    public double targetValueDouble()
    {
//      this.PRINT_WARNING("targetValuedouble  " + targetValue + "  name:  " + name+"  id  "+id);
       return Double.parseDouble((targetValue));
    }

    //将效果值转换为int值
    public int targetValueInt()
    {
//       this.PRINT_WARNING("targetValueInt  " + targetValue + "  name:  " + name + "  id  " + id);
        return Integer.parseInt((targetValue));
    }

     //效果值 通过计算获取 由外部使用
    public double GetEffectCalculateValue()
    {
        return 0;
    }
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getEffectRate() {
		return effectRate;
	}
	public void setEffectRate(double effectRate) {
		this.effectRate = effectRate;
	}
	public RoleElves getTargetRoleElves() {
		return targetRoleElves;
	}
	public void setTargetRoleElves(RoleElves _targetRoleElves, String _applyElvesKey) {
		if (_targetRoleElves != null)
        {
            targetRoleElves = _targetRoleElves;
            applyElvesKey = _applyElvesKey;
        }
        else
        {
            System.out.println("SetTargetRoleElves 为null");
            
        }
	}
	public String getApplyElvesKey() {
		return applyElvesKey;
	}
	public void setApplyElvesKey(String applyElvesKey) {
		this.applyElvesKey = applyElvesKey;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	
	public ChildEffectParent getChildEffectParent() {
		return childEffectParent;
	}
	public void setChildEffectParent(ChildEffectParent childEffectParent) {
		this.childEffectParent = childEffectParent;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getParentObjId() {
		return parentObjId;
	}
	public void setParentObjId(String parentObjId) {
		this.parentObjId = parentObjId;
	}
	public int getEffectiveTimeId() {
		return effectiveTimeId;
	}
	public void setEffectiveTimeId(int effectiveTimeId) {
		this.effectiveTimeId = effectiveTimeId;
	}
	public int getTake() {
		return take;
	}
	public void setTake(int take) {
		this.take = take;
	}
	public String getTakeValue() {
		return takeValue;
	}
	public void setTakeValue(String takeValue) {
		this.takeValue = takeValue;
	}
	public boolean isOnce() {
		return isOnce;
	}
	public void setOnce(boolean isOnce) {
		this.isOnce = isOnce;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isCanntAction() {
		return isCanntAction;
	}
	public void setCanntAction(boolean isCanntAction) {
		this.isCanntAction = isCanntAction;
	}
	public boolean isSkillFail() {
		return isSkillFail;
	}
	public void setSkillFail(boolean isSkillFail) {
		this.isSkillFail = isSkillFail;
	}
	public boolean isCantChangeElves() {
		return isCantChangeElves;
	}
	public void setCantChangeElves(boolean isCantChangeElves) {
		this.isCantChangeElves = isCantChangeElves;
	}
	public boolean isCantLeave() {
		return isCantLeave;
	}
	public void setCantLeave(boolean isCantLeave) {
		this.isCantLeave = isCantLeave;
	}
	public boolean isMustLeave() {
		return isMustLeave;
	}
	public void setMustLeave(boolean isMustLeave) {
		this.isMustLeave = isMustLeave;
	}
	public boolean isMustHit() {
		return isMustHit;
	}
	public void setMustHit(boolean isMustHit) {
		this.isMustHit = isMustHit;
	}
	public boolean isMustBeHit() {
		return isMustBeHit;
	}
	public void setMustBeHit(boolean isMustBeHit) {
		this.isMustBeHit = isMustBeHit;
	}
	public boolean isCanntResetHp() {
		return isCanntResetHp;
	}
	public void setCanntResetHp(boolean isCanntResetHp) {
		this.isCanntResetHp = isCanntResetHp;
	}
	public boolean isCannotFeatures() {
		return isCannotFeatures;
	}
	public void setCannotFeatures(boolean isCannotFeatures) {
		this.isCannotFeatures = isCannotFeatures;
	}
	public boolean isCanntChangeLevel() {
		return isCanntChangeLevel;
	}
	public void setCanntChangeLevel(boolean isCanntChangeLevel) {
		this.isCanntChangeLevel = isCanntChangeLevel;
	}
	public boolean isElvesAttributeFail() {
		return isElvesAttributeFail;
	}
	public void setElvesAttributeFail(boolean isElvesAttributeFail) {
		this.isElvesAttributeFail = isElvesAttributeFail;
	}
	public boolean isKeepOneHp() {
		return isKeepOneHp;
	}
	public void setKeepOneHp(boolean isKeepOneHp) {
		this.isKeepOneHp = isKeepOneHp;
	}
	public boolean isCanntChangeSkill() {
		return isCanntChangeSkill;
	}
	public void setCanntChangeSkill(boolean isCanntChangeSkill) {
		this.isCanntChangeSkill = isCanntChangeSkill;
	}
	public boolean isCanntVital() {
		return isCanntVital;
	}
	public void setCanntVital(boolean isCanntVital) {
		this.isCanntVital = isCanntVital;
	}
	public boolean isStopOneAtk() {
		return isStopOneAtk;
	}
	public void setStopOneAtk(boolean isStopOneAtk) {
		this.isStopOneAtk = isStopOneAtk;
	}
	public boolean isCouSameSkill() {
		return isCouSameSkill;
	}
	public void setCouSameSkill(boolean isCouSameSkill) {
		this.isCouSameSkill = isCouSameSkill;
	}
	public boolean isOnlyLastSkill() {
		return isOnlyLastSkill;
	}
	public void setOnlyLastSkill(boolean isOnlyLastSkill) {
		this.isOnlyLastSkill = isOnlyLastSkill;
	}
	public boolean isChangeTarget() {
		return isChangeTarget;
	}
	public void setChangeTarget(boolean isChangeTarget) {
		this.isChangeTarget = isChangeTarget;
	}
	public boolean isTriggerLineOne() {
		return isTriggerLineOne;
	}
	public void setTriggerLineOne(boolean isTriggerLineOne) {
		this.isTriggerLineOne = isTriggerLineOne;
	}
	public String gettriggerLine() {
		return triggerLine;
	}
	public void settriggerLine(String triggerLine) {
		this.triggerLine = triggerLine;
	}
	public String getTriggerFailLine() {
		return triggerFailLine;
	}
	public void setTriggerFailLine(String triggerFailLine) {
		this.triggerFailLine = triggerFailLine;
	}
	public List<String> getParameterArr() {
		return parameterArr;
	}
	public void setParameterArr(List<String> parameterArr) {
		this.parameterArr = parameterArr;
	}
	@Override
	public String toString() {
		return "Child_Effect [isEnable=" + isEnable + ", name=" + name + ", effectRate=" + effectRate
				+ ", childEffectParent=" + childEffectParent + ", targetRoleElves=" + targetRoleElves
				+ ", applyElvesKey=" + applyElvesKey + ", targetValue=" + targetValue + ", target=" + target
				+ ", parentObjId=" + parentObjId + ", parameterArr=" + parameterArr + ", effectiveTimeId="
				+ effectiveTimeId + ", take=" + take + ", takeValue=" + takeValue + ", isOnce=" + isOnce
				+ ", isTriggerLineOne=" + isTriggerLineOne + ", triggerLine=" + triggerLine + ", triggerFailLine="
				+ triggerFailLine + ", id=" + id + ", isCanntAction=" + isCanntAction + ", isSkillFail=" + isSkillFail
				+ ", isCantChangeElves=" + isCantChangeElves + ", isCantLeave=" + isCantLeave + ", isMustLeave="
				+ isMustLeave + ", isMustHit=" + isMustHit + ", isMustBeHit=" + isMustBeHit + ", isCanntResetHp="
				+ isCanntResetHp + ", isCannotFeatures=" + isCannotFeatures + ", isCanntChangeLevel="
				+ isCanntChangeLevel + ", isElvesAttributeFail=" + isElvesAttributeFail + ", isKeepOneHp=" + isKeepOneHp
				+ ", isCanntChangeSkill=" + isCanntChangeSkill + ", isCanntVital=" + isCanntVital + ", isStopOneAtk="
				+ isStopOneAtk + ", isCouSameSkill=" + isCouSameSkill + ", isOnlyLastSkill=" + isOnlyLastSkill
				+ ", isChangeTarget=" + isChangeTarget + "]";
	}
    
    
}



