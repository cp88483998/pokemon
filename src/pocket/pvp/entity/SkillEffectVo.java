package pocket.pvp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pocket.total.util.MyUtil;

/**
 * 技能子效果信息
 * @author 陈鹏
 */
public class SkillEffectVo {
	private String Skill_id;

	private String Skill_name;

    private String effect_name;

    //添加时机
    private int get_time;

    //特殊条件
    private String take;

    //对应值
    private String value;
    
    //效果值
    private String value1;

    //生效时机
    private String take_time;

    //作用目标
    private String target;

    //产生效果
    private String effect;

    //效果概率
    private String Rate;

    private String get_buff;
    
    //每个buff对应一个施加概率
    private String buff_rate;
    
    private List<Double> stateRateList;

    //保存技能触发的状态（包括异常状态与属性状态）
    private List<String> stateList;

    //效果触发台词
    private String text;

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getSkill_id() {
		return Skill_id;
	}

	public void setSkill_id(String skill_id) {
		Skill_id = skill_id;
	}

	public String getSkill_name() {
		return Skill_name;
	}

	public void setSkill_name(String skill_name) {
		Skill_name = skill_name;
	}

	public String getEffect_name() {
		return effect_name;
	}

	public void setEffect_name(String effect_name) {
		this.effect_name = effect_name;
	}

	public int getGet_time() {
		return get_time;
	}

	public void setGet_time(int get_time) {
		this.get_time = get_time;
	}

	public String getTake() {
		return take;
	}

	public void setTake(String take) {
		this.take = take;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTake_time() {
		return take_time;
	}

	public void setTake_time(String take_time) {
		this.take_time = take_time;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getRate() {
		return Rate;
	}

	public void setRate(String rate) {
		Rate = rate;
	}

	public String getGet_buff() {
		return get_buff;
	}

	public void setGet_buff(String get_buff) {
		if(MyUtil.isNullOrEmpty(get_buff)){

		}else{
			String[] arr = get_buff.split(",");
			stateList = new ArrayList<String>();
			 for (int i = 0; i < arr.length; i++){
                 if (arr[i].indexOf(':') != -1){
                     //存在多个，随机一个
                     String[] tmp = arr[i].split(":");
                     Random ran = new Random();
                     stateList.add(tmp[ran.nextInt(tmp.length)]);
                 }else{
                     stateList.add(arr[i]);
                 }
             }
		}
		this.get_buff = get_buff;
	}

	public String getBuff_rate() {
		return buff_rate;
	}

	public void setBuff_rate(String buff_rate) {
		if (MyUtil.isNullOrEmpty(buff_rate)){
			
        }else{
            String[] arr = buff_rate.split(";");
            stateRateList = new ArrayList<Double>();
            for (int i = 0; i < arr.length; i++){
                stateRateList.add(Double.parseDouble(arr[i]));
            }
        }
		this.buff_rate = buff_rate;
	}

	public List<Double> getStateRateList() {
		return stateRateList;
	}

	public void setStateRateList(List<Double> stateRateList) {
		this.stateRateList = stateRateList;
	}

	public List<String> getStateList() {
		return stateList;
	}

	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
