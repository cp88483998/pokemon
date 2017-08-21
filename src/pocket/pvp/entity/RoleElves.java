package pocket.pvp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import pocket.pvp.child_effect.Child_Effect;
import pocket.total.util.MyUtil;

/**
 * 精灵角色类
 * <p>Title: RoleElves<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年4月11日
 */
public class RoleElves {
	private Elve elve;
	private Elves_value elves_value;
	
//	private List<Double> randomList;
	private JSONArray randomList;
	//最大血值
    private int maxHp;
    
    private Map<String, SkillDataVo> studySkills;//技能列表
    private List<BuffInforVo> stateSkillEffectList;//buff列表
    
    private JSONArray childEffectListJA = new JSONArray();//子效果列表
    private JSONArray stateSkillEffectListJA = new JSONArray();//buff列表
    private List<SkillUseRecord> skillUseRecord;
    private int randomIndex;
	private boolean isFirstSkill;
	private long wuHitValue;
	private int hitValue;
    
    //是否包含这个子效果
    public boolean isHaveChildEffect(int childEffectId){
        for (int i = 0; i < childEffectList.size(); i++){
            if (childEffectList.get(i).id == childEffectId && childEffectList.get(i).isEnable()){
                return true;
            }
        }
        return false;
    }
    //是否使用过该技能
    public boolean isUseSkill(String skillId){
        if (MyUtil.isNullOrEmpty(skillId)){
            return false;
        }

        for (int i = 0; i < skillUseRecord.size(); i++){
            if (skillUseRecord.get(i).getSkillDataVo().getSkill_id() == Integer.parseInt(skillId)){
                return true;
            }
        }

        return false;


    }
    //判断是否连续命中
    public boolean getIsConHit(){
        if (skillUseRecord.size() < 2)
            return false;
        int count = 0;
        for (int i = 0; i < skillUseRecord.size(); i++){
            if (skillUseRecord.get(i).isHit()){
                count++;
            }
            if (count >= 2){
                return true;
            }
        }
        return false;
    }
    
    //添加子效果
    public void AddChildEffect(Child_Effect _ChildEffect,String elvesKey){
        if (_ChildEffect == null){
            return;
        }

        _ChildEffect.setTargetRoleElves(this, elvesKey);
        if(getChildEffectList() == null){
        	List<Child_Effect> list = new ArrayList<Child_Effect>();
        	list.add(_ChildEffect);
        }else{
        	getChildEffectList().add(_ChildEffect);
        }
        

//        ChangeChildEffect();//更新hp子效果的提示

    }
    
    //保存所有子效果
    private List<Child_Effect> childEffectList;
    //保存所有可用的子效果
    private List<Child_Effect> enablechildEffectList;

    //获取所有的可用的效果
    public List<Child_Effect> GetEnablechildEffectList(){
    	enablechildEffectList.clear();
    	for(Child_Effect child_Effect : childEffectList){
    		if(child_Effect.isEnable()){
    			enablechildEffectList.add(child_Effect);
    		}
    	}
        return enablechildEffectList;
    }
    
    //获取属性效果
    public List<Child_Effect> GetAttributeChildEffectList(int[] attributeArgs){
    	if (attributeArgs == null || attributeArgs.length==0){
    		return null;
    	}
    	enablechildEffectList.clear();
    	for(Child_Effect child_Effect : childEffectList){
    		 for (int i = 0; i < attributeArgs.length; i++){
                 if (child_Effect.id == attributeArgs[i]){
                     enablechildEffectList.add(child_Effect);
                     break;
                 }
             }
    	}
		return enablechildEffectList;
    }
    
    //获取某个可用的ChildEffect
    public Child_Effect GetChildEffectById(int id){
        for (int i = 0; i < childEffectList.size(); i++){
            if (childEffectList.get(i).id == id && childEffectList.get(i).isEnable()){
                return childEffectList.get(i);
            }
        }
        return null;
    }
    
