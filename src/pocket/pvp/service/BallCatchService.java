package pocket.pvp.service;

import net.sf.json.JSONObject;
import pocket.pvp.entity.RoleElves;
import pocket.total.util.BallCatchUtil;
import pocket.total.util.MyUtil;

public class BallCatchService {
	public static JSONObject BallCatch(){
		JSONObject jo = new JSONObject();
		RoleElves roleElves = null;
		String ballId = null;
		int buffId = 0;
		
		double b = BallCatchUtil.FirstJudge(roleElves, ballId, buffId);
		double g;
		boolean isCatchSuc = true;
		int count = 0;//小于G的随机数的个数（精灵球摇动的次数）
		
		//B在255以上时，捕获必定成功；B小于255时，进入下面的判定。
		if(b < 255){
			
			g = BallCatchUtil.SecondJudge(b);
			int[] arr = MyUtil.get4Random(65536);
			
			//依次检查每一个随机数，如果每一个数都小于G，那么捕获成功。
			//（精灵球摇动的次数）=（小于G的随机数的个数），如果4个数均大于等于G，则球不摇动，直接捕获失败。
			for(int i : arr){
				if(i > g){
					isCatchSuc =  false;
					count ++;
				}
			}
		}
		jo.put("isCatchSuc", isCatchSuc);
		jo.put("count", count);
		return jo;
	}
}
