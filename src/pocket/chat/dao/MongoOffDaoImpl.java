package pocket.chat.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import pocket.chat.entity.OfflineMsg;
import pocket.total.util.MongoDBUtil;

public class MongoOffDaoImpl implements MongoOffDao{
	
	private static final MongoOffDaoImpl instance = new MongoOffDaoImpl();// 饿汉式单例模式
	
	public static MongoOffDaoImpl getInstance(){
		return instance;
	}

	public MongoOffDaoImpl(){
		MongoDBUtil utilInstance = MongoDBUtil.getInstance();
		collection = utilInstance.getOffCollection();
	}
	
	private static MongoCollection<Document> collection;
	
	/**
	 * 文档数量
	 * @return
	 */
	public long count(){
		return collection.count();
	}
	
	public FindIterable<Document> findAll(){
		System.out.println("-----------------start findAll");
		FindIterable<Document> iterable = collection.find();
		return iterable;
	}
	
	/**
	 * 插入一条离线消息
	 * @param jo
	 * @return
	 */
	public boolean insertOne(Document document){
		boolean result = true;
		try {
			List<OfflineMsg> list = findOneGroup(document.getString("senderUid"), document.getString("recipientUid"));
			if(list.size() < 20){
				
			}else{
				deleteOneMsg(document.getString("senderUid"), document.getString("recipientUid"));
			}
			collection.insertOne(document);
			System.out.println("----------------insertOne ok");
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * 找出该玩家所有离线对话信息
	 * @param key
	 * @param value
	 * @return
	 */
	public List<OfflineMsg> findOneAll(String key,Object value){
		FindIterable<Document> documents =  collection.find(new Document(key, value));
		List<OfflineMsg> offlineMsgs = new ArrayList<OfflineMsg>();
		for(Document document : documents){
			OfflineMsg offlineMsg = new OfflineMsg();
			offlineMsg.setSenderUid(document.getString("senderUid"));
			offlineMsg.setRecipientUid(document.getString("recipientUid"));
			offlineMsg.setContext(document.getString("context"));
			offlineMsg.setAdditionalMsg(document.getString("additionalMsg"));
			offlineMsgs.add(offlineMsg);
		}
		return offlineMsgs;
	}
	
	/**
	 * 查找玩家多少组离线对话
	 * @param key
	 * @param value
	 * @return
	 */
	public Set<String> findMany(String key,Object value){
		FindIterable<Document> documents =  collection.find(new Document(key, value));
		Set<String> senderUids = new HashSet<String>();
		for(Document document : documents){
			senderUids.add(document.getString("senderUid"));
		}
		return senderUids;
	}
	
	/**
	 * 找出一组离线对话消息
	 * @param senderUid
	 * @param recipientUid
	 * @return
	 */
	public List<OfflineMsg> findOneGroup(String senderUid,String recipientUid){
		BasicDBObject doc = new BasicDBObject();
		doc.put("senderUid", senderUid);
		doc.put("recipientUid", recipientUid);
		FindIterable<Document> documents =  collection.find(doc);
		List<OfflineMsg> offlineMsgs = new ArrayList<OfflineMsg>();
		OfflineMsg offlineMsg = new OfflineMsg();
		for(Document document : documents){
			offlineMsg.setSenderUid(senderUid);
			offlineMsg.setRecipientUid(recipientUid);
			offlineMsg.setContext(document.getString("context"));
			offlineMsg.setAdditionalMsg(document.getString("additionalMsg"));
			offlineMsgs.add(offlineMsg);
		}
		return offlineMsgs;
	}
	
	/**
	 * 删除一组离线消息
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteResult deleteOneGroup(String senderUid, String recipientUid){
		BasicDBObject doc = new BasicDBObject();
		doc.put("senderUid", senderUid);
		doc.put("recipientUid", recipientUid);
		DeleteResult result = collection.deleteMany(doc);

		return result;
	}
	
	/**
	 * 删除一条离线消息
	 */
	public DeleteResult deleteOneMsg(String senderUid, String recipientUid){
		BasicDBObject doc = new BasicDBObject();
		doc.put("senderUid", senderUid);
		doc.put("recipientUid", recipientUid);
		DeleteResult result = collection.deleteOne(doc);
		
		return result;
	}

	/**
	 * 删除该玩家所有离线对话消息
	 * @param key
	 * @param value
	 * @return 删除条数
	 */
	public DeleteResult deleteAll(String key, Object value) {
		
		DeleteResult result = collection.deleteMany(new Document("recipientUid", value));
		
		return result;
	}
}
