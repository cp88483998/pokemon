package pocket.pvp.entity;

//Elves_value配置表
public class Elves_value {
	private int valueID;
	private int total_value;
	private int HP;
	//物攻
	private int ATK;
	//物防
	private int DEF;
	//特攻
	private int INT;
	//特防
	private int MDF;
	//速度
	private int ACT;
	public int getValueID() {
		return valueID;
	}
	public void setValueID(int valueID) {
		this.valueID = valueID;
	}
	public int getTotal_value() {
		return total_value;
	}
	public void setTotal_value(int total_value) {
		this.total_value = total_value;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	public int getATK() {
		return ATK;
	}
	public void setATK(int aTK) {
		ATK = aTK;
	}
	public int getDEF() {
		return DEF;
	}
	public void setDEF(int dEF) {
		DEF = dEF;
	}
	public int getINT() {
		return INT;
	}
	public void setINT(int iNT) {
		INT = iNT;
	}
	public int getMDF() {
		return MDF;
	}
	public void setMDF(int mDF) {
		MDF = mDF;
	}
	public int getACT() {
		return ACT;
	}
	public void setACT(int aCT) {
		ACT = aCT;
	}
	@Override
	public String toString() {
		return "Elves_value [valueID=" + valueID + ", total_value=" + total_value + ", HP=" + HP + ", ATK=" + ATK
				+ ", DEF=" + DEF + ", INT=" + INT + ", MDF=" + MDF + ", ACT=" + ACT + "]";
	}
	
}
