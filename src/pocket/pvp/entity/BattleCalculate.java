package pocket.pvp.entity;

import pocket.total.util.MyUtil;

public class BattleCalculate {
	//是不是一次性存在的
	public static boolean GetChildEffectIsOne(String childEffectId){

        if (MyUtil.isNullOrEmpty(childEffectId)){
            return false;
        }

        int id = MyUtil.IntParse(childEffectId);
        if (id >= 110 && id <= 117){//属性改变
            return false;
        }
        return true;

    }
}
