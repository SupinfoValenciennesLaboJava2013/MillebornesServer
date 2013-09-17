package cards;

import player.Player;
import exceptions.CannotPlayThisCardException;

public class DistanceCard extends Card {
	
	private int km;
	
	public int getKm() {
		return this.km;
	}
	
	private DistanceCard(int km) {
		this.km = km;
	}
	
	public static DistanceCard get25km() {
		return new DistanceCard(25);
	}
	
	public static DistanceCard get50km() {
		return new DistanceCard(50);
	}
	
	public static DistanceCard get75km() {
		return new DistanceCard(75);
	}
	
	public static DistanceCard get100km() {
		return new DistanceCard(100);
	}
	
	public static DistanceCard get200km() {
		return new DistanceCard(200);
	}
	
	@Override
	public void advancePlayer(Player player) throws CannotPlayThisCardException {
		if (player.canAdvance(this.getKm())) {
			player.setKm(player.getKm() + this.getKm());
		} else {
			throw new CannotPlayThisCardException();
		}
	}
}