package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class MapGenerator {

	private LinkedHashMap<Point, TerrainType> map;
	private Point point;

	public MapGenerator() {
		this.point = new Point();
		this.map = new LinkedHashMap<Point, TerrainType>();
	}

	public LinkedHashMap<Point, TerrainType> createMap() {
		try {
			boolean fortPresent = false;

			for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
				for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
					Point point = new Point(xCoordinate, yCoordinate, fortPresent);
					map.put(point, TerrainType.GRASS);
				}
			}

			Object[] pointList = map.keySet().toArray();

			for (int i = 0; i < 3; i++) {
				Object randomPoint = randomPoint(pointList);
				placeMountain(randomPoint, pointList);
			}

			for (int i = 0; i < 4; i++) {
				Object randomPoint = randomPoint(pointList);
				placeWater(randomPoint, pointList);
			}

			placeFort(pointList);
		} catch (StackOverflowError e) {
			createMap();
		}

		map.forEach((k, v) -> {
			System.out.println(k.getX() + "," + k.getY() + ", fort: " + k.getFortPresent() + ", " + v);
		});

		return map;

	}

	private void placeMountain(Object randomPoint, Object[] listOfPoints) {
		point = (Point) randomPoint;
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.put(point, TerrainType.MOUNTAIN);
			return;
		} else {
			Object anotherPoint = listOfPoints[new Random().nextInt(listOfPoints.length)];
			placeMountain(anotherPoint, listOfPoints);
		}
	}

	private void placeWater(Object randomPoint, Object[] listOfPoints) {
		point = (Point) randomPoint;
		Object anotherPoint = randomPoint(listOfPoints);
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.put(point, TerrainType.WATER);
			if (hasIslands(map)) {
				placeWater(anotherPoint, listOfPoints);
			} else 
			if (hasInvalidBorder(map)) {
				placeWater(anotherPoint, listOfPoints);
			} else
				return;
		} else {
			placeWater(anotherPoint, listOfPoints);
		}
	}

	private boolean hasIslands(HashMap<Point, TerrainType> map) {

		String[][] halfMapArray = new String[8][4];
		Set<Entry<Point, TerrainType>> entrySet = map.entrySet();

		// hashMap to 2dArray
		for (Entry<Point, TerrainType> pointTerrainTypeEntry : entrySet) {
			halfMapArray[pointTerrainTypeEntry.getKey().getX()][pointTerrainTypeEntry.getKey().getY()] = pointTerrainTypeEntry.getValue().toString();
		}
		
		// copy 2D array
		String[][] halfMapArray_copy = new String[8][4];

		for (int x = 0; x < halfMapArray.length; x++) {
			for (int y = 0; y < halfMapArray[0].length; y++) {
				halfMapArray_copy[x][y] = halfMapArray[x][y];
			}
		}

		int count = 0;

		for (int x = 0; x < halfMapArray.length; x++) {
			for (int y = 0; y < halfMapArray[0].length; y++) {
				if (halfMapArray_copy[x][y].equals("GRASS")) {
					count++;
					checkIsland(x, y, halfMapArray_copy);
				}

			}
		}
		if (count > 1)
			return true;
		else
			return false;
	}

	private void checkIsland(int x, int y, String[][] halfMapArray) {
		if (x < 0 || x == halfMapArray.length || y < 0 || y == halfMapArray[x].length || halfMapArray[x][y] == "WATER"
				|| halfMapArray[x][y] == "MOUNTAIN")
			return;

		halfMapArray[x][y] = "WATER";
		checkIsland(x + 1, y, halfMapArray);
		checkIsland(x - 1, y, halfMapArray);
		checkIsland(x, y + 1, halfMapArray);
		checkIsland(x, y - 1, halfMapArray);
	}

	private boolean hasInvalidBorder(HashMap<Point, TerrainType> map) {
		int countUpSide = 0;
		int countDownSide = 0;
		int countLeftSide = 0;
		int countRightSide = 0;

		Set<Entry<Point,TerrainType>> points = map.entrySet();
		
		for(Entry<Point, TerrainType> point: points) {
			for (int i = 0; i < 8; i++) {
				if (point.getKey().getX() == i && point.getKey().getY() == 0 && point.getValue().equals(TerrainType.WATER)) {
					countUpSide++;
					if (countUpSide > 3) {
						return true;
					}
				}
			}

//			count = 0;
			for (int i = 0; i < 8; i++) {
				if (point.getKey().getX() == i && point.getKey().getY() == 3 && point.getValue().equals(TerrainType.WATER)) {
					countDownSide++;
					if (countDownSide > 3) {
						return true;
					}
				}
			}

//			count = 0;
			for (int i = 0; i < 4; i++) {
				if (point.getKey().getX() == 0 && point.getKey().getY() == i && point.getValue().equals(TerrainType.WATER)) {
					countLeftSide++;
					if (countLeftSide > 1) {
						return true;
					}
				}
			}

//			count = 0;
			for (int i = 0; i < 4; i++) {
				if (point.getKey().getX() == 7 && point.getKey().getY() == i && point.getValue().equals(TerrainType.WATER)) {
					countRightSide++;
					if (countRightSide > 1) {
						return true;
					}
				}
			}
			
		}

		return false;
	}

	private void placeFort(Object[] listOfPoints) {
		point = (Point) randomPoint(listOfPoints);
		if (map.get(point).equals(TerrainType.GRASS)) {
			point.setFortPresent(true);
			map.put(point, TerrainType.GRASS);
			System.out.println(point.getX() + " " + point.getY());
			return;
		} else {
			placeFort(listOfPoints);

		}
	}

	private Object randomPoint(Object[] listOfPoints) {
		return listOfPoints[new Random().nextInt(listOfPoints.length)];
	}

}
