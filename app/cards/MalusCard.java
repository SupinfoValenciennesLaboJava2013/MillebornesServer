package cards;

import models.User;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

public 	abstract class MalusCard extends Card {
	public void setToPlayerAsMalus(User user) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		if (user.getMalus() == null) {
			user.setMalus(this);
		} else {
			throw new PlayerCantHaveMoreMalusException();
		}
	}
}
