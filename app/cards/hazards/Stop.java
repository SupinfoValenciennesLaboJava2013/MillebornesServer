package cards.hazards;

import cards.Card;
import cards.MalusCard;

public class Stop extends MalusCard {
	public boolean canBeCuredBy(Card card) {
		return card.canCureStop();
	}
	
	public boolean canBeCounteractBy(Card card) {
		return card.canCounteractStop();
	}
	
}