    //判断能力等级是否可以个改变
    public boolean GetIsCanntChangeLevel(){
        for (int i = 0; i < childEffectList.size(); i++){
            if (childEffectList.get(i).isEnable() && childEffectList.get(i).isCanntChangeLevel()){
                return true;
            }
        }
        return false;

    }
    //判断是是否存在某个状态
    public boolean IsHaveState(String stateId)
    {
        if (MyUtil.isNullOrEmpty(stateId))
        {
            return false;
        }


        for (int i = 0; i < stateSkillEffectList.size(); i++)
        {
            if (stateSkillEffectList.get(i).getBuffDataVo().getBuff_id() == Integer.parseInt(stateId))
            {
                return true;
            }
        }

        return false;

    }
    public boolean IsHaveState(String[] stateIdArr)
    {
        if (stateIdArr == null) return false;
        for (int i = 0; i < stateIdArr.length; i++)
        {
            if (IsHaveState(stateIdArr[i]))
            {
                return true;
            }
        }

        return false;
    }
    
    
	public boolean isFirstSkill() {
		return isFirstSkill;
	}
	public void setFirstSkill(boolean isFirstSkill) {
		this.isFirstSkill = isFirstSkill;
	}
	public long getWuHitValue() {
		return wuHitValue;
	}
	public void setWuHitValue(long wuHitValue) {
		this.wuHitValue = wuHitValue;
	}
	public int getHitValue() {
		return hitValue;
	}
	public void setHitValue(int hitValue) {
		this.hitValue = hitValue;
	}
	public int getRandomIndex() {
		return randomIndex;
	}
	public void setRandomIndex(int randomIndex) {
		this.randomIndex = randomIndex;
	}
	public JSONArray getRandomList() {
		return randomList;
	}
	public void setRandomList(JSONArray randomList) {
		this.randomList = randomList;
	}
	public JSONArray getChildEffectListJA() {
		return childEffectListJA;
	}
	public void setChildEffectListJA(JSONArray childEffectListJA) {
		this.childEffectListJA = childEffectListJA;
	}
	public JSONArray getStateSkillEffectListJA() {
		return stateSkillEffectListJA;
	}
	public void setStateSkillEffectListJA(JSONArray stateSkillEffectListJA) {
		this.stateSkillEffectListJA = stateSkillEffectListJA;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	//玩家身上的数值
    public AttributeValue attributeValue;
    
	public List<Child_Effect> getChildEffectList() {
		return childEffectList;
	}
	public void setChildEffectList(List<Child_Effect> childEffectList) {
		this.childEffectList = childEffectList;
	}
	public AttributeValue getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}
	public List<SkillUseRecord> getSkillUseRecord() {
		return skillUseRecord;
	}
	public void setSkillUseRecord(List<SkillUseRecord> skillUseRecord) {
		this.skillUseRecord = skillUseRecord;
	}
	public Map<String, SkillDataVo> getStudySkills() {
		return studySkills;
	}
	public void setStudySkills(Map<String, SkillDataVo> studySkills) {
		this.studySkills = studySkills;
	}
	public Elve getElve() {
		return elve;
	}
	public void setElve(Elve elve) {
		this.elve = elve;
	}
	public Elves_value getElves_value() {
		return elves_value;
	}
	public void setElves_value(Elves_value elves_value) {
		this.elves_value = elves_value;
	}

	public List<BuffInforVo> getStateSkillEffectList() {
		return stateSkillEffectList;
	}

	public void setStateSkillEffectList(List<BuffInforVo> stateSkillEffectList) {
		this.stateSkillEffectList = stateSkillEffectList;
	}

	public void setEnablechildEffectList(List<Child_Effect> enablechildEffectList) {
		this.enablechildEffectList = enablechildEffectList;
	}
	@Override
	public String toString() {
		return "RoleElves [elve=" + elve + ", elves_value=" + elves_value + "]";
	}
	
	
}
