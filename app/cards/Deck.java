package cards;

import java.util.LinkedList;
import java.util.List;

public class Deck {
	
	private List<Card> deck = new LinkedList<Card>();
	
	public Deck()
	{
		
	}
	
	public void shuffle() {
		List<Card> deck2 = new LinkedList<Card>();
		while (!this.deck.isEmpty()) {
			deck2.add(this.deck.get((int)Math.floor(Math.random() * this.deck.size())));
		}
		this.deck = deck2;
	}
	
}
