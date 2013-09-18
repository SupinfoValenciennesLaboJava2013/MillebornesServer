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

import play.db.jpa.JPA;
import exceptions.AlreadyInGameException;
import exceptions.GameAlreadyStartedException;
import exceptions.TooManyPlayersException;

@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column
	private String name;

	@Column
	private boolean started;

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) throws GameAlreadyStartedException {
		if (this.isStarted()) {
			throw new GameAlreadyStartedException();
		}
		this.started = started;
	}

	@OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
	private List<User> players;

	public Game() {
		this.players = new LinkedList<User>();
	}

	public void addFirstPlayer(User user) throws AlreadyInGameException {
		user.setGame(this);
		this.players.add(user);
	}

	public void addPlayer(User user) throws AlreadyInGameException,
			TooManyPlayersException, GameAlreadyStartedException {
		if (this.isStarted()) {
			throw new GameAlreadyStartedException();
		}
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

	@SuppressWarnings("unchecked")
	public static List<Game> findAll() {
		return (List<Game>) JPA.em().createQuery("SELECT g FROM Game g")
				.getResultList();
	}

	public static Game findById(long id) {
		return (Game) JPA.em().find(Game.class, id);
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

	@Transient
	private User currentPlayer;

	public boolean isCurrentPlayer(User player) {
		return this.currentPlayer == player;
	}

	public User NextPlayer() {
		if (this.currentPlayer == null) {
			this.currentPlayer = this.players.get(0);
		} else {
			int index = this.players.indexOf(this.currentPlayer);
			if (index == this.players.size() - 1) {
				this.currentPlayer = this.players.get(0);
			} else {
				this.currentPlayer = this.players.get(index + 1);
			}
		}
		return this.currentPlayer;
	}
}
