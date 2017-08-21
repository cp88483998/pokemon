package pocket.total.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import pocket.total.entity.Share;

/**
 * mongoDB多个数据库连接类
 * <p>Title: MongoDBUtil<／p>
 * <p>Description: mongoDBAdmin为PVP记录、离线消息存储数据库
 * 					mongoDBs集合存储了多个服的游戏数据库<／p>
 * <p>Company: LTGames<／p>	
 * @author 陈鹏
 * @date 2017年7月11日
 */
public class MongoDBUtil {
	private static MongoDatabase mongoDBAdmin;
//	private static MongoDatabase mongoDBGame;
	private static Properties p = Share.p;
	private static List<MongoDatabase> mongoDBs = new ArrayList<MongoDatabase>();
	
	
	private static MongoDBUtil instance = new MongoDBUtil();
	
	public static MongoDBUtil getInstance(){
		
		return instance;
	}
	static {
		//获取数据库1
		String DBName = p.getProperty("DBName");
		System.out.println("-----DBName :"+DBName+"-----");
		String[] DBNames = DBName.split(",");
		MongoClient client = getConnect();
		
		for(String key : DBNames){
			MongoDatabase mongoDB = client.getDatabase(key);
//			System.out.println(mongoDB.getName());
			mongoDBs.add(mongoDB);
		}
		
		//获取数据库2
		mongoDBAdmin = client.getDatabase(p.getProperty("serverAdminDbName"));
	}
	
	public List<MongoDatabase> getGameDB(){
		return mongoDBs;
		
	}
	
//	public static List<MongoDatabase> getGameDB(){
//		
////		DBUrl = p.getProperty("DBUrl");
////		DBPort = Integer.parseInt(p.getProperty("DBPort"));
////		server = p.getProperty("server");
////		DBName = p.getProperty("DBName");
//		
//		List<MongoDatabase> mongoDBs = new ArrayList<MongoDatabase>();
//		
//		String DBName = p.getProperty("DBName");
//		String[] DBNames = DBName.split(",");
//		MongoClient client = getDB2();
//		
//		for(String key : DBNames){
//			MongoDatabase mongoDB = client.getDatabase(key);
//			System.out.println(mongoDB.getName());
//			mongoDBs.add(mongoDB);
//		}
//		return mongoDBs;
//	}
//	
//	public static void getServerAdminDB(){
//		String DBName = p.getProperty("serverAdminDbName");
//		MongoClient client = getDB2();
//		
//		mongoDBAdmin = client.getDatabase(DBName);
//		System.out.println(mongoDBAdmin.getName());
//	}
	
	public static String getSystemParamKeyValue(String key){
		return p.getProperty(key);
	}
	
	public MongoDatabase getMongoDataBaseAdmin(){
		return mongoDBAdmin;
	}
	
	//离线消息集合
	public MongoCollection<Document> getOffCollection(){
		return mongoDBAdmin.getCollection(p.getProperty("OffCollectionName"));
	}
	//PVP单条记录集合
	public MongoCollection<Document> getPVPRecordCollection(){
		return mongoDBAdmin.getCollection(p.getProperty("PVPRecordCollectionName"));
	}
	//PVP计数集合
	public MongoCollection<Document> getPVPDataCollection(){
		return mongoDBAdmin.getCollection(p.getProperty("PVPDataCollectionName"));
	}
	//user集合
//	public MongoCollection<Document> getUserCollection(){
//		return mongoDBGame.getCollection(p.getProperty("UserCollectionName"));
//	}
	
	private static MongoClient getConnect(){	
		//String myFilePath = MongoInit.class.getResource("/").getPath() + "db.properties";
		MongoClient mongoClient = null;
		try {
//			p.load(MongoDBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
//			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/db.properties"));
//			System.out.println("-----p :"+p.toString());
			String DBUrl = p.getProperty("DBUrl");
//			String DBName = p.getProperty("DBName");
			int DBPort = Integer.parseInt(p.getProperty("DBPort"));
			String server = p.getProperty("server");
			
			MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
			buide.connectionsPerHost(Integer.parseInt(p.getProperty("connectionsPerHost")));// 与目标数据库可以建立的最大链接数
			buide.connectTimeout(Integer.parseInt(p.getProperty("connectTimeout")));// 与数据库建立链接的超时时间
			buide.maxWaitTime(Integer.parseInt(p.getProperty("maxWaitTime")));// 一个线程成功获取到一个可用数据库之前的最大等待时间
			buide.threadsAllowedToBlockForConnectionMultiplier(100);
			buide.maxConnectionIdleTime(0);
			buide.maxConnectionLifeTime(Integer.parseInt(p.getProperty("maxConnectionLifeTime")));
			buide.socketTimeout(Integer.parseInt(p.getProperty("socketTimeout")));
			buide.socketKeepAlive(true);
			MongoClientOptions myOptions = buide.build();
			
			if(server.equals("Chinese")){
				
				mongoClient = createMongoDBClient(DBUrl, DBPort);
				
			}else{
				ServerAddress serverAddress = new ServerAddress(DBUrl, DBPort);
				mongoClient = new MongoClient(serverAddress, myOptions);
			}
		} catch (Exception e) {
		   e.printStackTrace();
		   System.out.println("连接超时！");
		}
		return mongoClient;
	}
	
	/*
	 * 阿里云，第一种办法，目前用的第一种
	 */
	private static MongoClient createMongoDBClient(String DBUrl, int DBPort){
		ServerAddress serverAddress = new ServerAddress(DBUrl, DBPort);  
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
        addrs.add(serverAddress);  
          
        String username = p.getProperty("username");
		String password = p.getProperty("password");
		String defaultDB = p.getProperty("DefaultDB");
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
        MongoCredential credential = MongoCredential.createScramSha1Credential(username, defaultDB, password.toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
        credentials.add(credential);  
        
        return new MongoClient(addrs,credentials);  
	}
	
	/*
	 * 阿里云，第二种办法
	 */
	public MongoClient createMongoDBClientWithURI(String DBUrl, int DBPort) {
		ServerAddress serverAddress = new ServerAddress(DBUrl, DBPort);  
		String username = p.getProperty("username");
		String password = p.getProperty("password");
		String defaultDB = p.getProperty("DefaultDB");
        //另一种通过URI初始化
        //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
		String url = "mongodb://" + username + ":" + password + "@" + serverAddress + "/" + defaultDB;
		System.out.println(url);
        MongoClientURI connectionString = new MongoClientURI(url);
        return new MongoClient(connectionString);
    }
	
}
