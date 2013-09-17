package cards.hazards;

import cards.Card;
import cards.MalusCard;

public class FlatTire extends MalusCard {
	@Override
	public boolean canBeCuredBy(Card card) {
		return card.canCureCrevaison();
	}
	
	public boolean canBeCounteractBy(Card card) {
		return card.canCounteractCrevaison();
	}
}
