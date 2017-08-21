package test;

import org.junit.Test;

import net.sf.json.JSONObject;
import pocket.total.util.MyUtil;
import pocket.total.util.StaticClass;

public class CatchTest {
	@Test
	public void elveTest(){
		JSONObject jo = StaticClass.CatchElevs_js;
		JSONObject elveJo = jo.getJSONObject("elvesDataInforVo");
		String catchItemId = elveJo.getString("catchItemId");
		System.out.println(catchItemId .isEmpty());
	}
	@Test
	public void mathPow(){
		double a = Math.pow(2, 3);
		System.out.println(a);
	}
	@Test
	public void getRandom(){
		int[] arr = MyUtil.get4Random(65536);
	}
}
