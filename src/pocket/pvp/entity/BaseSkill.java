package pocket.pvp.entity;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.child_effect.Child_Effect;
import pocket.pvp.child_effect.Child_Effect_Factory;
import pocket.total.util.JsonRead;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;

public class BaseSkill {
	private String name;
	private String skillId;
	
	
	protected RoleElves attackRoleElves;
	protected RoleElves beAttackRoleElves;
	
	//当前技能数据
	public SkillDataVo curSkillDataVo;
	
	//技能的目标对象
	protected int target;
	
	protected Elve attackElvesDataInforVo;
	protected Elve beAttackElvesDataInforVo;
	protected boolean isDie;
    protected SkillEffectVo curSkillEffectVo;
    protected boolean isFirstHand ;
    
    //技能引发的所有buff
    protected List<BuffInforVo> skillStateInforList;
    //攻击角色身上的数值
    protected AttributeValue attributeValue;
    protected AttributeValue beAttributeValue;
    
    //是否改变攻击目标
    private boolean isChangeTarget;
    
    private SkillUseInforVo skillUseInfor;
    
    
    public BaseSkill(RoleElves _attackRoleElves, RoleElves _beAttackRoleElves, 
    				Elve _attackElvesDataInforVo, Elve _beAttackElvesDataInforVo,
    				SkillDataVo _curSkillDataVo, boolean _isFirstHand){
    	if ((_attackRoleElves == null && _beAttackRoleElves == null) || _curSkillDataVo == null){
            System.out.println("Skill_PuTong 数据传入有误");
            return;
        }

        attackRoleElves = _attackRoleElves;
        beAttackRoleElves = _beAttackRoleElves;
        attackElvesDataInforVo = _attackElvesDataInforVo;
        beAttackElvesDataInforVo = _beAttackElvesDataInforVo;
        isFirstHand = _isFirstHand;

//        realCallFunc = _callFunc;

        curSkillDataVo = _curSkillDataVo;

        skillUseInfor = new SkillUseInforVo();
        skillUseInfor.setSkillDataVo(_curSkillDataVo);

        // attributeBase = attackRoleElves.attributeBase;
        attributeValue = attackRoleElves.attributeValue;

        //Debug.Log("BaseSkill"+" "+attributeBase + " " + attributeValue);

        //获取技能引发的子效果及buff
//        curSkillEffectVo = (BaseConfig.Instatance.skillEffectCollection.GetItemById( curSkillDataVo.Skill_id) as SkillEffectVo);
        curSkillEffectVo = JsonRead.skillEffectFind(curSkillDataVo.getSkill_id(), StaticClass.skill_effect_js);
//        ShowAttackProcess();
    	
    }
    
