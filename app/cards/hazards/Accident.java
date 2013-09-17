package cards.hazards;

import cards.Card;
import cards.MalusCard;

public class Accident extends MalusCard {
	public boolean canBeCuredBy(Card card) {
		return card.canCureAccident();
	}
	
	public boolean canBeCounteractBy(Card card) {
		return card.canCounteractAccident();
	}
}