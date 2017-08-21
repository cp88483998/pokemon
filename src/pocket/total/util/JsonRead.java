package pocket.total.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.Elves_value;
import pocket.pvp.entity.SkillDataVo;
import pocket.pvp.entity.SkillEffectVo;


public class JsonRead {
	
	public static String getSpecies(int skill_id){
		String species = null;
		for(int i=0;i<StaticClass.skill_data_js.size();i++){
			JSONObject jo = StaticClass.skill_data_js.getJSONObject(i);
			if(jo.getInt("Skill_id") == skill_id){
				species = jo.getString("species");
			}
		}
		return species;
	}
	
	public static SkillEffectVo skillEffectFind(int SkillId, JSONArray jsonArray){
		SkillEffectVo skillEffectVo = new SkillEffectVo();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jo = jsonArray.getJSONObject(i);
			if(Integer.parseInt(jo.getString("Skill_id")) == SkillId){
				skillEffectVo.setSkill_name(jo.getString("Skill_name"));
				skillEffectVo.setGet_buff(jo.getString("get_buff"));
				skillEffectVo.setBuff_rate(jo.getString("buff_rate"));
				skillEffectVo.setGet_time(getInt(jo.getString("get_time")));
				skillEffectVo.setTake_time(jo.getString("take_time"));
				skillEffectVo.setTarget(jo.getString("target"));
				skillEffectVo.setEffect(jo.getString("effect"));
				skillEffectVo.setValue1(jo.getString("value1"));
				skillEffectVo.setRate(jo.getString("Rate"));
				skillEffectVo.setText(jo.getString("text"));
				skillEffectVo.setTake(jo.getString("take"));
				skillEffectVo.setValue(jo.getString("value"));
			}
		}
		return skillEffectVo;
	}
	
	
