package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import reactor.core.publisher.Mono;

public class MainClient {

	private final static Logger log = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) {
		
		String serverBaseUrl = args[1];
		String gameId = args[2];

		Network controller = new Network();

		UniquePlayerIdentifier uniqueID = controller.registerPlayer(serverBaseUrl, gameId);
		String uniquePlayerID = uniqueID.getUniquePlayerID();

		if (!(uniquePlayerID.isEmpty())) {

			while (true) {
				try {
					// wait 0,4 seconds
					Thread.sleep(400);
					ResponseEnvelope<GameState> response = controller.getState(serverBaseUrl, gameId, uniquePlayerID, uniqueID);

					if (response.getState() == ERequestState.Error) {
						log.error("Client error, errormessage:" + response.getExceptionMessage());
						return;
					} else {
						GameState state = response.getData().get();
						log.info("Client gameStateID: " + state.getGameStateId() + ", PlayerInformation"
								+ state.getPlayers().toString());
						if (state.getPlayers().iterator().next().getState().equals(EPlayerGameState.ShouldActNext)) {
							log.info("My turn: I post my Map");
							controller.postMap(serverBaseUrl, gameId, uniqueID);
							return;
						} else {
							log.info("NOT my turn!");
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}




}
