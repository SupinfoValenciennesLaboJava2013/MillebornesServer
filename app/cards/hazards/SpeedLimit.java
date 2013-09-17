package cards.hazards;

import models.User;
import cards.Card;
import cards.MalusCard;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

public class SpeedLimit extends MalusCard {
	public boolean canBeCuredBy(Card card) {
		return card.canCureSpeedLimit();
	}
	
	public boolean canBeCounteractBy(Card card) {
		return card.canCounteractSpeedLimit();
	}
	
	@Override
	public void setToPlayerAsMalus(User user) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		super.setToPlayerAsMalus(user);
		user.setSpeedLimited(true);
	}
}
