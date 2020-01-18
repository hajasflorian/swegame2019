package network;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.ERequestState;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import exceptions.RegistrationException;
import map.MapGenerator;
import map.Point;
import map.TerrainType;

public class Converter {

	private Network network = new Network();
	private MapGenerator mapGenerator = new MapGenerator();

	private final Logger log = LoggerFactory.getLogger(Converter.class);

	public UniquePlayerIdentifier convertRegistrationResponse(String baseUrl, String gameId,
			PlayerRegistration pRegistration) {
		try {
			ResponseEnvelope<UniquePlayerIdentifier> responseReg = network.registerPlayer(baseUrl, gameId,
					pRegistration);
			UniquePlayerIdentifier uniqueIdentifier = responseReg.getData().get();

			if (responseReg.getState() == ERequestState.Error) {
				throw new RegistrationException(responseReg.getExceptionMessage());

			} else {
				log.info("My Player ID:" + uniqueIdentifier.getUniquePlayerID());
			}
			return uniqueIdentifier;
		} catch (RegistrationException e) {
			log.error("Client error, errormessage" + e.getMessage());
			throw e;
		}
	}

	public GameState convertPlayerStateResponse(String baseUrl, String gameId, String playerId) {
		ResponseEnvelope<GameState> response;
		GameState state = null;
		try {
			response = network.getState(baseUrl, gameId, playerId);
			state = response.getData().get();
			if (response.getState() == ERequestState.Error) {
				log.error("Client error, errormessage:" + response.getExceptionMessage());

			} else {
				if (state.getMap().isPresent()) {
					log.info("Client gameStateID: " + state.getGameStateId() + ", PlayerInformation:"
							+ state.getPlayers().toString() + "MapInformation" + state.getMap().get());
//					FullMap fullmap = state.getMap().get();
//					for (FullMapNode nodes : fullmap.getMapNodes()) {
//						log.debug(nodes.getX() + ", " + nodes.getY() + ", " + nodes.getTerrain());
//						if (nodes.getPlayerPositionState().equals(EPlayerPositionState.MyPosition)) {
//							log.debug("My Position" + nodes.getX() + nodes.getY());
//						}
//					}
				} else {
					log.info("Client gameStateID: " + state.getGameStateId() + ", PlayerInformation:"
							+ state.getPlayers().toString());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return state;
	}

	public void convertPostMapResponse(String baseUrl, String gameId, HalfMap halfmap) {
		ResponseEnvelope<ERequestState> responsePostMap = network.postMap(baseUrl, gameId, halfmap);

		if (responsePostMap.getState() == ERequestState.Error) {
			log.error("Client error, errormessage: " + responsePostMap.getExceptionMessage());
		} else {
			log.info("HalfMap request was confirmed successfully");
		}
	}

	public HalfMap convertMap(String playerId) {
			Collection<HalfMapNode> halfMapNodes = new HashSet<HalfMapNode>();
			HashMap<Point, TerrainType> map = mapGenerator.createMap();

			map.forEach((k, v) -> {
				switch (v) {
				case Grass:
					halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), k.getFortPresent(), ETerrain.Grass));
					break;
				case Water:
					halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Water));
					break;
				case Mountain:
					halfMapNodes.add(new HalfMapNode(k.getX(), k.getY(), false, ETerrain.Mountain));
					break;
				default:
					break;
				}
			});

			HalfMap halfmap = new HalfMap(playerId, halfMapNodes);

		return halfmap;
	}

	public void convertPostMoveResponse(String baseUrl, String gameId, PlayerMove move) {
		ResponseEnvelope<ERequestState> responseMove = network.postMove(baseUrl, gameId, move);

		if (responseMove.getState() == ERequestState.Error) {
			log.error("Client error, errormessage: " + responseMove.getExceptionMessage());
		} else {
			log.info("Move was done successfully.");
		}
	}

}
