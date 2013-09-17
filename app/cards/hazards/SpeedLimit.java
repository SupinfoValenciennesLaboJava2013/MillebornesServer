package cards.hazards;

import player.Player;
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
	public void setToPlayerAsMalus(Player player) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		super.setToPlayerAsMalus(player);
		player.setSpeedLimited(true);
	}
}
