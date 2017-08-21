package pocket.total.util;

import pocket.pvp.entity.Elve;

public class ElveId {
	public static String addElveId(Elve elve){
		String elvesId = elve.geteLvesID()+"_"+elve.getGender()+"_"+elve.getCharacter()+"_"+elve.getSameIndex();
		return elvesId;
	}
}
