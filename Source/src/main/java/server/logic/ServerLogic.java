package server.logic;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
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
import server.exceptions.InvalidGameIdException;

public class ServerLogic {
	
	private Map<String, ServerGameInformation> games = new HashMap<>();

	public String createNewGame(int len) {
		ServerGameInformation gameInfo = new ServerGameInformation();
		final String alphaNumericContainer = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder strgbldr = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			strgbldr.append(alphaNumericContainer.charAt(rnd.nextInt(alphaNumericContainer.length())));

		String gameID = strgbldr.toString();
		
		gameInfo.setGameID(gameID);
		games.put(gameID,gameInfo);
		return gameID;
	}

	public boolean isGameIdValid(String gameID) {
		return games.containsKey(gameID);
	}

	public UniquePlayerIdentifier newPlayerCreation(String gameID, PlayerRegistration playerRegistration) {
		UniquePlayerIdentifier playerID;

		if (isGameIdValid(gameID)) {
			if(games.get(gameID).getPlayer1() == null) {
				playerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
				Player player1 = new Player(playerID.getUniquePlayerID());
				player1.setPlayerFirstName(playerRegistration.getStudentFirstName());
				player1.setPlayerLastName(playerRegistration.getStudentLastName());
				player1.setPlayerStudentID(playerRegistration.getStudentID());
				player1.setYourTurn(false);
				games.get(gameID).setPlayer1(player1);
			} else if(games.get(gameID).getPlayer2() == null) {
				playerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
				Player player2 = new Player(playerID.getUniquePlayerID());
				player2.setPlayerFirstName(playerRegistration.getStudentFirstName());
				player2.setPlayerLastName(playerRegistration.getStudentLastName());
				player2.setPlayerStudentID(playerRegistration.getStudentID());
				player2.setYourTurn(true);
				games.get(gameID).setPlayer2(player2);
				System.out.println("Player1: " + games.get(gameID).getPlayer1().getId() + ", Player2: " + games.get(gameID).getPlayer2().getId());
			} else {
				throw new RuntimeException("Game is full");
			}
		} else {
			throw new InvalidGameIdException("Name: InvalidGameIdException",
					"Message: The game ID used during the registration is not exists or not valid anymore!");
		}
		return playerID;
	}

	public boolean isPlayerIdValid(String gameID, String playerID) {
//		System.out.println("GameID: " + gameID + ", PlayerID: " + playerID + ", Player1: " + games.get(gameID).getPlayer1().getId() + ", Player2: " + games.get(gameID).getPlayer2().getId());
//		System.out.println(games.get(gameID));
//		System.out.println(games.get(gameID).getActiveplayer().getId());
		return games.get(gameID).isPlayerValid(playerID);
		
	}
	
