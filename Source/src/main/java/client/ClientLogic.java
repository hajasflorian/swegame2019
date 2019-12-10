package client;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import map.Point;
import map.TerrainType;
import network.Converter;
import player.Player;
import player.PlayerGameInformation;

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

	public void updateCollectedTreasure(GameState state, String uniquePlayerID) {
		for (PlayerState player : state.getPlayers()) {
			if (player.getUniquePlayerID().equals(uniquePlayerID) && player.hasCollectedTreasure()) {
				playerGameInformation.setCollectedTreasure(true);
			} else {
				playerGameInformation.setCollectedTreasure(false);
			}
		}
	}

	public void updateMapState(GameState state) {
		FullMap fullmap = state.getMap().get();
		for (FullMapNode nodes : fullmap.getMapNodes()) {
			EFortState fortState = nodes.getFortState();
			ETreasureState treasureState = nodes.getTreasureState();
			EPlayerPositionState playerPositionState = nodes.getPlayerPositionState();
			ETerrain terrainType = nodes.getTerrain();

			switch (fortState) {
			case MyFortPresent:
				if (playerGameInformation.getMyFortPosition().getX() != nodes.getX()
						&& playerGameInformation.getMyFortPosition().getY() != nodes.getY()) {
					Point myFort = new Point(nodes.getX(), nodes.getY(), true);
					playerGameInformation.setMyFortPosition(myFort);
				}
				break;
			case EnemyFortPresent:
				if (playerGameInformation.getEnemyFortPosition().getX() != nodes.getX()
						&& playerGameInformation.getEnemyFortPosition().getY() != nodes.getY()) {
					Point enemyFort = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setEnemyFortPosition(enemyFort);
				}
				break;
			case NoOrUnknownFortState:
				break;
			default:
				break;
			}

			switch (treasureState) {
			case MyTreasureIsPresent:
				if (playerGameInformation.getTreasurePosition().getX() != nodes.getX()
						&& playerGameInformation.getTreasurePosition().getY() != nodes.getY()) {
					Point myTreasure = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setTreasurePosition(myTreasure);
				}
				break;
			case NoOrUnknownTreasureState:
				break;
			default:
				break;
			}

			switch (playerPositionState) {
			case MyPosition:
				if (playerGameInformation.getmyPosition().getX() != (nodes.getX())
						&& playerGameInformation.getmyPosition().getY() != nodes.getY()) {
					Point myPosition = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setmyPosition(myPosition);
				}
				break;
			case EnemyPlayerPosition:
				if (playerGameInformation.getEnemyPosition().getX() != (nodes.getX())
						&& playerGameInformation.getEnemyPosition().getY() != nodes.getY()) {
					Point myPosition = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setEnemyPosition(myPosition);
				}
				break;
			case BothPlayerPosition:
				if ((playerGameInformation.getEnemyPosition().getX() != (nodes.getX()))
						&& (playerGameInformation.getEnemyPosition().getY() != nodes.getY())
						&& ((playerGameInformation.getmyPosition().getX() != (nodes.getX()))
								&& (playerGameInformation.getmyPosition().getY() != nodes.getY()))) {
					Point point = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setmyPosition(point);
					playerGameInformation.setEnemyPosition(point);
				}
				break;
			case NoPlayerPresent:
				break;
			default:
				break;
			}

			switch (terrainType) {
			case Water:
				if (playerGameInformation.getMap().isEmpty()) {
					HashMap<Point, TerrainType> map = new HashMap<Point, TerrainType>();
					map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Water);
					playerGameInformation.setMap(map);
					break;
				} else {
					HashMap<Point, TerrainType> map = playerGameInformation.getMap();
					Boolean[] found = { false };
					map.forEach((k, v) -> {
						if (k.getX() == nodes.getX() && k.getY() == nodes.getY()
								&& v.toString().equals(nodes.getTerrain().toString())) {
							found[0] = true;
							return;
						} else {
							return;
						}
					});
					if (!found[0]) {
						map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Water);
						playerGameInformation.setMap(map);
					}
					break;
				}
			case Mountain:
				if (playerGameInformation.getMap().isEmpty()) {
					HashMap<Point, TerrainType> map = new HashMap<Point, TerrainType>();
					map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Mountain);
					playerGameInformation.setMap(map);
					break;
				} else {
					HashMap<Point, TerrainType> map = playerGameInformation.getMap();
					Boolean[] found = { false };
					map.forEach((k, v) -> {
						if (k.getX() == nodes.getX() && k.getY() == nodes.getY()
								&& v.toString().equals(nodes.getTerrain().toString())) {
							found[0] = true;
							return;
						} else {
							return;
						}
					});
					if (!found[0]) {
						map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Mountain);
						playerGameInformation.setMap(map);
					}
					break;
				}
			case Grass:
				if (playerGameInformation.getMap().isEmpty()) {
					HashMap<Point, TerrainType> map = new HashMap<Point, TerrainType>();
					map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Grass);
					playerGameInformation.setMap(map);
					break;
				} else {
					HashMap<Point, TerrainType> map = playerGameInformation.getMap();
					Boolean[] found = { false };
					map.forEach((k, v) -> {
						if (k.getX() == nodes.getX() && k.getY() == nodes.getY()
								&& v.toString().equals(nodes.getTerrain().toString())) {
							found[0] = true;
							return;
						} else {
							return;
						}
					});
					if (!found[0]) {
						map.put(new Point(nodes.getX(), nodes.getY(), false), TerrainType.Grass);
						playerGameInformation.setMap(map);
					}
					break;
				}
			default:
				break;
			}

		}

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

		boolean isGameOver = false;
		int gameRoundCounter = 1;
		while (isGameOver == false) {
			try {
				// wait 0,4 seconds
				Thread.sleep(400);
				GameState gamestate = converter.convertPlayerStateResponse(baseUrl, gameId, playerId);
				EPlayerGameState playerState = getPlayerState(gamestate, playerId);
				if (playerState.equals(EPlayerGameState.ShouldActNext)) {
					log.info("My turn: I make my move");
					updateMapState(gamestate);
					updateCollectedTreasure(gamestate, playerId);
					System.out.println("My fort position is : " + playerGameInformation.getMyFortPosition().getX() + ", "
							+ playerGameInformation.getMyFortPosition().getY());
					System.out.println("My position is : " + playerGameInformation.getmyPosition().getX() + ", "
							+ playerGameInformation.getmyPosition().getY());
					System.out.println("My treasure position is : " + playerGameInformation.getTreasurePosition().getX() + ", "
							+ playerGameInformation.getTreasurePosition().getY());
					System.out.println("The enemy position is : " + playerGameInformation.getEnemyPosition().getX() + ", "
							+ playerGameInformation.getEnemyPosition().getY());
//					if (playerGameInformation.isCollectedTreasure() == false) {
//						calculateLookForTreasureMove();
//					}

				} else if (playerState.equals(EPlayerGameState.Won)) {
					log.info("Juhu. I won - enemy lost");
					isGameOver = true;
				} else if (playerState.equals(EPlayerGameState.Lost)) {
					log.info("I lost");
					isGameOver = true;
				} else {
					log.info("I am waiting");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();

			}

		}

	}

	public void calculateLookForTreasureMove() {
		HashMap<Point, TerrainType> map = playerGameInformation.getMap();
		map.forEach((k, v) -> {
			System.out.println(k.getX() + "," + k.getY() + ", fort: " + k.getFortPresent() + ", " + v);
		});

	}

}
