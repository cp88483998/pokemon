package pocket.chat.entity;

public class ChatUser {
	private String username;
	private String nickname;
	private int level;
	private int vip;
	private int elves_head_id;
	private int trainer_head_id;
	private int gender;
	private String time;
	private int pokedex;
	private long uid;
	private String elves_info;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public int getElves_head_id() {
		return elves_head_id;
	}
	public void setElves_head_id(int elves_head_id) {
		this.elves_head_id = elves_head_id;
	}
	public int getTrainer_head_id() {
		return trainer_head_id;
	}
	public void setTrainer_head_id(int trainer_head_id) {
		this.trainer_head_id = trainer_head_id;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getPokedex() {
		return pokedex;
	}
	public void setPokedex(int pokedex) {
		this.pokedex = pokedex;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getElves_info() {
		return elves_info;
	}
	public void setElves_info(String elves_info) {
		this.elves_info = elves_info;
	}
	
}
