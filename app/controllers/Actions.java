package controllers;

import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@Transactional
@Authenticated(Secured.class)
public class Actions extends SuperController {
	
	/**
	 * Jouer une carte pour soi même
	 * @return
	 */
	public static Result playCard(/* Card card */) {
		return status(501, jsonError("Not yet implemented"));
	}
	
	/**
	 * Jouer une carte pour attaquer un joueur en particulier
	 */
	public static Result attack(/* Card card, long targetId */) {
		return status(501, jsonError("Not yet implemented"));
	}
	
	/**
	 * Permet de répondre à une extension de durée de la partie
	 * de 700km à 1000km
	 * @return
	 */
	public static Result extendDuration() {
		return status(501, jsonError("Not yet implemented"));
	}
	
	/**
	 * Commence la partie
	 */
	public static Result start() {
		return status(501, jsonError("Not yet implemente"));
	}
}
