package pocket.pvp.skill_species;

public class Species_Factory {
	private static Species_Factory instance;
	public static Species_Factory getInstance(){
		if(instance == null)
			instance = new Species_Factory();
		return instance;
	}
	
	public Species_Factory() {

	}
	
	public Species create(int skill_id, String skill_name, String species){
		switch (species) {
		case "一击必杀":
			return new Species_dieHit(skill_id, skill_name, species);
		case "多次技能":
			return new Species_multipleHit(skill_id, skill_name, species);
		case "多回合技能":
			return new Species_multipleRoundHit(skill_id, skill_name, species);
		case "蓄气技能":
			return new Species_powerHit(skill_id, skill_name, species);
		case "硬直技能":	
			return new Species_yingZhiHit(skill_id, skill_name, species);
		case "蓄气反击":	
			return new Species_powerBackHit(skill_id, skill_name, species);
		default:
			//默认普通技能
			return new Species_normal(skill_id, skill_name, species);
		}
	}
}
