package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.EMove;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
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
						|| playerGameInformation.getMyFortPosition().getY() != nodes.getY()) {
					Point myFort = new Point(nodes.getX(), nodes.getY(), true);
					playerGameInformation.setMyFortPosition(myFort);
				}
				break;
			case EnemyFortPresent:
				if (playerGameInformation.getEnemyFortPosition().getX() != nodes.getX()
						|| playerGameInformation.getEnemyFortPosition().getY() != nodes.getY()) {
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
						|| playerGameInformation.getTreasurePosition().getY() != nodes.getY()) {
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
				if (playerGameInformation.getMyPosition().getX() != (nodes.getX())
						|| playerGameInformation.getMyPosition().getY() != nodes.getY()) {
					Point myPosition = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setmyPosition(myPosition);
				}
				break;
			case EnemyPlayerPosition:
				if (playerGameInformation.getEnemyPosition().getX() != (nodes.getX())
						|| playerGameInformation.getEnemyPosition().getY() != nodes.getY()) {
					Point myPosition = new Point(nodes.getX(), nodes.getY(), false);
					playerGameInformation.setEnemyPosition(myPosition);
				}
				break;
			case BothPlayerPosition:
				if ((playerGameInformation.getEnemyPosition().getX() != (nodes.getX()))
						|| (playerGameInformation.getEnemyPosition().getY() != nodes.getY())
								&& ((playerGameInformation.getMyPosition().getX() != (nodes.getX()))
										|| (playerGameInformation.getMyPosition().getY() != nodes.getY()))) {
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
//		int gameRoundCounter = 1;
		while (isGameOver == false) {
			try {
				// wait 0,4 seconds
				Thread.sleep(400);
				GameState gamestate = converter.convertPlayerStateResponse(baseUrl, gameId, playerId);
				EPlayerGameState playerState = getPlayerState(gamestate, playerId);
				updateMapState(gamestate);
				updateCollectedTreasure(gamestate, playerId);
				if (playerState.equals(EPlayerGameState.ShouldActNext)) {
					log.info("My turn: I make my move");
					log.debug("My fort position is : " + playerGameInformation.getMyFortPosition().getX()
							+ ", " + playerGameInformation.getMyFortPosition().getY());
					log.debug("My position is : " + playerGameInformation.getMyPosition().getX() + ", "
							+ playerGameInformation.getMyPosition().getY());
					log.debug("My treasure position is : " + playerGameInformation.getTreasurePosition().getX()
							+ ", " + playerGameInformation.getTreasurePosition().getY());
					log.debug("The enemy position is : " + playerGameInformation.getEnemyPosition().getX()
							+ ", " + playerGameInformation.getEnemyPosition().getY());
//					playerGameInformation.getMap().forEach((k,v)->{
//							System.out.println(k.getX() + ", " + k.getY() + ", " + v);
//					});
					if (playerGameInformation.isCollectedTreasure() == false) {
						if (playerGameInformation.getTreasurePosition().getX()==-1 && playerGameInformation.getTreasurePosition().getY()==-1) {
						EMove move = calculateLookForTreasureMove();
						PlayerMove pmove = new PlayerMove();
						pmove = PlayerMove.of(playerId, move);
						converter.convertPostMoveResponse(baseUrl, gameId, pmove);
						} else {
//							I will move to treasure position!
						}
						
					} else {
						log.info("I collected my treasure!");
						isGameOver = true;
					}

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

	public EMove calculateLookForTreasureMove() {
		setUpPossibleTreasurePositions();
//		HashMap<Point, TerrainType> map = playerGameInformation.getMap();
//		map.forEach((k, v) -> {
//			System.out.println(k.getX() + "," + k.getY() + ", fort: " + k.getFortPresent() + ", " + v);
//		});

		Point closest = getLowestDistance();
		
		HashMap<Point, TerrainType> neighbours = getNeighbours();
		
		log.debug("Closest possible treasure: " + closest.getX() + ", " + closest.getY());
		
		removePossibleTreasurePosition(closest);

		EMove move = null;
		
		neighbours.forEach((k, v) -> {
			if(closest.getX() == k.getX()  && closest.getY()==k.getY()) {
				
			}
		});
		
		if (closest.getX() > playerGameInformation.getMyPosition().getX()) {
			if(closest.getY() == playerGameInformation.getMyPosition().getY()) {
			log.info("My move: right, towards:" + closest.getX() + ", " + closest.getY());
			move = EMove.Right;
			}
		} else if (closest.getX() < playerGameInformation.getMyPosition().getX()
				&& closest.getY() == playerGameInformation.getMyPosition().getY()) {
			log.info("My move: left, towards:" + closest.getX() + ", " + closest.getY());
			move = EMove.Left;
		} else if (closest.getX() == playerGameInformation.getMyPosition().getX()
				&& closest.getY() > playerGameInformation.getMyPosition().getY()) {
			log.info("My move: down, towards:" + closest.getX() + ", " + closest.getY());
			move = EMove.Down;
		} else if (closest.getX() == playerGameInformation.getMyPosition().getX()
				&& closest.getY() < playerGameInformation.getMyPosition().getY()) {
			log.info("My move: up, towards:" + closest.getX() + ", " + closest.getY());
			move=  EMove.Up;
		} else {
			
		}
		
		//move validation
//		Boolean[] validMove = {true};
//		if(move==EMove.Right) {
//			int targetX = playerGameInformation.getMyPosition().getX() + 1;
//			int targetY = playerGameInformation.getMyPosition().getY();
//			map.forEach((k, v) -> {
//				if(k.getX() == targetX && k.getY() == targetY && v.equals(TerrainType.Water)) validMove[0] = false;
//			});
//			if(!validMove[0].booleanValue()) {
//				
//			}
//		}
		return move;
	}

	private void removePossibleTreasurePosition(Point closest) {
		ArrayList<Point> possibleTreasurePositions = playerGameInformation.getPossibleTreasurePositions();
		for (int i = 0; i < possibleTreasurePositions.size(); i++) {

			if (possibleTreasurePositions.get(i).getX() == closest.getX()
					&& possibleTreasurePositions.get(i).getY() == closest.getY()) {
				possibleTreasurePositions.remove(i);
			}
		}

	}

	private Point getLowestDistance() {
		int lowestMoveCost = 50;
		Point closestPoint = new Point(-1, -1, false);
		int myX = playerGameInformation.getMyPosition().getX();
		int myY = playerGameInformation.getMyPosition().getY();

		for (int i = 0; i < playerGameInformation.getPossibleTreasurePositions().size(); i++) {
			int possibleX = playerGameInformation.getPossibleTreasurePositions().get(i).getX();
			int possibleY = playerGameInformation.getPossibleTreasurePositions().get(i).getY();
			int actualCost = Math.abs(possibleX - myX) + Math.abs(possibleY - myY);
			if (actualCost < lowestMoveCost) {
				lowestMoveCost = actualCost;
				closestPoint = playerGameInformation.getPossibleTreasurePositions().get(i);
			}
		}
		return closestPoint;
	}

	public void setUpPossibleTreasurePositions() {
		ArrayList<Point> possibleTreasurePositions = playerGameInformation.getPossibleTreasurePositions();
//		System.out.println(possibleTreasurePositions.size());
		HashMap<Point, TerrainType> map = playerGameInformation.getMap();
		Point myPoint = playerGameInformation.getMyPosition();
		if (possibleTreasurePositions.isEmpty()) {
			map.forEach((k, v) -> {
				if (v.equals(TerrainType.Grass) && !(k.getX() == myPoint.getX() && k.getY() == myPoint.getY())) {
					possibleTreasurePositions.add(k);
				}
			});
		}

//		for (int i = 0; i < possibleTreasurePositions.size(); i++) {
//			System.out.println(possibleTreasurePositions.get(i).getX() + ", " + possibleTreasurePositions.get(i).getY());
//		}
		playerGameInformation.setPossibleTreasurePositions(possibleTreasurePositions);
	}
	
	public HashMap<Point, TerrainType> getNeighbours() {
		HashMap<Point, TerrainType> neighbours = new HashMap<Point, TerrainType>();
		HashMap<Point, TerrainType> map = playerGameInformation.getMap();
		int rightx = playerGameInformation.getMyPosition().getX()+1;
		int righty = playerGameInformation.getMyPosition().getY();
		int leftx = playerGameInformation.getMyPosition().getX()-1;
		int lefty = playerGameInformation.getMyPosition().getY();
		int upx = playerGameInformation.getMyPosition().getX();
		int upy = playerGameInformation.getMyPosition().getY()-1;
		int downx = playerGameInformation.getMyPosition().getX();
		int downy = playerGameInformation.getMyPosition().getY()+1;
		
		map.forEach((k, v) -> {
			//check right point
			if (k.getX() == rightx && k.getY() == righty) {
				if(!v.equals(TerrainType.Water)) {
					neighbours.put(k, v);
				}
			}
			
			//check left point
			if (k.getX() == leftx && k.getY() == lefty) {
				if(!v.equals(TerrainType.Water)) {
					neighbours.put(k,v);
				}
			}
			
			//check up point
			if (k.getX() == upx && k.getY() == upy) {
				if(!v.equals(TerrainType.Water)) {
					neighbours.put(k,v);
				}
			}
			
			//check down point
			if (k.getX() == downx && k.getY() == downy) {
				if(!v.equals(TerrainType.Water)) {
					neighbours.put(k,v);
				}
			}
		});
		
		return neighbours;
		
	}
	
	

}
