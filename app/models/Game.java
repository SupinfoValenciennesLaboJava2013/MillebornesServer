package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import exceptions.AlreadyInGameException;
import play.db.jpa.JPA;

@Entity
public class Game {
	
	@Id
	private long id;
	
	@Column
	private String name;
	
	@OneToMany(mappedBy="game")
	private List<User> players;

	public void addPlayer(User user) throws AlreadyInGameException {
		user.setGame(this);
		this.players.add(user);
	}
	
	public void removePlayer(User user) {
		if (this.players.contains(user)) {
			user.removeGame();
			this.players.remove(user);
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
