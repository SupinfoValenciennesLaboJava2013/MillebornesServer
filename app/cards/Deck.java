package cards;

import java.util.LinkedList;
import java.util.List;

public class Deck {
	
	private List<Card> deck = new LinkedList<Card>();
	
	public Deck()
	{
		//ajout des Safeties
		deck.add(new DrivingAce());
		deck.add(new ExtraTank());
		deck.add(new PunctureProof());
		deck.add(new RightOfWay());
		
		//ajout d'Attaque
		for(int i=0;i<3;i++)
		{
			deck.add(new Accident());
			deck.add(new FlatTire());
			deck.add(new OutOfGas());
		}
		//ajout d'Attaque et de Distance
		for(int i=0;i<4;i++)
		{
			deck.add(new SpeedLimit());
			deck.add(Distance.get200km());
		}
		//ajout D'attaque
		for(int i=0;i<5;i++)
			deck.add(new Stop());
		//ajout des reparations
		for(int i=0;i<6;i++)
		{
			deck.add(new Repairs());
			deck.add(new EndOfLimit());
			deck.add(new Gasoline());
			deck.add(new SpareTire());
		}
		//ajout de distances
		for(int i=0;i<10;i++)
		{
			deck.add(Distance.get25km());
			deck.add(Distance.get50km());
			deck.add(Distance.get75km());
		}
		//ajout de distances
		for(int i=0;i<12;i++)
			deck.add(Distance.get100km());
		//ajout de feu vert	
		for(int i=0;i<14;i++)
			deck.add(new GoRoll());
	}
	
	public void shuffle() {
		List<Card> deck2 = new LinkedList<Card>();
		while (!this.deck.isEmpty()) {
			deck2.add(this.deck.get((int)Math.floor(Math.random() * this.deck.size())));
		}
		this.deck = deck2;
	}
	
}
