package pocket.chat.dao;

import org.bson.Document;

public interface MongoAddMsgDao {
	public Document findOneUserInfo(long uid, int roomId);
}
