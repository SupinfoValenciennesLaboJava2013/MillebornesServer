package cards.hazards;

import cards.Card;
import cards.MalusCard;

public class OutOfGas extends MalusCard {
	public boolean canBeCuredBy(Card card) {
		return card.canCurePanneSeche();
	}
	
	public boolean canBeCounteractBy(Card card) {
		return card.canCounteractPanneSeche();
	}
}