	public GameState setUpGameState(String gameID, String playerID) {
		Collection<PlayerState> playerStateCollection = new ArrayList<>();
		Player player1 = games.get(gameID).getPlayer1();
		if(player1 != null) {
			System.out.println("Player1 should act next: " + player1.isYourTurn());
			if(player1.isYourTurn()) {
				if(player1.getId().equals(playerID)) {
					PlayerState playerState1 = new PlayerState(player1.getPlayerFirstName(), player1.getPlayerLastName(), player1.getPlayerStudentID(), EPlayerGameState.ShouldActNext, new UniquePlayerIdentifier(playerID), false);
					playerStateCollection.add(playerState1);
				} else {
					PlayerState playerState1 = new PlayerState(player1.getPlayerFirstName(), player1.getPlayerLastName(), player1.getPlayerStudentID(), EPlayerGameState.ShouldActNext, UniquePlayerIdentifier.random(), false);
					playerStateCollection.add(playerState1);
				}
			} else {
				if(player1.getId().equals(playerID)) {
					PlayerState playerState1 = new PlayerState(player1.getPlayerFirstName(), player1.getPlayerLastName(), player1.getPlayerStudentID(), EPlayerGameState.ShouldWait, new UniquePlayerIdentifier(playerID), false);
					playerStateCollection.add(playerState1);
				} else {
					PlayerState playerState1 = new PlayerState(player1.getPlayerFirstName(), player1.getPlayerLastName(), player1.getPlayerStudentID(), EPlayerGameState.ShouldWait, UniquePlayerIdentifier.random(), false);
					playerStateCollection.add(playerState1);
				}
			}
		}
		Player player2 = games.get(gameID).getPlayer2();
		if(player2 != null) {
			System.out.println("Player2 should act next: " + player2.isYourTurn());
			if(player2.isYourTurn()) {
				if(player2.getId().equals(playerID)) {
					PlayerState playerState2 = new PlayerState(player2.getPlayerFirstName(), player2.getPlayerLastName(), player2.getPlayerStudentID(), EPlayerGameState.ShouldActNext, new UniquePlayerIdentifier(playerID), false);
					playerStateCollection.add(playerState2);
				} else {
					PlayerState playerState2 = new PlayerState(player2.getPlayerFirstName(), player2.getPlayerLastName(), player2.getPlayerStudentID(), EPlayerGameState.ShouldActNext, UniquePlayerIdentifier.random(), false);
					playerStateCollection.add(playerState2);
				}
			} else {
				if(player2.getId().equals(playerID)) {
					PlayerState playerState2 = new PlayerState(player2.getPlayerFirstName(), player2.getPlayerLastName(), player2.getPlayerStudentID(), EPlayerGameState.ShouldWait, new UniquePlayerIdentifier(playerID), false);
					playerStateCollection.add(playerState2);
				} else {
					PlayerState playerState2 = new PlayerState(player2.getPlayerFirstName(), player2.getPlayerLastName(), player2.getPlayerStudentID(), EPlayerGameState.ShouldWait, UniquePlayerIdentifier.random(), false);
					playerStateCollection.add(playerState2);
				}
			}
		}
		HashMap<Point, TerrainType> map = games.get(gameID).getFullMap();
//		System.out.println("FullMap size: " + map.size());
		GameState gameState = null;
		if(map != null) {
			Optional<FullMap> fullMap = convertToFullMap(gameID, playerID, map);
			gameState = new GameState(fullMap ,playerStateCollection, games.get(gameID).getGameStateID());
		} else {
			gameState = new GameState(playerStateCollection, games.get(gameID).getGameStateID());
		}
		
//		System.out.println(player1.isYourTurn() + "" + player2.isYourTurn());
		System.out.println(playerStateCollection.size());
		
		return gameState;
	}
	
	public void togglePlayer(String gameID) {
		if(games.get(gameID).getPlayer1().isYourTurn()) {
			games.get(gameID).getPlayer1().setYourTurn(false);
			games.get(gameID).getPlayer2().setYourTurn(true);
		} else {
			games.get(gameID).getPlayer1().setYourTurn(true);
			games.get(gameID).getPlayer2().setYourTurn(false);
		}
	}
	
	public void createGameStateId(String gameID) {
		games.get(gameID).setGameStateID(UUID.randomUUID().toString());
	}
	
	public boolean isMapValid(String gameID, HalfMap halfMap) {
		if(halfMap.getNodes().size() == 32) {
			HashMap<Point, TerrainType> hashMap = convertToHalfMap(halfMap);
			HashMap<Point, TerrainType> halfMap1 = games.get(gameID).getHalfMap1();
			if(halfMap1 == null) {
				games.get(gameID).setHalfMap1(hashMap);
				hashMap.forEach((k,v) -> {
					if(k.getFortPresent()) games.get(gameID).setPlayer1Position(k);
				});
				games.get(gameID).setFullMap(hashMap);
			} else {
				games.get(gameID).setHalfMap2(hashMap);
				hashMap.forEach((k,v) -> {
					if(k.getFortPresent()) games.get(gameID).setPlayer2Position(k);
				});
				combineMap(gameID);
			}
			return true;
		}
		return false;
	}
	
