package test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.net.TelnetAppender;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
public class AliyunTest {
    public static ServerAddress seed1 = new ServerAddress("dds-bp19cf2236e95c042.mongodb.rds.aliyuncs.com", 3717);
//    public static ServerAddress seed2 = new ServerAddress("dds-bp19cf2236e95c041.mongodb.rds.aliyuncs.com", 3717);
    public static String username = "root";
    public static String password = "Ks123456";
//    public static String ReplSetName = "mgset-1441984463";
    public static String DEFAULT_DB = "server_admin";
    public static String DEMO_DB = "server_admin";
    public static String DEMO_COLL = "orders_log";
    public static MongoClient createMongoDBClient() {
        // 构建Seed列表
        List<ServerAddress> seedList = new ArrayList<ServerAddress>();
        seedList.add(seed1);
//        seedList.add(seed2);
        // 构建鉴权信息
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createScramSha1Credential(username, DEFAULT_DB, password.toCharArray()));
        // 构建操作选项，requiredReplicaSetName属性外的选项根据自己的实际需求配置，默认参数满足大多数场景
        MongoClientOptions options = MongoClientOptions.builder()
//                .requiredReplicaSetName(ReplSetName)
                .socketTimeout(2000)
                .connectionsPerHost(1).build();
        return new MongoClient(seedList, credentials, options);
    }
    public static MongoClient createMongoDBClientWithURI() {
        //另一种通过URI初始化
        //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
    	String url = "mongodb://" + username + ":" + password + "@" + seed1 + "/" + DEFAULT_DB;
    	System.out.println(url);
        MongoClientURI connectionString = new MongoClientURI(url);
        
        return new MongoClient(connectionString);
    }
    public static void main(String args[]) {
        MongoClient client = createMongoDBClient();
        //or
//        MongoClient client = createMongoDBClientWithURI();
        try {
            // 取得Collecton句柄
            MongoDatabase database = client.getDatabase(DEMO_DB);
            System.out.println(database.getName());
            
            database.createCollection("pvp_data");
//            MongoIterable<String> iterable = database.listCollectionNames();
//            for(String name : iterable){
//            	System.out.println(name);
//            }
//            MongoCollection<Document> collection = database.getCollection(DEMO_COLL);
//            System.out.println(collection.count());
            
        } finally {
            //关闭Client，释放资源
            client.close();
        }
        return ;
    }
}
