package commands;

import cards.Card;

public class CardReceiveCommand extends AbstractCommand {

	private Card card;
	
	public CardReceiveCommand(Card card) {
		this.setCard(card);
	}
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	{
		setType("card.receive");
	}
}
