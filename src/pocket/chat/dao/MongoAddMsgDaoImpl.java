package pocket.chat.dao;

import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import pocket.total.entity.Share;
import pocket.total.util.MongoDBUtil;
/**
 * 替换additionalMsg，防作弊
 * <p>Title: MongoAddMsgDaoImpl<／p>
 * <p>Description: <／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月10日
 */
public class MongoAddMsgDaoImpl implements MongoAddMsgDao{

	
//	private static MongoCollection<Document> userCollection;
	private static List<MongoDatabase> mongoDBs;
	private static Properties p = Share.p;
	private static MongoAddMsgDaoImpl instance = new MongoAddMsgDaoImpl();
	
	public static MongoAddMsgDaoImpl getInstance(){
//		if(instance == null){
//			instance = new MongoAddMsgDaoImpl();
//		}
		return instance;
	}
	private MongoAddMsgDaoImpl(){
		MongoDBUtil utilInstance = MongoDBUtil.getInstance();
		mongoDBs = utilInstance.getGameDB();
	}
	
	public MongoCollection<Document> getUserCollection(int roomId){
		MongoDatabase mongoDB = null;
		//根据roomId连接不同的数据库
		System.out.println("-----mongoDBs.size() :"+mongoDBs.size()+"-----");
		for(MongoDatabase key : mongoDBs){
			System.out.println(key.getName());
		}
		if(mongoDBs.size() == 1){
			mongoDB = mongoDBs.get(0);
		}else{
			mongoDB = mongoDBs.get(roomId-1);
		}
		MongoCollection<Document> userCollection = mongoDB.getCollection(p.getProperty("UserCollectionName"));
		return userCollection;
	}
	
	/*
	 * 查找一条user信息
	 */
	public Document findOneUserInfo(long uid, int roomId){
		BasicDBObject bson = new BasicDBObject();
		bson.put("uid", uid);
		MongoCollection<Document> userCollection = getUserCollection(roomId);
		FindIterable<Document> documents = userCollection.find(bson);
		
		Document document = new Document();
		for(Document doc : documents){
			document = doc;
			break;
		}
		return document;
	}
	
}