//	public Player getPlayer(){
////		String JsonContext = ReadFile("resource/player_data2.json");
////		JSONObject jsonObject = JSONObject.fromObject(JsonContext);
////		System.out.println(jsonObject);
//		JsonServiceImpl jsonService = new JsonServiceImpl();
//		Player player = jsonService.parsePlayer(StaticClass.player_data2_js);
//		
//		return player;
//	}
	
	public static SkillDataVo skillFind(int skill_id, JSONArray jsonArray){
		int size = jsonArray.size();
		SkillDataVo skill = new SkillDataVo();
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.getInt("Skill_id") == skill_id){
				skill.setSkill_id(jsonObject.getInt("Skill_id"));
				skill.setATK_time(jsonObject.get("ATK_time").toString());
				skill.setATK_tpye(jsonObject.getString("ATK_tpye"));
				skill.setDeath(jsonObject.getInt("Death"));
				skill.setHit(jsonObject.getInt("Hit"));
				skill.setPP(jsonObject.getInt("PP"));
				skill.setPriority(getInt(jsonObject.get("Priority")));
				skill.setRatio(jsonObject.getString("Ratio"));
				skill.setSkill_ability(jsonObject.getString("Skill_ability"));
				skill.setSkill_name(jsonObject.getString("Skill_name"));
				skill.setSkill_number(jsonObject.getInt("Skill_number"));
				skill.setSkill_power(jsonObject.getInt("Skill_power"));
				skill.setSkill_text(jsonObject.getString("Skill_text"));
				skill.setSpecies(jsonObject.getString("species"));
				skill.setTarget(jsonObject.getInt("Target"));
//				skill.setTarget_number(jsonObject.getInt("Target_number"));
				skill.setTarget_number(getInt(jsonObject.get("Target_number")));
				skill.setSkill_type(jsonObject.getString("Type"));
//				skill.setVital(jsonObject.getInt("Vital"));
				skill.setVital(getInt(jsonObject.get("Vital")));
			}
		}
		return skill;
	}
	
	public static SkillDataVo skillFind2(int skill_id){
//		String JsonContext = ReadFile("resource/skill_data.json");
//		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		int size = StaticClass.skill_data_js.size();
		SkillDataVo skill = new SkillDataVo();
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject = StaticClass.skill_data_js.getJSONObject(i);
			if(jsonObject.getInt("Skill_id") == skill_id){
				skill.setSkill_id(jsonObject.getInt("Skill_id"));
				skill.setATK_time(jsonObject.get("ATK_time").toString());
				skill.setATK_tpye(jsonObject.getString("ATK_tpye"));
				skill.setDeath(jsonObject.getInt("Death"));
				skill.setHit(jsonObject.getInt("Hit"));
				skill.setPP(jsonObject.getInt("PP"));
				skill.setPriority(jsonObject.getInt("Priority"));
				skill.setRatio(jsonObject.getString("Ratio"));
				skill.setSkill_ability(jsonObject.getString("Skill_ability"));
				skill.setSkill_name(jsonObject.getString("Skill_name"));
				skill.setSkill_number(jsonObject.getInt("Skill_number"));
				skill.setSkill_power(jsonObject.getInt("Skill_power"));
				skill.setSkill_text(jsonObject.getString("Skill_text"));
				skill.setSpecies(jsonObject.getString("species"));
				skill.setTarget(jsonObject.getInt("Target"));
				skill.setTarget_number(jsonObject.getInt("Target_number"));
				skill.setSkill_type(jsonObject.getString("Type"));
				skill.setVital(jsonObject.getInt("Vital"));
			}
		}
		return skill;
	}
	
	public static Elves_value ElvesValueFind(int elvesID, JSONArray jsonArray){
//		String JsonContext = ReadFile("resource/elves_value.json");
//		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		
		Elves_value elves_value = new Elves_value();
		for(int  i = 0; i < jsonArray.size(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
//			System.out.println(jsonObject);
//			System.out.println(jsonObject.get("Skill_id"));
			if(jsonObject.getInt("valueID") == elvesID){
				elves_value.setValueID(elvesID);
				elves_value.setHP(jsonObject.getInt("HP"));
				elves_value.setATK(jsonObject.getInt("ATK"));
				elves_value.setDEF(jsonObject.getInt("DEF"));
				elves_value.setINT(jsonObject.getInt("INT"));
				elves_value.setMDF(jsonObject.getInt("MDF"));
				elves_value.setACT(jsonObject.getInt("ACT"));
				elves_value.setTotal_value(jsonObject.getInt("total_value"));
			}
		}
		return elves_value;
	}
	
//	public Map<Integer, Elve> changeElve(){
//		String JsonContext = ReadFile("resource/change_elves.json");
//		JSONObject jsonObject = JSONObject.fromObject(JsonContext);
//		JSONArray jaElves = StaticClass.change_elves_js.getJSONArray("elves");
//		System.out.println("jaElves :"+jaElves);
//		JsonService jsonService = new JsonServiceImpl();
//		Map<Integer, Elve> elves = jsonService.parseElve(jaElves);
//		return elves;
//	}
	
//	public String ReadFile(String path){
//		BufferedReader reader = null;
//		String laststr = "";
//		System.out.println("ClassLoader.getSystemResource() :"+ClassLoader.getSystemResource(""));
//		try{
//			InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
////			FileInputStream fileInputStream = new FileInputStream(path);
//			InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
//			reader = new BufferedReader(inputStreamReader);
//			String tempString = null;
//			while((tempString = reader.readLine()) != null){
//				laststr += tempString;
//			}
//			reader.close();
//		}catch(IOException e){
//			e.printStackTrace();
//		}finally{
//			if(reader != null){
//			try {
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				}
//			}
//		}
//		return laststr;
//	}
	
	private static int getInt(Object obj){
		int i = 0;
		if(obj instanceof Integer){
			i = Integer.parseInt(obj.toString());
		}
		return i;
	}
}
