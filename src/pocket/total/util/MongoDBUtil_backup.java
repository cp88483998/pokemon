package pocket.total.util;

import java.io.IOException;
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

public class MongoDBUtil_backup {
	private static MongoClient mongoClientAdmin;
	private static MongoClient mongoClientGame;
	private static MongoDatabase mongoDBAdmin;
	private static MongoDatabase mongoDBGame;
	private static String adminDb;
	private static String gameDb;
	
	private static String DBUrl;
	private static int DBPort;
	private static String server;
	
	// 读取配置文件
	private static Properties p = new Properties();	
	
	static{
		try {
			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/db.properties"));
//			System.out.println("-----p :"+p.toString());
			adminDb = p.getProperty("serverAdminDbName");
			gameDb = p.getProperty("gameDbName");
			
			DBUrl = p.getProperty("DBUrl");
			DBPort = Integer.parseInt(p.getProperty("DBPort"));
			server = p.getProperty("server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final static MongoDBUtil_backup serverAdminInstance = new MongoDBUtil_backup(adminDb,1);
	private final static MongoDBUtil_backup gameDbInstance = new MongoDBUtil_backup(gameDb,2);
	
	public static MongoDBUtil_backup getServerAdminInstance(){
		return serverAdminInstance;
	}
	
	public static MongoDBUtil_backup getGameDbInstance(){
		return gameDbInstance;
	}
	
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
	public MongoCollection<Document> getUserCollection(){
		return mongoDBGame.getCollection(p.getProperty("UserCollectionName"));
	}
	
	public MongoDBUtil_backup(String DBName, int type){	
		//String myFilePath = MongoInit.class.getResource("/").getPath() + "db.properties";
		try {
//			p.load(MongoDBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
//			p.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resource/db.properties"));
//			System.out.println("-----p :"+p.toString());
//			String DBUrl = p.getProperty("DBUrl");
//			String DBName = p.getProperty("DBName");
//			int DBPort = Integer.parseInt(p.getProperty("DBPort"));
			
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
			
//			System.out.println(p.getProperty("server"));
			if(server.equals("Chinese")){
				
				mongoClientAdmin = createMongoDBClient(DBUrl, DBPort);
//				mongoClient = createMongoDBClientWithURI(DBUrl, DBPort);
				mongoClientGame = createMongoDBClient(DBUrl, DBPort);
				
			}else{
				ServerAddress serverAddress = new ServerAddress(DBUrl, DBPort);
				mongoClientAdmin = new MongoClient(serverAddress, myOptions);
				mongoClientGame = new MongoClient(serverAddress, myOptions);
			}
			
//			System.out.println("Connect to mongo successfully");
			   
			if(type == 1){
				mongoDBAdmin = mongoClientAdmin.getDatabase(DBName);
				System.out.println("Connect to mongoDB successfully : "+mongoDBAdmin.getName());
			}
			if(type == 2){
				mongoDBGame = mongoClientGame.getDatabase(DBName);
				System.out.println("Connect to mongoDB successfully : "+mongoDBGame.getName());
			}
		} catch (Exception e) {
		   e.printStackTrace();
		   System.out.println("连接超时！");
		}
	}
	
	/*
	 * 阿里云，第一种办法
	 */
	public MongoClient createMongoDBClient(String DBUrl, int DBPort){
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
