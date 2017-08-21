package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.SkillDataVo;
import pocket.total.util.MyUtil;
import scala.util.Random;

/**
 * 一回合多次
 * @author 陈鹏
 */
public class Species_multipleHit extends Species{

	public Species_multipleHit(int _skill_id, String _skill_name, String _species) {
		super(_skill_id, _skill_name, _species);
	}

	@Override
	public int count(SkillDataVo _battleSkillData) {
		String _hitCount = _battleSkillData.getATK_time();
		int hitCount;
        if (MyUtil.isNullOrEmpty(_hitCount)){
            hitCount = 1;
        }else{
            int[] arr = MyUtil.ArrayToInt(_hitCount.split(","));
            if (arr.length == 1){
                hitCount = arr[0];
            }else{
                if (arr[0] > arr[1]){
                    hitCount = 1;

                }else{
                	Random random = new Random();
                	int i = random.nextInt(2);
                	if(i==0){
                		hitCount = arr[0];
                	}else{
                		hitCount = arr[1];
                	}
                    
                }
            }
        }
        super.setHitCount(hitCount);
		return hitCount;
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		joUseSkill.put("hitCount", super.getHitCount());
		jo.put("BaseSkill", joUseSkill);
		return jo;
	}
	
	

}
