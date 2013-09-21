package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.jpa.JPA;
import cards.Card;
import cards.SafetiesCard;
import exceptions.AlreadyInGameException;
import exceptions.CannotPlayThisCardException;
import exceptions.CannotPlayThisRemedyCardException;
import exceptions.PlayerCantHaveMoreMalusException;
import exceptions.WrongCardForSuchSlotException;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String username;
	
	@Column
	@JsonIgnore
	private String password;
	
	@ManyToOne
	@JsonIgnore
	private Game game;
	
	public void removeGame() {
		this.game = null;
	}
	
	public boolean inGame() {
		return this.game != null;
	}
	
	public void setGame(Game game) throws AlreadyInGameException {
		if (this.game != null) {
			throw new AlreadyInGameException();
		}
		this.game = game;
	}
	
	public static User findById(long id) {
		return JPA.em().find(User.class, id);
	}
	
	public static User findByUsername(String username) {
		try {
			return (User)JPA.em()
					.createQuery("SELECT u FROM User u WHERE u.username = ?")
					.setParameter(1, username)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	public static User findByUsernameAndPassword(String username, String password) {
		User user = findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		} else {
			return null;
		}
	}
	
	public static void save(User user) {
		if (user.getId() == 0) {
			JPA.em().persist(user);
		} else {
			JPA.em().merge(user);			
		}
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	@Transient
	private boolean hasGreenLight = false;

	@Transient
	private boolean speedLimited = false;
	
	@Transient
	private ArrayList<SafetiesCard> playerSafeties = new ArrayList<SafetiesCard>();
	
	public void setSpeedLimited(boolean arg) {
		this.speedLimited = arg;
	}
	
	public void purge() {
		this.malus = null;
		this.speedLimited = false;
	}
	
	// Distance parcourue, en .... km !!
	@Transient
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
	@Transient
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
	
	@Transient
	private List<Card> cards = new LinkedList<Card>();
	
	public void addCard(Card card) {
		this.cards.add(card);
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
