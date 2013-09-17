package cards.remedies;

import cards.RemedyCard;

public class Repairs extends RemedyCard {
	@Override
	public boolean canCureAccident() {
		return true;
	}
}