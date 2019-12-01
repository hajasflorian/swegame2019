package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import reactor.core.publisher.Mono;

public class Network {
 
	UniquePlayerIdentifier uniqueID = new UniquePlayerIdentifier();
//	private WebClient baseWebClient;
	private final Logger log = LoggerFactory.getLogger(Network.class);

	
//	public Network(WebClient webclient) {
//		this.baseWebClient = webclient;
//	}

	public UniquePlayerIdentifier registerPlayer(String serverBaseUrl, String gameId, PlayerRegistration playerRegistration) {
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses XML
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromObject(playerRegistration))
				.retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			log.error("Client error, errormessage" + resultReg.getExceptionMessage());
		} else {
			uniqueID = resultReg.getData().get();
			log.info("My Player ID:" + uniqueID.getUniquePlayerID());
		}

		return uniqueID;
	}
	
	public ResponseEnvelope<GameState> getState(String baseUrl, String gameId, String playerId) throws Exception {

		WebClient baseWebClient = WebClient.builder().baseUrl(baseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class); 

		ResponseEnvelope<GameState> requestResult = webAccess.block();

		return requestResult;
	}

	public void postMap(String serverBaseUrl, String gameID, HalfMap halfmap) {
		
		WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
																							// XML
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
				.body(BodyInserters.fromObject(halfmap)).retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<ERequestState> resultHalfMap = webAccess.block();

		if (resultHalfMap.getState() == ERequestState.Error) {
			log.error("Client error, errormessage: " + resultHalfMap.getExceptionMessage());
		} else {
			log.info("HalfMap request was confirmed successfully");
		}

	}
	


}
