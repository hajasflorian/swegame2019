package map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import client.ClientLogic;
import client.main.Main;
import map.MapGenerator;
import map.Point;
import map.TerrainType;
import player.PlayerGameInformation;

public class MapGeneratorTest {

	@Test
	public void testWhenCreatingMap_ThenShouldNotHasIslands() {
		MapGenerator mapgenerator = new MapGenerator();
		// when
		HashMap<Point, TerrainType> map = mapgenerator.createMap();
		boolean hasMapIslands = mapgenerator.hasIslands(map);
		// then
		assertThat(hasMapIslands, is(equalTo(false)));
	}

	@Test
	public void testWhenCreatingMap_ThenShouldNotHasInvalidBoarders() {
		MapGenerator mapgenerator = new MapGenerator();
		// when
		HashMap<Point, TerrainType> map = mapgenerator.createMap();
		boolean hasInvalidBorder = mapgenerator.hasInvalidBorder(map);
		// then
		assertThat(hasInvalidBorder, is(equalTo(false)));
	}

	@Test
	public void testWhenCreatingMap_ThenShouldBeValid() {
		MapGenerator mapgenerator = new MapGenerator();
		// when
		LinkedHashMap<Point, TerrainType> map = mapgenerator.createMap();
		boolean hasInvalidBorder = mapgenerator.isMapValid(map);
		// then
		assertThat(hasInvalidBorder, is(equalTo(true)));
	}

	@Test
	public void testWhenPlaceFort_ThenThereIsOnePointWithFortPresent() {
		// given
		MapGenerator mapGenerator = new MapGenerator();
		HashMap<Point, TerrainType> map = mapGenerator.getMap();
		for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
			for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
				Point point = new Point(xCoordinate, yCoordinate, false);
				map.put(point, TerrainType.Grass);
			}
		}

		Set<Point> points = map.keySet();

		// when
		mapGenerator.placeFort(points);

		// then
		Set<Entry<Point, TerrainType>> entrySet = map.entrySet();
		int countFortPresent = 0;
		for (Entry<Point, TerrainType> pointTerrainTypeEntry : entrySet) {
			if (pointTerrainTypeEntry.getKey().getFortPresent() == true) {
				countFortPresent++;
			}
		}
		assertThat(countFortPresent, is(equalTo(1)));
	}

	@Test
	public void testWhenGetRandomPoint_RandomPointIsReturned() {
		// given
		MapGenerator mapGenerator = new MapGenerator();
		HashMap<Point, TerrainType> map = mapGenerator.getMap();
		for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
			for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
				Point point = new Point(xCoordinate, yCoordinate, false);
				map.put(point, TerrainType.Grass);
			}
		}

		Set<Point> points = map.keySet();

		// when
		Point randomPoint = mapGenerator.randomPoint(points);
		
		//then
		assertNotNull(randomPoint);
		assertThat(map.containsKey(randomPoint), is(equalTo(true)));

	}

}