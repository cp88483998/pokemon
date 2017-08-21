package pocket.total.controller;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import net.sf.json.JSONObject;
import pocket.pvp.entity.Elve;

public class CatchElevsEventHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String infor = params.getUtfString("infor");
		JSONObject jo = JSONObject.fromObject(infor);
		String itemID = jo.getString("itemID");
		int maxHp = jo.getInt("maxHp");
		String buffId = jo.getString("buffId");
		JSONObject elveJo = jo.getJSONObject("elvesDataInforVo");
		//1.验证战斗数据
		
		//2.计算抓捕机率
		Elve elve = new Elve();
	    elve.seteLvesID(Integer.parseInt(elveJo.getString("eLvesID")));
	    elve.setRarity(elveJo.getInt("rarity"));
	    elve.seteLvesname(elveJo.getString("eLvesname"));
	    elve.setCharacter(elveJo.getInt("character"));
		elve.setExp(elveJo.getInt("exp"));
	    elve.setHp(elveJo.getInt("hp"));
	    elve.setSameIndex(elveJo.getInt("sameIndex"));
		elve.setHpLevel(elveJo.getInt("hpLevel"));
	    elve.setWuAttackLevel(elveJo.getInt("wuAttackLevel"));
	    elve.setWuDefenseLevel(elveJo.getInt("wuDefenseLevel"));
	    elve.setTeAttackLevel(elveJo.getInt("teAttackLevel"));
	    elve.setTeDefenseLevel(elveJo.getInt("teDefenseLevel"));
	    elve.setSpeedLevel(elveJo.getInt("speedLevel"));
	    if(!elveJo.getString("carryitem").isEmpty()){
	    	elve.setCarryitem(elveJo.getString("carryitem"));
	    }
		if(!elveJo.getString("carryequip").isEmpty()){
			elve.setCarryequip(elveJo.getString("carryequip"));
		}
		if(!elveJo.getString("features").isEmpty()){
			elve.setFeatures(elveJo.getString("features"));
		}
		elve.setTrainedLevel(elveJo.getInt("trainedLevel"));
		elve.setBreakthroughLevel(elveJo.getInt("breakthroughLevel"));
		elve.setLevel(elveJo.getInt("level"));
		elve.setUpgradeLv(elveJo.getInt("upgradeLv"));
		elve.setStudySkillList(elveJo.getString("studySkillList"));
		elve.setGender(elveJo.getInt("gender"));
		elve.setMega(elveJo.getString("mega"));
		if(!elveJo.getString("own").isEmpty()){
			elve.setOwn(elveJo.getString("own"));
		}
	}

}
