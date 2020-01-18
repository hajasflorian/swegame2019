package map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import exceptions.MapIsNotValidException;

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
	public void testWhenCreatingWrongMap_ThenMapIsNotValidExceptionShouldBeThrownCauseHasIslands() {
		MapGenerator mapgenerator = new MapGenerator();
		LinkedHashMap<Point, TerrainType> map = mapgenerator.getMap();
		Point point1 = new Point(0, 0, false);
		Point point2 = new Point(0, 1, false);
		Point point3 = new Point(0, 2, false);
		Point point4 = new Point(1, 0, false);
		Point point5 = new Point(1, 1, false);
		Point point6 = new Point(1, 2, false);
		Point point7 = new Point(2, 0, false);
		Point point8 = new Point(2, 1, false);
		Point point9 = new Point(2, 2, false);
		map.put(point1, TerrainType.Grass);
		map.put(point2, TerrainType.Water);
		map.put(point3, TerrainType.Grass);
		map.put(point4, TerrainType.Water);
		map.put(point5, TerrainType.Grass);
		map.put(point6, TerrainType.Water);
		map.put(point7, TerrainType.Grass);
		map.put(point8, TerrainType.Water);
		map.put(point9, TerrainType.Grass);

		// when
		Executable testCode = () -> mapgenerator.checkMapValidation(map);
		// then
		Assertions.assertThrows(MapIsNotValidException.class, testCode, "Map has islands!");

	}

	@Test
	public void testWhenCreatingWrongMap_ThenMapIsNotValidExceptionShouldBeThrownCaseHasInvalidBorder() {
		MapGenerator mapgenerator = new MapGenerator();
		LinkedHashMap<Point, TerrainType> map = mapgenerator.getMap();
		Point point1 = new Point(0, 0, false);
		Point point2 = new Point(0, 1, false);
		Point point3 = new Point(0, 2, false);
		Point point4 = new Point(1, 0, false);
		Point point5 = new Point(1, 1, false);
		Point point6 = new Point(1, 2, false);
		Point point7 = new Point(2, 0, false);
		Point point8 = new Point(2, 1, false);
		Point point9 = new Point(2, 2, false);
		map.put(point1, TerrainType.Water);
		map.put(point2, TerrainType.Water);
		map.put(point3, TerrainType.Grass);
		map.put(point4, TerrainType.Grass);
		map.put(point5, TerrainType.Grass);
		map.put(point6, TerrainType.Grass);
		map.put(point7, TerrainType.Grass);
		map.put(point8, TerrainType.Grass);
		map.put(point9, TerrainType.Grass);

		// when
		Executable testCode = () -> mapgenerator.checkMapValidation(map);
		// then
		Assertions.assertThrows(MapIsNotValidException.class, testCode, "Map has invalid border!");

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

		// then
		assertNotNull(randomPoint);
		assertThat(map.containsKey(randomPoint), is(equalTo(true)));

	}
	
//	@Test
//	public void test

}