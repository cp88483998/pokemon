package test;

import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import pocket.chat.dao.MongoOffDao;
import pocket.chat.dao.MongoOffDaoImpl;
import pocket.chat.entity.OfflineMsg;
import pocket.total.util.MongoDBUtil;

public class TestCase2 {
	
	MongoOffDao dao = MongoOffDaoImpl.getInstance();
	@Test
	public void connectTest(){
		
		MongoDBUtil dbUtil = MongoDBUtil.getInstance();
		MongoDatabase db = dbUtil.getMongoDataBaseAdmin();
		System.out.println(db.getName());
	}
	
	@Test
	public void createCol(){
		MongoDBUtil dbUtil = MongoDBUtil.getInstance();
		MongoDatabase db = dbUtil.getMongoDataBaseAdmin();
		db.createCollection("PVPData");
	}
	@Test
	public void insertOne(){
		Document document = new Document();
		document.append("senderUid", "14884231051702");
		document.append("recipientUid", "14884231051701");
		document.append("context", "你好!0507");
		document.append("additionalMsg", "1|F_G|test182,王路飞,70,15,2,0,2,2,24/4 05:39:12,58,14884231051702,1000315:2:68:girl;2000033:2:59:girl;1000282:2:53:girl;1000251:3:70:;2000032:2:49:girl;1000212:1:39:boy ");
		document.append("createdAt", System.currentTimeMillis());
		dao.insertOne(document);
		System.out.println(dao.count());
	}
	
	@Test
	public void findMany(){
		Set<String> senderUids = dao.findMany("recipientUid", "14884231051701");
		System.out.println(senderUids.size());
	}
	@Test
	public void findOneGroup(){
		List<OfflineMsg> list = dao.findOneGroup("14884231051702", "14884231051701");
		for(OfflineMsg offlineMsg : list){
			System.out.println(offlineMsg.toString());
		}
	}
	@Test
	public void deleteOneGroup(){
		DeleteResult result = dao.deleteOneGroup("14884231051702", "14884231051701");
		System.out.println(result);
	}
	@Test 
	public void deleteOneMsg(){
		DeleteResult result = dao.deleteOneMsg("14884231051702", "14884231051701");
		System.out.println(result);
	}
	@Test
	public void deleteAll(){
		DeleteResult result = dao.deleteAll("recipientUid", "14884231051701");
		System.out.println(result);
	}
	@Test 
	public void findOneAll(){
		List<OfflineMsg> offlineMsgs = dao.findOneAll("senderUid", "14884231051702");
		System.out.println(offlineMsgs);
	}
}
