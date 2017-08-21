package pocket.pvp.entity;

import java.util.List;

public class Infor {
	private Player player;
	private List<Elve> elves;
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public List<Elve> getElves() {
		return elves;
	}
	public void setElves(List<Elve> elves) {
		this.elves = elves;
	}
	@Override
	public String toString() {
		return "Infor [player=" + player + ", elves=" + elves + "]";
	}
	
}
