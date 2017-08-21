package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.BSON;
import org.bson.Document;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.sf.json.JSONObject;
import pocket.pvp.dao.MongoPVPDaoImpl;
import pocket.pvp.entity.Player;
import pocket.total.util.IpInfoUtil;
import pocket.total.util.MongoDBUtil;

public class TestCase3 {
	MongoPVPDaoImpl dao = MongoPVPDaoImpl.getInstance();
	@Test
	public void timeTest(){
		long usTime = 1495073248183l;
		long time = usTime - 46800000;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(usTime));
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(1495073852183l));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(1495073416183l));
		
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println(hour);
		if(hour>=12 && hour<=13 || hour>=19 && hour<=20){
			System.out.println(true);
		}else{
			System.out.println(false);
		}
	}
	
	@Test
	public void testCount(){
		System.out.println(dao.count());
	}
	
	@Test
	public void testInsert(){
		
		Document document1 = new Document();
		document1.append("isRobot", false);
		document1.append("time", System.currentTimeMillis());
		document1.append("playerUid", 14884231051701L);
		document1.append("playerName", "疯狂的石头");
		document1.append("playerServer", "s1");
		document1.append("playerIp", "112.80.59.174");
		document1.append("playerCountry", "中国");
		document1.append("matchUid", 14884231051702L);
		document1.append("matchName", "疯狂的赛车");
		document1.append("matchServer", "s2");
		document1.append("matchIp", "58.209.20.25");
		document1.append("matchCountry", "中国");
		Document document2 = new Document();
		document2.append("isRobot", false);
		document2.append("time", System.currentTimeMillis());
		document2.append("playerUid", 14884231051701L);
		document2.append("playerName", "疯狂的石头");
		document2.append("playerServer", "s1");
		document2.append("playerIp", "112.80.59.174");
		document2.append("playerCountry", "中国");
		document2.append("matchUid", 14884231051702L);
		document2.append("matchName", "疯狂的赛车");
		document2.append("matchServer", "s2");
		document2.append("matchIp", "58.209.20.25");
		document2.append("matchCountry", "中国");
		List<Document> list = new ArrayList<Document>();
		list.add(document1);
		list.add(document2);
		
		Document document3 = new Document();
		document3.append("reqPVPCount", 100L);
		document3.append("playerMatch", 80L);
		document3.append("robotMatch", 50L);
		document3.append("cancelCount", 20L);
		
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		Document doc = new Document();
		doc.append("country", "中国");
		doc.append("count", new Document().append(dateStr, document3));
		doc.append("record", new Document().append(dateStr, list));
		dao.insertRecord(doc);
	}
	@Test
	public void updateData(){
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
//		String dateStr = "2017-05-18";
		Document doc = dao.findOneRecord("墨西哥");
		Document countDoc = (Document) doc.get("count");
		Document countDateDoc = (Document) countDoc.get(dateStr);
		long reqPVPCount = countDateDoc.getLong("reqPVPCount");
		
		BasicDBObject bson = new BasicDBObject("country", "墨西哥");
		BasicDBObject bson2 = new BasicDBObject("$set", new BasicDBObject("count."+dateStr+".reqPVPCount", reqPVPCount+1));
		dao.updateOneRecord(bson, bson2);
		
	}
	@Test
	public void insertRecord(){
//		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		String dateStr = "2017-05-13";
		Document doc = dao.findOneRecord("美国");
		Document document3 = new Document();
		document3.append("reqPVPCount", 0L);
		document3.append("playerMatch", 0L);
		document3.append("robotMatch", 0L);
		document3.append("cancelCount", 0L);
		
		if(doc.size() == 0){
			Document document = new Document();
			document.append("country", "美国");
			document.append("count", new Document().append(dateStr, document3));
			document.append("record", new Document().append(dateStr, new ArrayList<Document>()));
			dao.insertRecord(document);
		}
		Document document1 = new Document();
		document1.append("isRobot", true);
		document1.append("time", System.currentTimeMillis());
		document1.append("playerUid", 14884231051703L);
		document1.append("playerName", "疯狂的明天");
		document1.append("playerServer", "s1");
		document1.append("playerIp", "112.80.59.174");
		document1.append("playerCountry", "美国");
		document1.append("matchUid", 14884231051704L);
		document1.append("matchName", "疯狂的明天");
		document1.append("matchServer", "s2");
		document1.append("matchIp", "58.209.20.25");
		document1.append("matchCountry", "中国");
		
		BasicDBObject bson = new BasicDBObject("country", "美国");
		BasicDBObject bson2 = new BasicDBObject("$push", new BasicDBObject("record."+dateStr, document1));
		dao.updateOneRecord(bson, bson2);
		
	}
	@Test
	public void updateTest(){
		dao.updatePVPData("朝鲜", "reqPVPCount");
	}
	
	@Test
	public void testDate(){
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		System.out.println(dateStr);
	}
	@Test
	public void testUpdate(){
		String country = "China";
		String which = "reqPVPCount";
		dao.updatePVPData(country, which);
		which = "cancelCount";
		dao.updatePVPData(country, which);
	}
	
	@Test 
	public void testIp(){
		String result = IpInfoUtil.getIpInfo("112.198.70.157");
		IpInfoUtil.getIpInfo("113.210.76.49");
//		System.out.println(result.getJSONObject("data").getString("country"));
		System.out.println(result);
	}
}
