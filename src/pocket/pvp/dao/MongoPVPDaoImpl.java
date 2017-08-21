package pocket.pvp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import pocket.chat.entity.OfflineMsg;
import pocket.pvp.entity.Player;
import pocket.total.util.MongoDBUtil;

public class MongoPVPDaoImpl implements MongoPVPDao {

	
	private static final MongoPVPDaoImpl instance = new MongoPVPDaoImpl();// 饿汉式单例模式
	
	public static MongoPVPDaoImpl getInstance(){
		return instance;
	}
	
	private static MongoCollection<Document> recordCollection;
//	private static MongoCollection<Document> dataCollection;
	
	public MongoPVPDaoImpl(){
		MongoDBUtil utilInstance = MongoDBUtil.getInstance();
		recordCollection = utilInstance.getPVPRecordCollection();
//		dataCollection = dbUtil.getPVPDataCollection();
	}
	
	public long count() {
		
		return recordCollection.count();
	}
	
	/**
	 * 插入当天 pvp record
	 */
	public boolean insertOneRecord(Player player, Player playerMatch, boolean isRobot) {
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		boolean result = true;
		try {
			findOneRecord(player.getCountry());
			Document document = new Document();
			document.append("isRobot", isRobot);
			document.append("time", System.currentTimeMillis());
			
			document.append("playerUid", player.getUid());
			document.append("playerName", player.getNickname());
			document.append("playerIp", player.getUser().getIpAddress());
			document.append("playerServer", player.getServerName());
			document.append("playerCountry", player.getCountry());
			
			if(!isRobot){
				document.append("matchUid", playerMatch.getUid());
				document.append("matchName", playerMatch.getNickname());
				document.append("matchIp", playerMatch.getUser().getIpAddress());
				document.append("matchServer", playerMatch.getServerName());
				document.append("matchCountry", playerMatch.getCountry());
			}
			
			BasicDBObject bson = new BasicDBObject("country", player.getCountry());
			BasicDBObject bson2 = new BasicDBObject("$push", new BasicDBObject("record."+dateStr, document));
			updateOneRecord(bson, bson2);
			
			//插入record之后直接更新count
			if(!isRobot){
				updatePVPData(player.getCountry(), "playerMatch");
			}else{
				updatePVPData(player.getCountry(), "robotMatch");
			}
			
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public boolean insertRecord(Document document){
		boolean result = true;
		try {
			recordCollection.insertOne(document);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新当天pvp data
	 */
	public boolean updatePVPData(String country, String which){
		boolean result = true;
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		try {
			Document doc = findOneRecord(country);
			
			Document countDoc = (Document) doc.get("count");
			Document countDateDoc = (Document) countDoc.get(dateStr);
			BasicDBObject bson = new BasicDBObject("country", country);
			long count = countDateDoc.getLong(which);
			BasicDBObject bson2 = new BasicDBObject("$set", new BasicDBObject("count."+dateStr+"."+which, count+1));
			updateOneRecord(bson, bson2);
			
		} catch (Exception e) {
			result = false;
		}	
		return result;
	}

	public Document findOneRecord(String country) {
		
		BasicDBObject bson = new BasicDBObject();
		bson.put("country", country);
		FindIterable<Document> documents = recordCollection.find(bson);
		
		Document document = new Document();
		for(Document doc : documents){
			document = doc;
			break;
		}
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		Document document3 = new Document();
		document3.append("reqPVPCount", 0L);
		document3.append("playerMatch", 0L);
		document3.append("robotMatch", 0L);
		document3.append("cancelCount", 0L);
		if(document.size() == 0){
			document.append("country", country);
			document.append("count", new Document().append(dateStr, document3));
			document.append("record", new Document().append(dateStr, new ArrayList<Document>()));
			insertRecord(document);
		}
		Document countDoc = (Document) document.get("count");
		Document countDateDoc = (Document) countDoc.get(dateStr);
		if(countDateDoc == null){
			BasicDBObject bson1 = new BasicDBObject("country", country);
			BasicDBObject bson2 = new BasicDBObject("$set", new BasicDBObject("count."+dateStr, document3));
			updateOneRecord(bson1, bson2);
			
			documents = recordCollection.find(bson);
			for(Document doc : documents){
				document = doc;
				break;
			}
		}
		return document;
	}
	
	public boolean updateOneRecord(BasicDBObject bson1, BasicDBObject bson2){
		boolean result = true;
		try {
			recordCollection.updateOne(bson1, bson2);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	public List<OfflineMsg> findAll(String key, Object value) {
		return null;
	}

	public DeleteResult deleteAll(String key, Object value) {
		return null;
	}
}
