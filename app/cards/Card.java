package cards;

import models.User;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import exceptions.CannotPlayThisCardException;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
public abstract class Card {
	
	private static int uniqueCardIdInc = 1;

	private int id;
	
	public Card() {
		this.id = uniqueCardIdInc++;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean canCureCrevaison() {
		return false;
	}
	
	public boolean canCurePanneSeche() {
		return false;
	}
	
	public boolean canCureAccident() {
		return false;
	}
	
	public boolean canCureSpeedLimit() {
		return false;
	}
	
	public boolean canCureStop() {
		return false;
	}
	
	public boolean canBeCuredBy(Card card) {
		return false;
	}
	
	//safeties
	
	public boolean canCounteractCrevaison() {
		return false;
	}

	public boolean canCounteractPanneSeche() {
		return false;
	}
	
	public boolean canCounteractAccident() {
		return false;
	}
	
	public boolean canCounteractSpeedLimit() {
		return false;
	}
	
	public boolean canCounteractStop(){
		return false;
	}
	
	public boolean canBeCounteractBy(Card card)
	{
		return false;
	}
	
	public void setToPlayerAsMalus(User user) throws WrongCardForSuchSlotException, PlayerCantHaveMoreMalusException {
		throw new WrongCardForSuchSlotException();
	}
	
	public void advancePlayer(User user) throws CannotPlayThisCardException {
		throw new CannotPlayThisCardException();
	}
	
	public void curePlayer(User user) throws CannotPlayThisCardException {
		if (!user.getMalus().canBeCuredBy(this)) {
			throw new CannotPlayThisCardException();
		}
		user.setMalus(null);
	}
}