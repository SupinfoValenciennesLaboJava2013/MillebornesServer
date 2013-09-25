package commands;

import cards.Card;

public class CardRemoveCommand extends AbstractCommand {

	private Card card;
	
	public CardRemoveCommand(Card card) {
		this.setCard(card);
	}
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	{
		setType("card.remove");
	}
}
