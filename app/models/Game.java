package models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;

import play.db.jpa.JPA;
import exceptions.AlreadyInGameException;
import exceptions.TooManyPlayersException;

@Entity
public class Game {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String name;
	
	@OneToMany(mappedBy="game", fetch=FetchType.LAZY)
	private List<User> players;

	public Game() {
		this.players = new LinkedList<User>();
	}
	
	public void addFirstPlayer(User user) throws AlreadyInGameException {
		user.setGame(this);
		this.players.add(user);
	}
	
	public void addPlayer(User user) throws AlreadyInGameException, TooManyPlayersException {
		if (this.players.size() >= 5) {
			throw new TooManyPlayersException();
		}
		user.setGame(this);
		this.players.add(user);
	}
	
	public void removePlayer(User user) {
		if (this.players.contains(user)) {
			user.removeGame();
			this.players.remove(user);
			if (this.players.size() == 0) {
				JPA.em().remove(this);
			}
		}
	}
	
	public static Game findById(long id) {
		return (Game)JPA.em().find(Game.class, id);
	}
	
	public static void save(Game game) {
		if (game.getId() == 0) {
			JPA.em().persist(game);
		} else {
			JPA.em().merge(game);
		}
	}
	
	public long getId() {
		return id;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
