package pocket.timeSpace.entity;

public class Boss {
	private String elvesId;
	private long maxHp;
	private long nowHp;
	public long getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}
	public long getNowHp() {
		return nowHp;
	}
	public void setNowHp(long nowHp) {
		this.nowHp = nowHp;
	}
	public String getElvesId() {
		return elvesId;
	}
	public void setElvesId(String elvesId) {
		this.elvesId = elvesId;
	}
	
}
