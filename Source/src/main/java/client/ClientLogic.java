package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import network.Converter;
import network.Network;
import player.Player;

public class ClientLogic {
	private final static Logger log = LoggerFactory.getLogger(ClientLogic.class);
	private Network network = new Network();
	private Converter converter = new Converter();
	private Player player = new Player();

	public void registerToGame(String baseUrl, String gameId) {
		PlayerRegistration pRegistration = new PlayerRegistration(player.getStudentFirstName(), player.getStudentLastName(), player.getStudentId());
		
		UniquePlayerIdentifier uniqueIdentifier = network.registerPlayer(baseUrl, gameId, pRegistration);
		String playerId = uniqueIdentifier.getUniquePlayerID();
		
		if (!(playerId.isEmpty())) {

			while (true) {
				try {
					// wait 0,4 seconds
					Thread.sleep(400);
					ResponseEnvelope<GameState> response = network.getState(baseUrl, gameId, playerId);
					if (response.getState() == ERequestState.Error) {
						log.error("Client error, errormessage:" + response.getExceptionMessage());
						return;
					} else {
						GameState state = response.getData().get();
						log.info("Client gameStateID: " + state.getGameStateId() + ", PlayerInformation"
								+ state.getPlayers().toString());
						EPlayerGameState playerState = getPlayerState(state, playerId);
						if (playerState.equals(EPlayerGameState.ShouldActNext)) {
							log.info("My turn: I post my Map");
							HalfMap hafmap = converter.convertMap(playerId);
							network.postMap(baseUrl, gameId, hafmap);
							return;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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

}
