package pocket.total.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StaticClass {
	public final static JSONArray elves_value_js; 
	public final static JSONArray skill_data_js;
	public final static JSONObject player_data2_js;
	public final static JSONObject player_data_js;
	public final static JSONArray prop_effect_js;
	public final static JSONArray character_str_js;
	public final static JSONArray features_effect_js;
	public final static JSONArray skill_effect_js;
	public final static JSONArray pvp_robot_js;
	public final static JSONArray pvp_ai_js;
	public final static JSONArray elves_value_ai_js;
	public final static JSONObject player_namedate_js;
	public final static JSONArray server_name_js;
	public final static JSONArray Elves_data_js;
	public final static List<String> robotNames;
	public final static JSONArray Ball_catch_js;
	
	//测试
	public final static JSONObject CatchElevs_js; 
	
	static{
		
		
		String elves_value_str = ReadFile("resource/elves_value.json");
		elves_value_js = JSONArray.fromObject(elves_value_str);
		
		String skill_data_str = ReadFile("resource/skill_data.json");
		skill_data_js = JSONArray.fromObject(skill_data_str);
//		
		String player_data2_str = ReadFile("resource/player_data2.json");
		player_data2_js = JSONObject.fromObject(player_data2_str);
//		
		String player_data_str = ReadFile("resource/player_data.json");
		player_data_js = JSONObject.fromObject(player_data_str);
		
		String prop_effect_str = ReadFile("resource/prop_effect.json");
		prop_effect_js = JSONArray.fromObject(prop_effect_str);
		
		String character_str = ReadFile("resource/character.json");
		character_str_js = JSONArray.fromObject(character_str);
		
		String features_effect_str = ReadFile("resource/features_effect.json");
		features_effect_js = JSONArray.fromObject(features_effect_str);
		
		String skill_effect_str = ReadFile("resource/skill_effect.json");
		skill_effect_js = JSONArray.fromObject(skill_effect_str);
		
		String pvp_robot_str = ReadFile("resource/pvp_robot.json");
		pvp_robot_js = JSONArray.fromObject(pvp_robot_str);
		
		String pvp_ai_str = ReadFile("resource/pvp_ai.json");
		pvp_ai_js = JSONArray.fromObject(pvp_ai_str);
		
		String elves_value_ai_str = ReadFile("resource/elves_value_ai.json");
		elves_value_ai_js = JSONArray.fromObject(elves_value_ai_str);
		
		String player_namedate_str = ReadFile("resource/player_namedate.json");
		player_namedate_js = JSONObject.fromObject(player_namedate_str);
		
		String server_name_str = ReadFile("resource/server_name.json");
		server_name_js = JSONArray.fromObject(server_name_str);
		
		String elves_data_str = ReadFile("resource/Elves_data.json");
		Elves_data_js = JSONArray.fromObject(elves_data_str);
		
		robotNames = ReadTxtFile("resource/RobotName.txt");
		
		String ball_catch_str = ReadFile("resource/ball_catch.json");
		Ball_catch_js = JSONArray.fromObject(ball_catch_str);
		
		//测试
		String CatchElevs_str = ReadFile("resource/CatchElevs.json");
		CatchElevs_js = JSONObject.fromObject(CatchElevs_str);
	}
	public static String ReadFile(String path){
		BufferedReader reader = null;
		String laststr = "";
//		System.out.println("ClassLoader.getSystemResource() :"+ClassLoader.getSystemResource(""));
		try{
			InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
//			FileInputStream fileInputStream = new FileInputStream(path);
			InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				laststr += tempString;
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
	public static List<String> ReadTxtFile(String path){
		BufferedReader reader = null;
		List<String> robotNames = new ArrayList<String>();
//		System.out.println("ClassLoader.getSystemResource() :"+ClassLoader.getSystemResource(""));
		try{
			InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
//			FileInputStream fileInputStream = new FileInputStream(path);
			InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				robotNames.add(tempString);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return robotNames;
	}
}
