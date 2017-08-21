package pocket.timeSpace.entity;

import com.smartfoxserver.v2.entities.User;

public class BossPlayer {
	private User user;
	private long startTime;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
