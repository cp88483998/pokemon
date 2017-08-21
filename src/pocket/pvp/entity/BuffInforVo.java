package pocket.pvp.entity;

import pocket.total.util.MyUtil;
import scala.util.Random;

public class BuffInforVo {
	//buff数据
	private BuffDataVo buffDataVo;
	//持续回合
	private int round;
	
	public BuffInforVo(BuffDataVo buffDataVo){
		this.buffDataVo = buffDataVo;
		
		//设置buff持续回合
		int[] arr = MyUtil.ArrayToInt(buffDataVo.getRound().split(","));
		Random ran = new Random();
		round = (arr.length==1) ? ((arr[0]==-1 || arr[0]==0) ? Integer.MAX_VALUE : arr[0]) : arr[ran.nextInt(arr.length)];
	}
	public BuffDataVo getBuffDataVo() {
		return buffDataVo;
	}
	public void setBuffDataVo(BuffDataVo buffDataVo) {
		this.buffDataVo = buffDataVo;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	@Override
	public String toString() {
		return "BuffInforVo [buffDataVo=" + buffDataVo + ", round=" + round + "]";
	}
	
}
