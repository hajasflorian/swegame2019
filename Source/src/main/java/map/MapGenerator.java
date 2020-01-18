package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.MapIsNotValidException;

public class MapGenerator {

	private final static Logger log = LoggerFactory.getLogger(MapGenerator.class);

	private LinkedHashMap<Point, TerrainType> map;
	private Point point;

	public MapGenerator() {
		this.point = new Point();
		this.map = new LinkedHashMap<Point, TerrainType>();
	}

	public LinkedHashMap<Point, TerrainType> createMap() {
		map.clear();
		try {
			boolean fortPresent = false;

			for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
				for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
					Point point = new Point(xCoordinate, yCoordinate, fortPresent);
					map.put(point, TerrainType.Grass);
				}
			}

			Set<Point> pointList = map.keySet();

			for (int i = 0; i < 4; i++) {
				Point randomPoint = randomPoint(pointList);
				placeMountain(randomPoint, pointList);
			}

			for (int i = 0; i < 5; i++) {
				Point randomPoint = randomPoint(pointList);
				placeWater(randomPoint, pointList);
			}
			
			try {
				checkMapValidation(map);
				placeFort(pointList);
			} catch (MapIsNotValidException e) {
				log.error(e.toString());
				createMap();
			}

		} catch (StackOverflowError e) {
			createMap();
		}

		return map;

	}

	protected void checkMapValidation(LinkedHashMap<Point, TerrainType> map) throws MapIsNotValidException {
		if (hasIslands(map)) {
			throw new MapIsNotValidException("Map has islands!");
		} else if(hasInvalidBorder(map)) {
			throw new MapIsNotValidException("Map has invalid border!");
		}
	}

	private void placeMountain(Point randomPoint, Set<Point> listOfPoints) {
		Point anotherPoint = randomPoint(listOfPoints);
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.Grass)) {
			map.put(randomPoint, TerrainType.Mountain);
			return;
		} else {
			placeMountain(anotherPoint, listOfPoints);
		}
	}

	private void placeWater(Point randomPoint, Set<Point> listOfPoints) {
		Point anotherPoint = randomPoint(listOfPoints);
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.Grass)) {
			map.put(randomPoint, TerrainType.Water);
				return;
		} else {
			placeWater(anotherPoint, listOfPoints);
		}
	}

	 protected boolean hasIslands(HashMap<Point, TerrainType> map) {

		String[][] halfMapArray = new String[8][4];
		Set<Entry<Point, TerrainType>> entrySet = map.entrySet();

		// hashMap to 2D array
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
				if (halfMapArray_copy[x][y].equals("Grass") || halfMapArray_copy[x][y].equals("Mountain")) {
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
		if (x < 0 || x == halfMapArray.length || y < 0 || y == halfMapArray[x].length || halfMapArray[x][y] == "Water")
			return;

		halfMapArray[x][y] = "Water";
		checkIsland(x + 1, y, halfMapArray);
		checkIsland(x - 1, y, halfMapArray);
		checkIsland(x, y + 1, halfMapArray);
		checkIsland(x, y - 1, halfMapArray);
	}

	 protected boolean hasInvalidBorder(HashMap<Point, TerrainType> map) {
		int countUpSide = 0;
		int countDownSide = 0;
		int countLeftSide = 0;
		int countRightSide = 0;

		Set<Entry<Point,TerrainType>> points = map.entrySet();
		
		for(Entry<Point, TerrainType> point: points) {
			for (int i = 0; i < 8; i++) {
				if (point.getKey().getX() == i && point.getKey().getY() == 0 && point.getValue().equals(TerrainType.Water)) {
					countUpSide++;
					if (countUpSide > 3) {
						return true;
					}
				}
			}

			for (int i = 0; i < 8; i++) {
				if (point.getKey().getX() == i && point.getKey().getY() == 3 && point.getValue().equals(TerrainType.Water)) {
					countDownSide++;
					if (countDownSide > 3) {
						return true;
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (point.getKey().getX() == 0 && point.getKey().getY() == i && point.getValue().equals(TerrainType.Water)) {
					countLeftSide++;
					if (countLeftSide > 1) {
						return true;
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				if (point.getKey().getX() == 7 && point.getKey().getY() == i && point.getValue().equals(TerrainType.Water)) {
					countRightSide++;
					if (countRightSide > 1) {
						return true;
					}
				}
			}
			
		}

		return false;
	}

	protected void placeFort(Set<Point> listOfPoints) {
		point = (Point) randomPoint(listOfPoints);
		if (map.get(point).equals(TerrainType.Grass)) {
			point.setFortPresent(true);
			map.put(point, TerrainType.Grass);
			return;
		} else {
			placeFort(listOfPoints);

		}
	}

	protected Point randomPoint(Set<Point> pointList) {
		int randomNumber = (int) (Math.random()*(((pointList.size()-1)-0)+1))+0;
		
		List<Point> mainList = new ArrayList<Point>();
		mainList.addAll(pointList);
		return mainList.get(randomNumber);
	}
	
	public LinkedHashMap<Point, TerrainType> getMap() {
		return map;
	}

	public void setMap(LinkedHashMap<Point, TerrainType> map) {
		this.map = map;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

}
