package cards;

import player.Player;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

public 	abstract class MalusCard extends Card {
	public void setToPlayerAsMalus(Player player) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		if (player.getMalus() == null) {
				player.setMalus(this);
		} else {
			throw new PlayerCantHaveMoreMalusException();
		}
	}
}
