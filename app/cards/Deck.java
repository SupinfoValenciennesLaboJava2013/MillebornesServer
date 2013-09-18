package cards;

import java.util.LinkedList;
import java.util.List;

public class Deck {
	
	private List<Card> deck = new LinkedList<Card>();
	
	public Deck()
	{
		deck.add(new DrivingAce());
		deck.add(new ExtraTank());
		deck.add(new PunctureProof());
		deck.add(new RightOfWay());
		
		for(int i=0;i<3;i++)
		{
			deck.add(new Accident());
			deck.add(new FlatTire());
			deck.add(new OutOfGas());
		}
		
		for(int i=0;i<4;i++)
		{
			deck.add(new SpeedLimit());
		}
		for(int i=0;i<5;i++)
		{
			deck.add(new Stop());
		}
		for(int i=0;i<6;i++)
		{
			deck.add();
				deck.add();
		}
	}
	
	public void shuffle() {
		List<Card> deck2 = new LinkedList<Card>();
		while (!this.deck.isEmpty()) {
			deck2.add(this.deck.get((int)Math.floor(Math.random() * this.deck.size())));
		}
		this.deck = deck2;
	}
	
}
