package commands;

public class CardReceiveCommand extends AbstractCommand {

	private String card;
	
	public CardReceiveCommand(String card) {
		this.setCard(card);
	}
	
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	{
		setType("card.receive");
	}
}
