package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.jpa.JPA;
import exceptions.AlreadyInGameException;

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
	
}
