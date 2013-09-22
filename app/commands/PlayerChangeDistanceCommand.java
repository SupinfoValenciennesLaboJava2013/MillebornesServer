package commands;

public class PlayerChangeDistanceCommand extends AbstractCommand {

	private int km;

	public PlayerChangeDistanceCommand(int km) {
		this.setKm(km);
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getKm() {
		return km;
	}
}