    //添加技能引发的子效果
    public void AddChildEffect(JSONArray jsonArray){
    	if (curSkillDataVo.getRate()/100f < MyUtil.getRandomValue01()){
    		//检测概率
    		return;
    	}
    	if (curSkillEffectVo == null || MyUtil.isNullOrEmpty(curSkillEffectVo.getTarget())){
    		return;
    	}
    	//获取子效果的添加目标
    	//目标数组
        int[] targetArr = MyUtil.ArrayToInt(curSkillEffectVo.getTarget().split(";"));
        
        //特殊条件
        int[] takeArr = MyUtil.SafeArrayToInt(curSkillEffectVo.getTake(), targetArr.length, ";");
        String[] takeValueArr = MyUtil.SafeArrayToString(curSkillEffectVo.getValue(), targetArr.length, ";");
        
        //效果参数
		String[] effectArr = curSkillEffectVo.getEffect().split(";");//效果id
		String[] effectValueArr = MyUtil.SafeArrayToString(curSkillEffectVo.getValue1(),effectArr.length,";");//效果值
		String[] rateArr = curSkillEffectVo.getRate().split(";");//效果概率
		String[] textArr = MyUtil.isNullOrEmpty(curSkillEffectVo.getText()) ? null : curSkillEffectVo.getText().split(";");//效果触发台词
		
		//效果生效时机
        String[] takeTimeArr = curSkillEffectVo.getTake_time().split(";");
		
        String[] strArr = null;
        int index = 0;
        
        for (int j = 0; j < targetArr.length; j++){
        	RoleElves targetRole = targetArr[j] == 0 ? attackRoleElves : beAttackRoleElves;//BattleCalculate.GetElvesTarget(targetArr[j]);
        	 
        	//施加方的精灵key  如果是添加给自己的就不需要设置了
            String applyElvesKey = targetArr[j] == 0 ? "" : MyUtil.elvesKey(attackRoleElves.getElve());
            
            boolean isSuc = false;
            //存在添加的特殊条件
            switch (takeArr[j]) {
				case 1:
					//1，天气
					strArr = takeValueArr[j].split(",");
	                for (int i = 0; i < strArr.length; i++){
	                    //
	                    int[] arr2 = MyUtil.ArrayToInt(strArr[i].split("："));
	                    for (int n = 0; n < arr2.length; n++){
	                        if (WeatherMgr.getInstance().IsHaveWeather(arr2[n])){
	                            //处于该天气
	                            index = n;
	                            isSuc = true;
	                            break;
	                        }
	                    }
	                }
					break;
				case 2:
	                //2，目标处于某状态
	                strArr= takeValueArr[j].split(",");
	                for (int i = 0; i < strArr.length; i++){
	                    if (beAttackRoleElves.IsHaveState(strArr[i])){
	                        isSuc = true;
	                        break;
	                    }
	                }
	                break;
				case 3:
	                //3，自身处于某状态
	                strArr= takeValueArr[j].split(",");
	                for (int i = 0; i < strArr.length; i++){
	                    if (attackRoleElves.IsHaveState(strArr[i])){
	                        isSuc = true;
	                        break;
	                    }
	                }
	                break;  
				case 4:
	                //4，自身HP状态(64*剩下HP/最大HP)
	                index = BattleConst.GetPowerIndexByHpPer((float)(attackElvesDataInforVo.getHp())/attackRoleElves.getMaxHp());
	                isSuc = true;
	                break; 
				case 5:
	                // 5，使用过某技能
	                 strArr= takeValueArr[j].split(",");
	                for (int i = 0; i < strArr.length; i++){
	                    if (attackRoleElves.isUseSkill(strArr[i])){
	                        isSuc = true;
	                        break;
	                    }
	                }
	                break;  
				case 6:
	                //回合连续命中 大于等于2视为连续命中
	                isSuc = attackRoleElves.getIsConHit();
	
	                break;   
				default:
					isSuc = true;
					break;
			}
            if(isSuc){
            	//添加子效果
            	String[] effectArr2 = effectArr[j].split(",");
                String[] valueArr2 = effectValueArr[j].split(",");
                String[] rateArr2 = rateArr[j].split(",");
                String[] textArr2 = textArr==null ? null : textArr[j].split(",");

                String[] temp = takeTimeArr[j].split(",");
                int[] takeTime2Arr = null;
                if (temp.length == effectArr2.length)
                    takeTime2Arr = MyUtil.ArrayToInt(takeTimeArr[j].split(","));
                else
                    takeTime2Arr = MyUtil.ArrayToIntSame(temp[0], effectArr2.length);

                for (int i = 0; i < effectArr2.length; i++){

                    String targetValue = "";
                    double rate = 0;
                    String effect = "";

                    if (takeArr[j] == 1 || takeArr[j] == 4){
                        String[] tmp = valueArr2[i].split(":");
                        targetValue = tmp[index];

                        String[] tmpRateArr =rateArr2[i].split(":");
                        rate= Double.parseDouble(tmpRateArr[index]);

                        String[] tmpEffectArr =effectArr2[i].split(":");
                        effect= tmpEffectArr[index];
                    }else{
                        rate = Double.parseDouble(rateArr2[i]);
                        effect = effectArr2[i];
                        targetValue = valueArr2[i];
                    }

                    //创建子效果
                    Child_Effect cd = Child_Effect_Factory.getInstance().create(effect, targetArr[j], targetValue);
                    if (cd == null){
                        continue;
                    }

                    for(int m=0;m<jsonArray.size();m++){
                    	JSONObject jo = jsonArray.getJSONObject(i);
//                    	JSONObject joCE = jo.getJSONObject("childEffect");
//                    	if(MyUtil.isNullOrEmpty(effect)){
//                    		continue;
//                    	}
                    	if((String.valueOf(jo.getInt("id"))).equals(effect)){
                    		cd = setChild_Effect(jo, cd);
                    	}
                    }
                    cd.setEnable(true);

                    //效果id

                    //效果值

                    //效果的触发时机
                    cd.setEffectiveTimeId(takeTime2Arr[i]);

                    //效果概率
                    cd.setEffectRate(rate/100);

                    //判断是否是一次性的
                    cd.setOnce(BattleCalculate.GetChildEffectIsOne(effect));

                    //设置效果绑定在哪个角色上
                   // cd.SetTargetRoleElves(targetRole);

                    //直播一次
                    cd.setTriggerLineOne(true);

//                    String str = textArr2 == null ? "" : (textArr2.length >= i + 1 ? textArr2[i] : textArr2[0]);
//                    cd.set_triggerLine( MyUtil.GetTextByLanguage(str));

                    //绑定宿主id
                    cd.setParentObjId(String.valueOf(curSkillDataVo.getSkill_id()));

                    //
                    cd.setChildEffectParent(ChildEffectParent.Skill);

                    //创建额外的特效
//                    if (!cd.isOnce)
//                    {
//                        int v=Integer.parseInt(targetValue);
//                        String effectName = v< 0 ? "302_" : "301_";
//                        boolean isOwn = targetRole.roleType == GameEnum.RoleType.Player ? true : false;
//                        BattleCalculate.CreateAdditionalEffect(effectName, isOwn);
//                    }

                    targetRole.AddChildEffect(cd, applyElvesKey);
                }
            }
        }
    }
    public SkillDataVo getCurSkillDataVo(){
		return curSkillDataVo;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public boolean isChangeTarget() {
		return isChangeTarget;
	}
	public void setChangeTarget(boolean isChangeTarget) {
		this.isChangeTarget = isChangeTarget;
	}
	@Override
	public String toString() {
		return "BaseSkill [name=" + name + ", skillId=" + skillId + "]";
	}
	
	//创建子效果
	private Child_Effect setChild_Effect(JSONObject jsonObject, Child_Effect child_effect){
		if(child_effect == null){
			return child_effect;
		}
		String name = jsonObject.getString("name");
		double effectRate = jsonObject.getDouble("effectRate");
		int childEffectParentID = jsonObject.getInt("childEffectParent");
		String targetValue = jsonObject.getString("targetValue");
//		int target = jsonObject.getInt("target");
		String parentObjId = jsonObject.getString("parentObjId");
//			JSONArray joArr = jsonObject.getJSONArray("parameterArr");
		int effectiveTimeId = jsonObject.getInt("effectiveTimeId");
		int take = jsonObject.getInt("take");
		String takeValue = jsonObject.getString("takeValue");
		boolean isOnce = jsonObject.getBoolean("isOnce");
		boolean isTriggerLineOne = jsonObject.getBoolean("isTriggerLineOne");
		String triggerFailLine = jsonObject.getString("triggerFailLine");
		int id = jsonObject.getInt("id");
		
		boolean isCanntAction = jsonObject.getBoolean("isCanntAction");
		boolean isSkillFail = jsonObject.getBoolean("isSkillFail");
		boolean isCantChangeElves = jsonObject.getBoolean("isCantChangeElves");
		boolean isCantLeave = jsonObject.getBoolean("isCantLeave");
		boolean isMustLeave = jsonObject.getBoolean("isMustLeave");
		boolean isMustHit = jsonObject.getBoolean("isMustHit");
		boolean isMustBeHit = jsonObject.getBoolean("isMustBeHit");
		boolean isCanntResetHp = jsonObject.getBoolean("isCanntResetHp");
		boolean isCannotFeatures = jsonObject.getBoolean("isCannotFeatures");
		boolean isCanntChangeLevel = jsonObject.getBoolean("isCanntChangeLevel");
		boolean isElvesAttributeFail = jsonObject.getBoolean("isElvesAttributeFail");
		boolean isKeepOneHp = jsonObject.getBoolean("isKeepOneHp");
		boolean isCanntChangeSkill = jsonObject.getBoolean("isCanntChangeSkill");
		boolean isCanntVital = jsonObject.getBoolean("isCanntVital");
		boolean isStopOneAtk = jsonObject.getBoolean("isStopOneAtk");
		boolean isCouSameSkill = jsonObject.getBoolean("isCouSameSkill");
		boolean isOnlyLastSkill = jsonObject.getBoolean("isOnlyLastSkill");
		boolean isChangeTarget = jsonObject.getBoolean("isChangeTarget");
		
		child_effect.setName(name);
		child_effect.setEffectRate(effectRate);
//				child_effect.setApplyElvesKey(applyElvesKey);
		child_effect.setParentObjId(parentObjId);
		child_effect.setEffectiveTimeId(effectiveTimeId);
		child_effect.setTake(take);
		child_effect.setTakeValue(takeValue);
		child_effect.setOnce(isOnce);
		child_effect.setId(id);
		child_effect.setTargetValue(targetValue);
		child_effect.setChildEffectParent(getEnum(childEffectParentID));
		child_effect.setTriggerLineOne(isTriggerLineOne);
		child_effect.setTriggerFailLine(triggerFailLine);
		
		child_effect.setCanntAction(isCanntAction);
		child_effect.setSkillFail(isSkillFail);
		child_effect.setCantChangeElves(isCantChangeElves);
		child_effect.setCantLeave(isCantLeave);
		child_effect.setMustLeave(isMustLeave);
		child_effect.setMustHit(isMustHit);
		child_effect.setMustBeHit(isMustBeHit);
		child_effect.setCanntResetHp(isCanntResetHp);
		child_effect.setCannotFeatures(isCannotFeatures);
		child_effect.setCanntChangeLevel(isCanntChangeLevel);
		child_effect.setElvesAttributeFail(isElvesAttributeFail);
		child_effect.setKeepOneHp(isKeepOneHp);
		child_effect.setCanntChangeSkill(isCanntChangeSkill);
		child_effect.setCanntVital(isCanntVital);
		child_effect.setStopOneAtk(isStopOneAtk);
		child_effect.setCouSameSkill(isCouSameSkill);
		child_effect.setOnlyLastSkill(isOnlyLastSkill);
		child_effect.setChangeTarget(isChangeTarget);
		
		return child_effect;
		
	}
		
	private ChildEffectParent getEnum(int i){
		switch (i) {
		case 1:
			return ChildEffectParent.Buff;
		case 2:
			return ChildEffectParent.Features;
		case 3:
			return ChildEffectParent.Effect;
		case 4:
			return ChildEffectParent.Equip;
		case 5:
			return ChildEffectParent.Weather;
		case 6:
			return ChildEffectParent.Skill;
		default:
			break;
		}
		return ChildEffectParent.None;
	}

}

class ResultLineVo
{
    /// <summary>
    /// 结果
    /// </summary>
    public boolean result;

    /// <summary>
    /// 台词
    /// </summary>
    public String line;


    public ResultLineVo(boolean result, String line)
    {
        this.result = result;
        this.line = line;
    }


    public ResultLineVo()
    {

    }


}
