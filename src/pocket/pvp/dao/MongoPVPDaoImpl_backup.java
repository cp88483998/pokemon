package pocket.pvp.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import pocket.chat.entity.OfflineMsg;
import pocket.pvp.entity.Player;
import pocket.total.util.MongoDBUtil;

public class MongoPVPDaoImpl_backup{

	
	private static final MongoPVPDaoImpl_backup instance = new MongoPVPDaoImpl_backup();// 饿汉式单例模式
	
	public static MongoPVPDaoImpl_backup getInstance(){
		return instance;
	}
	
	private static MongoCollection<Document> recordCollection;
	private static MongoCollection<Document> dataCollection;
	
	public MongoPVPDaoImpl_backup(){
		MongoDBUtil dbUtil = MongoDBUtil.getInstance();
		recordCollection = dbUtil.getPVPRecordCollection();
		dataCollection = dbUtil.getPVPDataCollection();
	}
	
	public long count() {
		
		return recordCollection.count();
	}
	

	public boolean insertOneRecord(Player player, Player playerMatch, boolean isRobot) {
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
		
		boolean result = insertRecord(document);
		
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
	
	public boolean insertData(Document document){
		boolean result = true;
		try {
			dataCollection.insertOne(document);
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

	public boolean updatePVPData(String country, String which){
		boolean result = true;
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		try {
			Document doc = findOneData(country, dateStr);
			if(doc.size() == 0){
				
				Document document = new Document();
				document.append("country", country);
				document.append("reqPVPCount", 0L);
				document.append("playerMatch", 0L);
				document.append("robotMatch", 0L);
				document.append("cancelCount", 0L);
				document.append("date", dateStr);
				insertData(document);
				doc = document;
			}
			long count = doc.getLong(which);
			Document document1 = new Document();
			document1.append("country", country);
			document1.append("date", dateStr);
			
			Document document2 = new Document();
			document2.append("$set", new Document(which, count+1));
			
			updateOneData(document1, document2);
		} catch (Exception e) {
			result = false;
		}	
		return result;
	}
	public Document findOneData2(String country) {
		
		BasicDBObject bson = new BasicDBObject();
		bson.put("country", country);
		FindIterable<Document> documents = dataCollection.find(bson);
		Document document = new Document();
		for(Document doc : documents){
			document = doc;
			break;
		}
		return document;
	}
	public Document findOneData(String country, String date) {
		
		BasicDBObject bson = new BasicDBObject();
		bson.put("country", country);
		bson.put("date", date);
		FindIterable<Document> documents = dataCollection.find(bson);
		
		Document document = new Document();
		for(Document doc : documents){
			document = doc;
			break;
		}
		return document;
	}
	
	public boolean updateOneData(Document document1, Document document2){
		boolean result = true;
		try {
			dataCollection.updateOne(document1, document2);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public boolean updateOneData2(BasicDBObject document1, BasicDBObject document2){
		boolean result = true;
		try {
			dataCollection.updateOne(document1, document2);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

}
