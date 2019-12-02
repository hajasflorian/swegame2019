package client;

import static org.hamcrest.CoreMatchers.is;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import network.Converter;
import player.Player;

public class ClientLogic {

	private final static Logger log = LoggerFactory.getLogger(ClientLogic.class);
	private Converter converter = new Converter();
	private Player player = new Player();
	private PlayerGameInformation playerGameInformation;
	
	public ClientLogic(PlayerGameInformation playerGameInformation) {
		this.playerGameInformation = playerGameInformation;
	}

	public void registerToGame(String baseUrl, String gameId) {
		PlayerRegistration pRegistration = new PlayerRegistration(player.getStudentFirstName(),
				player.getStudentLastName(), player.getStudentId());
		UniquePlayerIdentifier uniqueIdentifier = converter.convertRegistrationResponse(baseUrl, gameId, pRegistration);
		String playerId = uniqueIdentifier.getUniquePlayerID();
		playerGameInformation.setPlayerId(playerId);

		if (!(playerId.isEmpty())) {
			playGame(baseUrl, gameId, playerId);
		}
	}

	public EPlayerGameState getPlayerState(GameState state, String uniquePlayerID) {
		for (PlayerState player : state.getPlayers()) {
			if (player.getUniquePlayerID().equals(uniquePlayerID)) {
				return player.getState();
			}
		}
		return null;
	}

	public void playGame(String baseUrl, String gameId, String playerId) {
		boolean isMapNotSent = true;
		while (isMapNotSent) {
			try {
				// wait 0,4 seconds
				Thread.sleep(400);
				GameState gamestate = converter.convertPlayerStateResponse(baseUrl, gameId, playerId);
				EPlayerGameState playerState = getPlayerState(gamestate, playerId);
				if (playerState.equals(EPlayerGameState.ShouldActNext)) {
					log.info("My turn: I post my Map");
					HalfMap halfmap = converter.convertMap(playerId);
					converter.convertPostMapResponse(baseUrl, gameId, halfmap);
					isMapNotSent = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
//		boolean isClientWon = true;
//		int gameRoundCounter = 0;
//		while (isClientWon) {
//			try {
//				// wait 0,3 seconds
//				Thread.sleep(300);
//				GameState gamestate = converter.convertPlayerStateResponse(baseUrl, gameId, playerId);
//				EPlayerGameState playerState = getPlayerState(gamestate, playerId);
//				if (playerState.equals(EPlayerGameState.ShouldActNext)) {
//					log.info("My turn: I make my move");
//					if (gameRoundCounter < 200) {
//						gameRoundCounter++;
//					}
//					isClientWon = false;
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//
//			}
//
//		}

	}

}