	private HashMap<Point, TerrainType> convertToHalfMap(HalfMap halfMap) {
		HashMap<Point, TerrainType> halfHashMap = new HashMap<>();
		Collection<HalfMapNode> halfMapNodes = halfMap.getNodes();
		Iterator<HalfMapNode> it = halfMapNodes.iterator();
		if(it.hasNext()) {
			HalfMapNode node = halfMapNodes.iterator().next();
			Point point = new Point(node.getX(), node.getY(), node.isFortPresent());
			if(node.getTerrain().equals(ETerrain.Grass)) {
				halfHashMap.put(point, TerrainType.Grass);
			} else if (node.getTerrain().equals(ETerrain.Mountain)) {
				halfHashMap.put(point, TerrainType.Mountain);
			} else {
				halfHashMap.put(point, TerrainType.Water);
			}
		}
		return halfHashMap;
	}
	
	private Optional<FullMap> convertToFullMap(String gameID, String playerID, HashMap<Point, TerrainType> fullMap) {
		Collection<FullMapNode> fullMapNodes = new ArrayList<FullMapNode>();
		System.out.println("Size of the fullmap: " +  fullMap.size());
		if(fullMap.entrySet().iterator().hasNext()) {
			Entry<Point, TerrainType> set = fullMap.entrySet().iterator().next();
			Point p = set.getKey();
			if(playerID.equals(games.get(gameID).getPlayer1().getId())) {
				if(set.getValue().equals(TerrainType.Grass)) {
						if(p.equals(games.get(gameID).getPlayer1Position())) {
							FullMapNode node = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPosition, ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, p.getX(), p.getY());
							fullMapNodes.add(node);
						} else {
							FullMapNode node = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, p.getX(), p.getY());
							fullMapNodes.add(node);
						}
				} else if(set.getValue().equals(TerrainType.Mountain)) {
					if(p.equals(games.get(gameID).getPlayer1Position())) {
						FullMapNode node = new FullMapNode(ETerrain.Mountain, EPlayerPositionState.MyPosition, ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, p.getX(), p.getY());
						fullMapNodes.add(node);
					} else {
						FullMapNode node = new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, p.getX(), p.getY());
						fullMapNodes.add(node);
					}
				} else {
					if(p.equals(games.get(gameID).getPlayer1Position())) {
						FullMapNode node = new FullMapNode(ETerrain.Water, EPlayerPositionState.MyPosition, ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, p.getX(), p.getY());
						fullMapNodes.add(node);
					} else {
						FullMapNode node = new FullMapNode(ETerrain.Water, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, p.getX(), p.getY());
						fullMapNodes.add(node);
					}
				}
			} else if(set.getValue().equals(TerrainType.Grass)) {
				if(p.equals(games.get(gameID).getPlayer1Position())) {
					FullMapNode node = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPosition, ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, p.getX(), p.getY());
					fullMapNodes.add(node);
				} else {
					FullMapNode node = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, p.getX(), p.getY());
					fullMapNodes.add(node);
				}
			} else {
				
			}
		}
		Optional<FullMap> resultfullMap = Optional.of(new FullMap(fullMapNodes));
		return resultfullMap;
	}
	
	private boolean toCombineMapVertical( ) {
		Random rnd = new Random();
		int number = rnd.nextInt(2);
		if(number == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	00 01 02 03
	10 11 12 13
	
	
	public void combineMap(String gameID) {
		HashMap<Point, TerrainType> halfMap2= games.get(gameID).getHalfMap2();
		HashMap<Point, TerrainType> fullMap= games.get(gameID).getFullMap();
		//TODO combine map is now default
		if(toCombineMapVertical()) {
			//TODO add y +8
			halfMap2.forEach((k, v) -> {
				System.out.println("X: " + k.getX() + ", Y: " + k.getY());
				fullMap.put(k, v);
			});
		} else {
			//TODO add x +4
			halfMap2.forEach((k, v) -> {
				System.out.println("X: " + k.getX() + ", Y: " + k.getY());
				fullMap.put(k, v);
			});
		}
	}
}
