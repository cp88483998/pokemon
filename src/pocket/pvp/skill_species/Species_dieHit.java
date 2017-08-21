package pocket.pvp.skill_species;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;
import pocket.pvp.entity.SkillDataVo;
import scala.util.Random;
/**
 * 必杀
 * @author 陈鹏
 */
public class Species_dieHit extends Species{

	public Species_dieHit(int _skill_id, String _skill_name, String _species) {
		super(_skill_id, _skill_name, _species);
	}

	@Override
	public boolean hitDie(Elve _attackElvesDataInforVo, Elve _beAttackElvesDataInforVo) {
		boolean isBiSha = false;
		//若防御方的等级大于攻击方的等级，必定失败。
        if (_beAttackElvesDataInforVo.getLevel() > _attackElvesDataInforVo.getLevel()){
        	isBiSha = false;
        }

        //产生一个1～100之间的随机数，
        Random ran = new Random();
        int i = ran.nextInt()*100+1;
        
        //否则，A=（30+自身等级-对方等级）%
        //该随机数小于A时视为命中，否则为失误。
        if ((_attackElvesDataInforVo.getLevel() - _beAttackElvesDataInforVo.getLevel() + 30)/100f >i){
        	isBiSha = true;
        }
        super.setBiSha(isBiSha);
        return isBiSha;
	}

	@Override
	public JSONObject getUseSkill(SkillDataVo skill) {
		JSONObject joUseSkill = new JSONObject();
		JSONObject jo = new JSONObject();
		
		joUseSkill.put("name", skill.getSpecies());
		joUseSkill.put("skillId", String.valueOf(skill.getSkill_id()));
		joUseSkill.put("isBiSha", super.isBiSha());
		jo.put("BaseSkill", joUseSkill);
		
		return jo;
	}
	

}
