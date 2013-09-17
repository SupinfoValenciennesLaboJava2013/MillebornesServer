package player;

import java.util.ArrayList;

import cards.Card;
import cards.SafetiesCard;
import cards.safeties.DrivingAce;
import cards.safeties.ExtraTank;
import cards.safeties.PunctureProof;
import cards.safeties.RightOfWay;
import exceptions.CannotPlayThisCardException;
import exceptions.CannotPlayThisRemedyCardException;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

public class Player {
	
	private boolean hasGreenLight = false;
	
	private boolean speedLimited = false;
	
	private ArrayList<SafetiesCard> playerSafeties = new ArrayList<SafetiesCard>();
	
	public void setSpeedLimited(boolean arg) {
		this.speedLimited = arg;
	}
	
	public void purge() {
		this.malus = null;
		this.speedLimited = false;
	}
	
	// Distance parcourue, en .... km !!
	private int km;
	
	public void setKm(int km) {
		this.km = km;
	}
	
	public int getKm() {
		return this.km;
	}
	
	public boolean canAdvance(int km) throws CannotPlayThisCardException {
		if (!this.hasGreenLight) {
			return false;
		} else if (km > 50 && this.speedLimited) {
			return false;
		} else {
			return (700 - this.km) > km;
		}
	}
	
	public void setSafetie(Card card) throws CannotPlayThisCardException
	{
		if(card instanceof SafetiesCard)
			playerSafeties.add((SafetiesCard)card);
		else
			throw new CannotPlayThisCardException();	
	}
	
	public boolean haveSafeties()
	{
		if(playerSafeties.size() != 0)
			return true;
		else
			return false;
	}
	 
	public void advance(Card card) throws CannotPlayThisCardException {
		card.advancePlayer(this);
	}
	
	// La carte malus actuellement appliquée
	public Card malus;
	
	public void setMalus(Card card) {
		if(!this.haveSafeties())
			this.malus = card;
		else
		{
			for(int i=0;i<playerSafeties.size();i++)
			{
				if(card.canBeCounteractBy(playerSafeties.get(i)))
					return;
			}
		}
	}
	
	public Card getMalus() {
		return this.malus;
	}
	
	// Attaque le joueur avec une carte
	public void attackWithMalus(Card card) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		card.setToPlayerAsMalus(this);
	}
	
	// Permet au joueur d'essayer de se débarasser d'un malus
	void playRemedyCard(Card remedyCard) throws CannotPlayThisRemedyCardException {
		if (this.malus != null && this.malus.canBeCuredBy(remedyCard)) {
			this.purge();
		} else {
			throw new CannotPlayThisRemedyCardException();
		}
	}
}