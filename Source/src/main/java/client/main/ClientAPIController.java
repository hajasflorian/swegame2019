package client.main;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import reactor.core.publisher.Mono;

public class ClientAPIController {

	private PlayerRegistration playerReg;
	private UniquePlayerIdentifier uniqueID = new UniquePlayerIdentifier();


	public UniquePlayerIdentifier registerPlayer(WebClient baseWebClient, String gameId) {
		playerReg = new PlayerRegistration("Florian", "Hajas", "1207533");

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromObject(playerReg)) // specify the data which is set to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		// WebClient support asynchronous message exchange, in SE1 we use a synchronous
		// one for the sake of simplicity. So calling block is fine.
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();


		if (resultReg.getState() == ERequestState.Error) {
			// typically happens if you forgot to create a new game before the client
			// execution or
			// forgot to adapt the run configuration so that it supplies the id of the new
			// game to the client
			// open http://swe.wst.univie.ac.at:18235/games in your browser to create a new
			// game and obtain its game id
			System.out.println("Client error, errormessage:" + resultReg.getExceptionMessage());
		} else {
			uniqueID = resultReg.getData().get();
			System.out.println("My Player ID:" + uniqueID.getUniquePlayerID());
		}
		return uniqueID;
	}

}
