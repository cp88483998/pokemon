package pocket.total.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.pvp.entity.RoleElves;

public class BallCatchUtil {
	/**
	 * B = {（（3*最大HP-2*当前HP）*捕捉率*捕捉修正）/（3*最大HP）}*状态修正
	 * 捕获修正：精灵球的捕获修正
	 * 状态修正：buff 冰冻、睡眠×2；中毒、灼伤、麻痹：×1.5
	 * B在255以上时，捕获必定成功；B小于255时，进入下面的判定。
	 * @return b
	 */
	public static double FirstJudge(RoleElves roleElves, String item_id, int buffId){
		//最大血值
		int maxHp = roleElves.getMaxHp();
		//当前血值
		int nowHp = roleElves.getElve().getHp();
		//抓捕率，Elves_data配置文件中的catch_rate
		double elve_catchRate = 1;
		JSONArray elves_data_js = StaticClass.Elves_data_js;
		JSONObject jo = null;
		String elveId = roleElves.getElve().geteLvesID()+"";
		for(int i=0;i<elves_data_js.size();i++){
			jo = elves_data_js.getJSONObject(i);
			if(jo.getString("Elves_ID").equals(elveId)){
				elve_catchRate = jo.getDouble("catch_rate");
				break;
			}
		}
		//捕获修正,ball_catch配置文件中的catch_rate
		double ball_catchRate = 1;
		JSONArray ball_catch_js = StaticClass.Ball_catch_js;
		for(int i=0;i<ball_catch_js.size();i++){
			jo = ball_catch_js.getJSONObject(i);
			if(jo.getString("item_id").equals(item_id)){
				ball_catchRate = jo.getDouble("catch_rate");
				break;
			}
		}
		//状态修正
		double status = 1;
		if(buffId == 202 || buffId == 205){
			status = 2;
		}
		if(buffId == 200 || buffId == 201 || buffId == 203 || buffId == 204){
			status = 1.5;
		}
		double b = (((3*maxHp - 2*nowHp)
				*elve_catchRate*ball_catchRate)
				/(3*maxHp))*status;
		
		
		return b;
	}
	
	/**
	 * G = 65536 / (255/B)^(1/4)
	 * 此时在0000～FFFF之间产生4个随机数。
	 * 依次检查每一个随机数，如果每一个数都小于G，那么捕获成功。
	 * 如果有数字大于等于G，那么捕获失败。
	 * （精灵球摇动的次数）=（小于G的随机数的个数），如果4个数均大于等于G，则球不摇动，直接捕获失败。
	 * @return g
	 */
	public static double SecondJudge(double b){
		double g = Math.pow(65536/(255/b), (1/4));//Math.pow()：次方
		return g;
	}
}
