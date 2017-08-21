package test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pocket.timeSpace.entity.Boss;
import pocket.total.entity.Share;
import pocket.total.util.Ability;
import pocket.total.util.BossUtil;
import pocket.total.util.StaticClass;

public class BossTest {
	
//	@Test
//	public void resultDataTest(){
//		JSONObject jo = StaticClass.result_data_js;
//		jo.put("uid", "14758964587860");
//		System.out.println(jo.toString());
//		String result = BossUtil.verifyBossData(jo.toString());
//		System.out.println(result);
//	}
	@Test
	public void bossHpTest(){
		int HP = 80;//种族值
		double trValue = Ability.trainValue_Com(5);//特训值
		double btValue = Ability.btVlaue_Com(14);//突破值
		int hpLv = 70;//个体值等级
		int lv = 70;//等级
		double HP_Ability = 24*((HP*trValue+btValue+hpLv/2+100)*lv/100+10);
//		System.out.println(elve.geteLvesID()+" HP能力值："+HP+"*"+trValue+"+"+btValue+"+"+hpLv+"/2+100)*"+lv+"/100+10"+"="+HP_Ability);
		System.out.println((int)Math.round(HP_Ability*1000));//四舍五入
	}
	
	@Test
	public void bossTest(){
//		JSONArray ja = StaticClass.boss_data_js;
		JSONArray ja = new JSONArray();
		JSONObject jo = null;
		int HP = 0;
		double trValue = 0;
		double btValue = 0;
		int hpLevel = 0;
		int lv = 0;
		double HP_Ability = 0;
		String eLvesid = null;
		long hp = 0;
		Map<String, Long> map = new LinkedHashMap<String, Long>();
		for(int i=0;i<ja.size();i++){
			jo = ja.getJSONObject(i);
			HP = jo.getInt("HP");
			trValue = Ability.trainValue_Com(jo.getInt("trainedLevel"));
			btValue = Ability.btVlaue_Com(jo.getInt("breakthroughLevel"));
			hpLevel = jo.getInt("hpLevel");
			lv = jo.getInt("level");
			HP_Ability = 24*((HP*trValue+btValue+hpLevel/2+100)*lv/100+10);
			eLvesid = jo.getInt("eLvesid")+"";
//			System.out.println("eLvesid :"+eLvesid);
			hp = Math.round(HP_Ability*1000);
//			System.out.println("hp :"+hp);
			map.put(eLvesid, hp);
		}
		System.out.println(map.size());
		
	}
	
	@Test
	public void bossTest2(){
		Map<String, Boss> bossMap = Share.bossMap;
		List<Boss> list = new ArrayList<>();
		for(String key:bossMap.keySet()){
			list.add(bossMap.get(key));
		}
		JSONArray ja2 = JSONArray.fromObject(list);
		System.out.println(ja2);
	}
}
