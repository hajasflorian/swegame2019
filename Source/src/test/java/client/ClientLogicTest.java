package client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import MessagesBase.ETerrain;
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
import player.PlayerGameInformation;

public class ClientLogicTest {

	private GameState gameState;
	private String myId;

	public void initialize() {
		UniquePlayerIdentifier playerN1id = UniquePlayerIdentifier.random();
		myId = playerN1id.getUniquePlayerID();
		UniquePlayerIdentifier playerN2id = UniquePlayerIdentifier.random();
		PlayerState playerN1State = new PlayerState("Florian", "Hajas", "01207533", EPlayerGameState.ShouldActNext,
				playerN1id, false);
		PlayerState playerN2State = new PlayerState("Florian", "Hajas", "01207533", EPlayerGameState.ShouldWait,
				playerN2id, false);
		List<PlayerState> playerStates = new ArrayList<PlayerState>();
		playerStates.add(playerN1State);
		playerStates.add(playerN2State);
		FullMapNode node1 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPosition,
				ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, 0, 0);
		FullMapNode node2 = new FullMapNode(ETerrain.Water, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 0, 1);
		FullMapNode node3 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.MyTreasureIsPresent, EFortState.NoOrUnknownFortState, 5, 3);
		FullMapNode node4 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 0, 4);
		FullMapNode node5 = new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 1, 0);
		FullMapNode node6 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.EnemyPlayerPosition,
				ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 4, 2);
		FullMapNode node7 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent,
				ETreasureState.NoOrUnknownTreasureState, EFortState.EnemyFortPresent, 6, 2);
		List<FullMapNode> nodes = new ArrayList<FullMapNode>();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		nodes.add(node6);
		nodes.add(node7);
		FullMap map = new FullMap(nodes);
		Optional<FullMap> fullmap = Optional.of(map);
		gameState = new GameState(fullmap, playerStates, "game5");
	}

	@Test
	public void testWhenUpdateMapState_ThenPlayerInformationShouldBeUpdated() {
		// given

		initialize();
		// when
		PlayerGameInformation playerGameInformation = new PlayerGameInformation();
		ClientLogic logic = new ClientLogic(playerGameInformation);
		logic.updateMapState(gameState);

		// then
		assertThat(playerGameInformation.getMyPosition().getX(), is(equalTo(0)));
		assertThat(playerGameInformation.getMyPosition().getY(), is(equalTo(0)));

		assertThat(playerGameInformation.getMyFortPosition().getX(), is(equalTo(0)));
		assertThat(playerGameInformation.getMyFortPosition().getY(), is(equalTo(0)));

		assertThat(playerGameInformation.getEnemyPosition().getX(), is(equalTo(4)));
		assertThat(playerGameInformation.getEnemyPosition().getY(), is(equalTo(2)));

		assertThat(playerGameInformation.getEnemyFortPosition().getX(), is(equalTo(6)));
		assertThat(playerGameInformation.getEnemyFortPosition().getY(), is(equalTo(2)));

	}

	@Test
	public void testWhenSetUpPossibleTresurePositions_ThenPlayerInformationPossibleTreasurePositionsShouldBeGrass() {
		// given
		initialize();
		PlayerGameInformation gameInformation = new PlayerGameInformation();
		ClientLogic logic = new ClientLogic(gameInformation);
		logic.updateMapState(gameState);
		
		ArrayList<Point> points = gameInformation.getPossibleTreasurePositions();

		HashMap<Point, TerrainType> map = gameInformation.getMap();
		int counterMap = 0;

		// when
		logic.setUpPossibleTreasurePositions();
		Set<Entry<Point, TerrainType>> entryset = map.entrySet();
		for(Entry<Point, TerrainType> entry : entryset) {
			if(entry.getValue().equals(TerrainType.Grass)) {
				counterMap++;
			}
		}
		
		// consider that myPositions would not count
		counterMap -= 1;
		
		// then
		// consider that myPositions would not count
		assertThat(counterMap, is(equalTo(points.size())));
	}

	@Test
	public void testWhenGetPlayerState_ThenShouldBeMyGameStateReturned( ) {
		//given
		initialize();
		PlayerGameInformation gameInformation = new PlayerGameInformation();
		ClientLogic logic = new ClientLogic(gameInformation);
		logic.updateMapState(gameState);
		
		//when
		EPlayerGameState myState = logic.getPlayerState(gameState, myId);
		
		//then
		assertThat(myState, is(equalTo(EPlayerGameState.ShouldActNext)));
		
	}
	
	@Test
	public void testServerCall_ThenShouldBeAvailable() {
		//given
		
		
	}
	

}
