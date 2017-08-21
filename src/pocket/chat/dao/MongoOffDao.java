package pocket.chat.dao;

import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;

import net.sf.json.JSONObject;
import pocket.chat.entity.OfflineMsg;

public interface MongoOffDao {
	
	/**
	 * 文档数量
	 * @return
	 */
	public long count();
	
	public FindIterable<Document> findAll();
	
	/**
	 * 插入一条离线消息
	 * @param jo
	 * @return
	 */
	public boolean insertOne(Document document);
	
	/**
	 * 查找玩家多少离线对话
	 * @param key
	 * @param value
	 * @return
	 */
	public Set<String> findMany(String key,Object value);
	
	/**
	 * 找出一组离线对话消息
	 * @param senderUid
	 * @param recipientUid
	 * @return
	 */
	public List<OfflineMsg> findOneGroup(String senderUid,String recipientUid);
	
	/**
	 * 删除一组离线消息
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteResult deleteOneGroup(String senderUid, String recipientUid);
	
	/**
	 * 删除一条离线消息
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteResult deleteOneMsg(String senderUid, String recipientUid);
	
	/**
	 * 找出该玩家所有离线对话消息
	 * @param key
	 * @param value
	 * @return
	 */
	public List<OfflineMsg> findOneAll(String key,Object value);
	
	/**
	 * 删除该玩家所有离线对话消息
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteResult deleteAll(String key, Object value);
	
}
