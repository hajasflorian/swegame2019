package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class MapGenerator {

	private HashMap<Point, TerrainType> map;
	private Point point;
	
	public MapGenerator() {
		this.point = new Point();
		this.map = new HashMap<Point, TerrainType>();
	}

	public HashMap<Point, TerrainType> createMap() {
		
		for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
			for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
				Point point = new Point(xCoordinate, yCoordinate);
				map.put(point, TerrainType.GRASS);
			}
		}
		
		System.out.println(map.keySet());
		
		Object[] pointList = map.keySet().toArray();

		for (int i = 0; i < 3; i++) {
			Object randomPoint = pointList[new Random().nextInt(pointList.length)];
			placeMountain(randomPoint, pointList);
		}

		for (int i = 0; i < 4; i++) {
			Object randomPoint = pointList[new Random().nextInt(pointList.length)];
			placeWater(randomPoint, pointList);
		}

		return map;

	}

	private void placeMountain(Object randomPoint, Object[] listOfPoints) {
		Point rPoint = (Point) randomPoint;
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.put(rPoint, TerrainType.MOUNTAIN);
			return;
		} else {
			Object anotherPoint = listOfPoints[new Random().nextInt(listOfPoints.length)];
			placeMountain(anotherPoint, listOfPoints);
		}
	}

	private void placeWater(Object randomPoint, Object[] listOfPoints) {
		Point rPoint = (Point) randomPoint;
		Object anotherPoint = listOfPoints[new Random().nextInt(listOfPoints.length)];
		if (map.containsKey(randomPoint) && map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.put(rPoint, TerrainType.WATER);
			if (hasIslands(map)) {
				placeWater(anotherPoint, listOfPoints);
			} else if (hasInvalidBorder(map)) {
				placeWater(anotherPoint, listOfPoints);
			} else
				return;
		} else {
			placeWater(anotherPoint, listOfPoints);
		}
	}

	private boolean hasIslands(HashMap<Point, TerrainType> map) {

		return false;
	}

	private boolean hasInvalidBorder(HashMap<Point, TerrainType> map) {
		int count = 0;

		Set<Point> points = map.keySet();

		for (Iterator<Point> it = points.iterator(); it.hasNext();) {
			Point point = it.next();
			for (int i = 0; i < 8; i++) {
				if (point.getX() == i && point.getY() == 0 && map.get(point).equals(TerrainType.WATER)) {
					count++;
					if (count > 3) {
						return true;
					}
				}
			}

			count = 0;
			for (int i = 0; i < 8; i++) {
				if (point.getX() == i && point.getY() == 3 && map.get(point).equals(TerrainType.WATER)) {
					count++;
					if (count > 3) {
						return true;
					}
				}
			}

			count = 0;
			for (int i = 0; i < 4; i++) {
				if (point.getX() == 0 && point.getY() == i && map.get(point).equals(TerrainType.WATER)) {
					count++;
					if (count > 3) {
						return true;
					}
				}
			}

			count = 0;
			for (int i = 0; i < 4; i++) {
				if (point.getX() == 7 && point.getY() == i && map.get(point).equals(TerrainType.WATER)) {
					count++;
					if (count > 3) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
