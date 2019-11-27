package client.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import map.MapGenerator;
import map.Point;
import map.TerrainType;
import reactor.core.publisher.Mono;

public class Network {

	private PlayerRegistration playerReg;
	private UniquePlayerIdentifier uniqueID = new UniquePlayerIdentifier();
	private final Logger log = LoggerFactory.getLogger(Network.class);

	private MapGenerator mapGenerator = new MapGenerator();

	public UniquePlayerIdentifier registerPlayer(WebClient baseWebClient, String gameId) {
		playerReg = new PlayerRegistration("Florian", "Hajas", "1207533");

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromObject(playerReg)) // specify the data which is set to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		// WebClient support asynchronous message exchange, in SE1 we use a synchronous
		// one for the sake of simplicity. So calling block is fine.
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			// open http://swe.wst.univie.ac.at:18235/games in your browser to create a new
			log.error("Client error, errormessage" + resultReg.getExceptionMessage());
		} else {
			uniqueID = resultReg.getData().get();
			log.info("My Player ID:" + uniqueID.getUniquePlayerID());
		}

		return uniqueID;
	}

	public void postMap(WebClient baseWebClient, String gameID) {
		HalfMap halfmap = new HalfMap();
		Collection<HalfMapNode> nodes = new HashSet<HalfMapNode>();

		HashMap<Point, TerrainType> map = mapGenerator.createMap();
		map.forEach((k, v) -> {
			switch (v) {
			case GRASS:
				nodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Grass));
				break;
			case WATER:
				nodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Water));
				break;
			case MOUNTAIN:
				nodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Mountain));
				break;
			default:
				break;
			}
		});

		HalfMapNode halfmapNode = new HalfMapNode();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "halfmaps")
				.body(BodyInserters.fromObject(halfmap)).retrieve().bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<ERequestState> resultHalfMap = webAccess.block();

		if (resultHalfMap.getState() == ERequestState.Error) {
			log.error("Client error, errormessage" + resultHalfMap.getExceptionMessage());
		} else {
			log.info("HalfMap request was confirmed successfully");
		}

	}

}