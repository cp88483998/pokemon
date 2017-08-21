package pocket.pvp.dao;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.result.DeleteResult;

import pocket.chat.entity.OfflineMsg;
import pocket.pvp.entity.Player;

public interface MongoPVPDao {
	/**
	 * 文档数量
	 * @return
	 */
	public long count();
	
	/**
	 * 插入一条PVP数据
	 * @param jo
	 * @return
	 */
	public boolean insertOneRecord(Player player, Player playerMatch, boolean isRobot);
	
	/**
	 * 插入一条统计记录
	 * @param document
	 * @return
	 */
	public boolean insertRecord(Document document);
//	public boolean insertData(Document document);
	
	/**
	 * 找出一条消息
	 * @param key
	 * @param value
	 * @return
	 */
//	public Document findOneData(String country);
	
	/**
	 * 找出该条件所有PVP数据
	 * @param key
	 * @param value
	 * @return
	 */
	public List<OfflineMsg> findAll(String key,Object value);
	
	/**
	 * 删除该条件所有PVP数据
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteResult deleteAll(String key, Object value);
	
	/**
	 * 更新数据
	 * @param document1
	 * @param document2
	 */
//	public boolean updateOneData(BasicDBObject document1, BasicDBObject document2);
	
	/**
	 * 更新PVP
	 * @param key
	 */
	public boolean updatePVPData(String country, String which);
}
