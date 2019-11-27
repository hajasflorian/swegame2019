package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MapGenerator {

	private HashMap<Point, TerrainType> map = new HashMap<Point, TerrainType>();
	private Point point = new Point();

	public HashMap<Point, TerrainType> createMap() {

		for (int xCoordinate = 0; xCoordinate < 8; xCoordinate++) {
			for (int yCoordinate = 0; yCoordinate < 4; yCoordinate++) {
				Point point = new Point(xCoordinate, yCoordinate);
				map.put(point, TerrainType.GRASS);
			}
		}

		for (int i = 0; i < 3; i++) {
			placeMountain(point.randomPoint());
		}

		for (int i = 0; i < 4; i++) {
			placeWater(point.randomPoint());
		}

		return map;

	}
	

	private void placeMountain(Point randomPoint) {
		if (map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.replace(randomPoint, TerrainType.MOUNTAIN);
			return;
		} else {
			placeMountain(randomPoint.randomPoint());
		}
	}

	private void placeWater(Point randomPoint) {
		if (map.get(randomPoint).equals(TerrainType.GRASS)) {
			map.replace(randomPoint, TerrainType.WATER);
			if (hasIslands(map)) {
				placeWater(randomPoint.randomPoint());
			} else if (hasInvalidBorder(map)) {
				placeWater(randomPoint.randomPoint());
			} else
				return;
		} else {
			placeWater(randomPoint.randomPoint());
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
