package controllers;

import middlewares.InGame;
import middlewares.MyTurn;
import models.Game;
import models.User;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.With;
import cards.Card;
import cards.Deck;

import commands.CardReceiveCommand;

import exceptions.GameAlreadyStartedException;

@Transactional
@Authenticated(Secured.class)
@With({InGame.class, MyTurn.class})
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
		User currentUser = currentUser();
		Game game = currentUser.getGame();
		if (currentUser.getGame().getPlayers().get(0) != currentUser) {
			return badRequest(jsonError("Only the creator of the game can start it"));
		}
		try {
			currentUser.getGame().setStarted(true);
		} catch (GameAlreadyStartedException e) {
			return badRequest(jsonError("The game has already started"));
		}
		
		Deck deck = new Deck();
		deck.shuffle();
		game.setDeck(deck);
		Card card;
		for (int i = 0; i < 6; i++) {
			for (User u: game.getPlayers()) {
				card = deck.takeOne();
				u.addCard(card);
				sendCommandToUser(game, u, new CardReceiveCommand(card));
			}
		}
		sendFinalize();
		return ok(jsonInfo("game started"));
	}
}
