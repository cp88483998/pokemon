package pocket.chat.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;

import pocket.chat.dao.MongoAddMsgDao;
import pocket.chat.dao.MongoAddMsgDaoImpl;

public class PublicService {
	/*
	 * 从数据库中找出uid对应的user信息
	 */
	public static String findUserInfo(int roomId, long uid, String additionalMsg){
		MongoAddMsgDao dao = MongoAddMsgDaoImpl.getInstance();
		
		Document document = dao.findOneUserInfo(uid, roomId);
		
		String username = document.getString("username");
		String nickname = document.getString("nickname");
		long level = Long.parseLong(document.get("level").toString());
		int vip = document.getInteger("vip");
		int elves_head_id = document.getInteger("elves_head_id");
		int trainer_head_id = document.getInteger("trainer_head_id");
		int gender = document.getInteger("gender");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm:ss");
		String time = sdf.format(date);
		int pokedex;
		
		String[] msgs = additionalMsg.split(",");
		msgs[2] = level+"";
		msgs[3] = vip+"";
		msgs[8] = time;
		StringBuffer result = new StringBuffer();
		for(String str : msgs){
			result.append(str+",");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
}
